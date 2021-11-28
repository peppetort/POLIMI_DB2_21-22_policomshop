package services;

import entities.Customer;
import entities.Offer;
import entities.OptionalProduct;
import entities.Order;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.ws.rs.BadRequestException;
import java.io.Serializable;
import java.util.*;

/**
 * Http Session is a perfect storage place where all request from same web client have access
 * to all attributes stored in http session. So you ask for it from the container and keep it in a place (http session)
 * from where it can be referred again by all requests for same session.
 * Therefore, if in the session there's no attribute userQuestionnaire, it means that it's the first time the user is fulfilling
 * the product's questionnaire
 * -> @Stateful EJBs can be also denoted with @SessionScoped annotation.
 * Then the active HTTP session becomes the "EJB client" and maintains the instances.
 */
@SessionScoped
@Stateful
public class BuyService implements Serializable {
    private final Map<OptionalProduct, Boolean> optionalProductBooleanMap = new HashMap<>();
    @PersistenceContext(unitName = "db2_project", type = PersistenceContextType.EXTENDED)
    private EntityManager em;
    private Order order;

    public void init(Customer customer) {
        order = new Order(customer);
        order.setCreationDate(new Date());
        order.setTotalMonthlyFee(0);
        optionalProductBooleanMap.clear();
    }

    public void setOffer(int id) throws IllegalAccessException {
        Offer se = em.find(Offer.class, id);
        if (se == null || !se.isActive()) throw new IllegalAccessException();
        if (se.equals(order.getOffer())) return;
        order.setOffer(se);
        for (OptionalProduct o : order.getOffer().getServicePackage().getOptionalProductList()) {
            optionalProductBooleanMap.put(o, Boolean.FALSE);
        }
    }

    public Map<OptionalProduct, Boolean> getOptionalProduct() {
        return optionalProductBooleanMap;
    }

    public void addOptionalProduct(int idOpProd) throws IllegalAccessException {
        Optional<OptionalProduct> optional = optionalProductBooleanMap.keySet().stream().filter(x -> x.getId() == idOpProd).findAny();
        if (optional.isEmpty()) throw new IllegalAccessException();
        OptionalProduct o = optional.get();
        optionalProductBooleanMap.replace(o, Boolean.TRUE);
        order.getOptionalProductList().add(o);
    }

    public void setStartDate(Date date) throws IllegalAccessException {
        if (new Date().before(date)) order.setStartDate(date);
        else throw new IllegalAccessException();
    }

    public void removeItem(OptionalProduct op) {
        optionalProductBooleanMap.replace(op, Boolean.FALSE);
    }

    private double evaluateMonthlyFee() {
        if (order.getOffer() == null) return 0;
        double sum = 0;
        sum = sum + order.getOffer().getMonthlyFee();
        for (Map.Entry<OptionalProduct, Boolean> e : optionalProductBooleanMap.entrySet()) {
            if (e.getValue()) sum = sum + e.getKey().getMonthlyFee();
        }
        return sum;
    }

    public Order getOrder() {
        if (order != null) order.setTotalMonthlyFee(evaluateMonthlyFee());
        return order;
    }

    public boolean isCorrectFilled(boolean userIsImportant) {
        return order.isCorrectFilled(userIsImportant);
    }

    public boolean executePayment() {
        if (!order.isCorrectFilled(true))
            throw new BadRequestException("Non sei loggato / Il tuo utente non è legato all'ordine");
        boolean flag = randomPayment();
        if (flag) {
            order.setStatus(Order.State.PAID);
        } else {
            order.setStatus(Order.State.PAYMENT_FAILED);
        }
        em.persist(order);
        return flag;
    }

    private boolean randomPayment() {
        return new Random().nextBoolean();
    }

    @Remove
    public void checkout() {
    }
}