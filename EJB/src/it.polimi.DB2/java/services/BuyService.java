package services;

import entities.*;

import javax.ejb.EJB;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.SynchronizationType;
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

@Stateful
public class BuyService implements Serializable {

    @PersistenceContext(unitName = "db2_project", type = PersistenceContextType.EXTENDED, synchronization = SynchronizationType.UNSYNCHRONIZED)
    private EntityManager em;
    @EJB(beanName = "OrderService")
    OrderService orderService;

    /* ---- Object Status ---- */
    private Order order;
    private ServicePackage servicePackage;
    private final Map<OptionalProduct, Boolean> optionalProductBooleanMap = new HashMap<>();


    /* -------- PUBLIC METHODS -------- */
    public void init(Customer customer, int id_service) {
        order = new Order(customer);
        setServicePackage(id_service);
        /*A bean could be refreshed by  */
        optionalProductBooleanMap.clear();
    }

    public boolean isInitialized() {
        return servicePackage != null;
    }

    public ServicePackage getServicePackage() {
        return servicePackage;
    }

    public void setOffer(int id) {
        /*The only valid flow to call this method is via the CustomizeOrder post method,
        so the user must have already accessed the get method of the same controller -> This bean is already initialized*/
        if (order == null) throw new BadRequestException();
        Offer offer = em.find(Offer.class, id);
        if (offer == null || !offer.isActive()) throw new BadRequestException();
        if (offer.equals(order.getOffer())) return;
        if (!offer.getServicePackage().equals(this.servicePackage)) throw new BadRequestException();
        order.setOffer(offer);
    }

    public Map<OptionalProduct, Boolean> getOptionalProduct() {
        return optionalProductBooleanMap;
    }

    public void setOptionalProducts(String[] opProds) throws IllegalAccessException {
        initOptionalProductBooleanMap();
        order.getOptionalProductList().clear();
        for (String opId : opProds) {
            int idOpProd = Integer.parseInt(opId);
            Optional<OptionalProduct> optional = optionalProductBooleanMap.keySet().stream().filter(x -> x.getId() == idOpProd).findAny();
            if (optional.isEmpty()) throw new IllegalAccessException();
            OptionalProduct o = optional.get();
            optionalProductBooleanMap.replace(o, Boolean.TRUE);
            order.getOptionalProductList().add(o);
        }
    }

    public void setStartDate(Date date) throws IllegalAccessException {
        if (new Date().before(date)) order.setStartDate(date);
        else throw new IllegalAccessException();
    }

    public Order getOrder() {
        if (order != null) order.setTotalMonthlyFee(evaluateMonthlyFee());
        return order;
    }

    public boolean isCorrectFilled(boolean userIsImportant) {
        return order.isCorrectFilled(userIsImportant);
    }

    /* I decided to implement this function in java business logic and not with a Trigger in the db,
    because there are two main cases where a user can fail a payment:
    1 - Create a new order
    2 - Tries to refund a failed payment
    In the second use case there is no query on any table, so the only method is to directly update the value with a query.
    So I prefer to always use this java method and avoid the trigger to ensure code maintainability
    Also for performance reason*/

    public boolean executePayment() {
        if (!order.isCorrectFilled(true))
            throw new BadRequestException("Non sei loggato / Il tuo ordine non è stato compilato correttamente");
        /*Specification: "When the user presses the BUY button, an order is created",
         * so the creation date is set manually only in this method*/
        if (order.getCreationDate() == null) order.setCreationDate(new Date());
        boolean flag = randomPayment();
        if (flag) {
            order.setStatus(Order.State.PAID);
        } else {
            setAsInsolvent();
        }
        em.persist(order);
        em.merge(order.getCustomer());
        checkout();
        return flag;
    }

    public boolean retryPayment(int idOrder, Customer customer) {
        Order o = orderService.getRejectedOrderByIdAndUser(idOrder, customer.getId());
        if (o != null) {
            order = o;
            boolean flag = executePayment();
            if (flag) {
                customer.removeOneFailedPayment();
                if (customer.getNumFailedPayments() == 0) {
                    em.remove(em.find(AuditCustomer.class, customer.getId()));
                }
                em.merge(customer);
                return true;
            }
            return false;
        }
        throw new BadRequestException();
    }

    @Remove
    public void checkout() {
        em.joinTransaction();
    }

    /* ----- PRIVATE METHODS ------ */
    private void setServicePackage(int id) {
        ServicePackage servicePackage = em.find(ServicePackage.class, id);
        if (servicePackage == null) throw new BadRequestException();
        this.servicePackage = servicePackage;
        initOptionalProductBooleanMap();
    }

    private void initOptionalProductBooleanMap() {
        for (OptionalProduct o : servicePackage.getOptionalProductList()) {
            optionalProductBooleanMap.put(o, Boolean.FALSE);
        }
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

    private boolean randomPayment() {
        return new Random().nextBoolean();
    }

    private void setAsInsolvent() {
        if (order.getStatus() == Order.State.PAYMENT_FAILED) return;
        order.getCustomer().addOneFailedPayment();
        if (order.getCustomer().getNumFailedPayments() >= 3) {
            AuditCustomer a = em.find(AuditCustomer.class, order.getCustomer().getId());
            if (a == null) {
                a = new AuditCustomer(order.getCustomer(), order.getTotalMonthlyFee(), order.getCreationDate());
                em.persist(a);
            } else {
                a.setAmount(order.getTotalMonthlyFee());
                a.setLastRejection(order.getCreationDate());
            }
        }
        order.setStatus(Order.State.PAYMENT_FAILED);
    }
}