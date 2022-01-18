package services;

import entities.*;
import utils.PaymentRevisionBot;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.StatefulTimeout;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceException;
import javax.ws.rs.BadRequestException;
import java.io.Serializable;
import java.util.*;

/**
 * Http Session is a perfect storage place where all request from same web client have access
 * to all attributes stored in http session. So you ask for it from the container and keep it in a place (http session)
 * from where it can be referred again by all requests for same session.
 * Therefore, if in the session there's no attribute userQuestionnaire, it means that it's the first time the user is fulfilling
 * the product's questionnaire
 * Then the active HTTP session becomes the "EJB client" and maintains the instances.
 */

@Stateful
@StatefulTimeout(value = 10)
public class BuyService implements Serializable {

    @PersistenceContext(unitName = "db2_project", type = PersistenceContextType.EXTENDED)
    private EntityManager em;

    /* ---- Object Status ---- */
    private Order order;
    private ServicePackage servicePackage;
    private Map<OptionalProduct, Boolean> optionalProductBooleanMap;

    public void initOrder(ServicePackage servicePackage) throws PersistenceException {
        order = new Order();
        optionalProductBooleanMap = new HashMap<>();
        this.servicePackage = servicePackage;
        for (OptionalProduct o : servicePackage.getOptionalProductList()) {
            optionalProductBooleanMap.put(o, Boolean.FALSE);
        }
    }

    public void setOffer(int id) throws PersistenceException, BadRequestException {
        /*The only valid flow to call this method is via the CustomizeOrder post method,
        so the user must have already accessed the get method of the same controller -> This bean is already initialized*/
        if (order == null) throw new BadRequestException();
        Offer offer = em.find(Offer.class, id);
        if (offer == null || !offer.isActive()) throw new BadRequestException();
        if (offer.equals(order.getOffer())) return;
        if (!offer.getServicePackage().equals(this.servicePackage)) throw new BadRequestException();
        order.setOffer(offer);
        order.setTotalMonthlyFee(offer.getMonthlyFee());
    }

    public void setOptionalProducts(List<Integer> optionalProductIds) throws BadRequestException, PersistenceException {
        if (optionalProductBooleanMap == null) throw new BadRequestException();
        if (servicePackage == null) throw new BadRequestException();

        for (OptionalProduct o : servicePackage.getOptionalProductList()) {
            optionalProductBooleanMap.put(o, Boolean.FALSE);
        }
        order.getOptionalProductSet().clear();

        if (order.getOffer() != null) {
            order.setTotalMonthlyFee(order.getOffer().getMonthlyFee());
        } else {
            order.setTotalMonthlyFee(0);
        }

        for (int id : optionalProductIds) {
            OptionalProduct optionalProduct = em.find(OptionalProduct.class, (long) id);

            if (optionalProduct == null) {
                throw new BadRequestException();
            }
            if (optionalProductBooleanMap.replace(optionalProduct, true) == null) {
                throw new BadRequestException();
            }
            double tot = order.getTotalMonthlyFee();
            order.setTotalMonthlyFee(tot + optionalProduct.getMonthlyFee());
            order.getOptionalProductSet().add(optionalProduct);
        }
    }

    public void setStartDate(Date date) {
        if (order == null) throw new BadRequestException();
        order.setActivationDate(date);
    }

    public Order getOrder() {
        if (order == null) throw new BadRequestException();
        return order;
    }

    public ServicePackage getServicePackage() {
        if (servicePackage == null) throw new BadRequestException();
        return servicePackage;
    }

    public Map<OptionalProduct, Boolean> getOptionalProduct() {
        if (optionalProductBooleanMap == null) throw new BadRequestException();
        return optionalProductBooleanMap;
    }

    @Remove
    public boolean executePayment(Customer customer) throws PersistenceException {
        /*Specification: "When the user presses the BUY button, an order is created",
         * so the creation date is set manually only in this method*/
        if (order.getOffer() == null || order.getActivationDate() == null) {
            throw new BadRequestException();
        }

        order.setCustomer(customer);
        order.setCreationDate(new Date());
        Calendar c = Calendar.getInstance();
        c.setTime(order.getActivationDate());
        c.add(Calendar.MONTH, order.getOffer().getValidityPeriod());
        order.setDeactivationDate(c.getTime());
        boolean isPaymentValid = PaymentRevisionBot.review();

        if (isPaymentValid) {
            order.setStatus(Order.State.PAID);
        } else {
            order.setStatus(Order.State.PAYMENT_FAILED);
            customer.addOneFailedPayment();
            if (customer.isAudit()) {
                AuditCustomer a = new AuditCustomer(customer, order.getTotalMonthlyFee());
                em.persist(a);
            }
            em.merge(customer);
        }
        em.persist(order);
        return isPaymentValid;
    }

    @Remove
    public void stopProcess() {
    }

}