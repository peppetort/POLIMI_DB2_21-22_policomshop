package services;

import entities.*;
import exception.OrderNotFound;
import utils.PaymentRevisionBot;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.List;

@Stateless
public class OrderService {
    @PersistenceContext(unitName = "db2_project")
    EntityManager em;

    public OrderService() {
    }

    public List<Order> getRejectedOrdersByCustomer(Long customerId) throws OrderNotFound {
        List<Order> rejectedOrder;
        try {
            rejectedOrder = em.createNamedQuery("Order.rejectedOrders", Order.class)
                    .setParameter(1, customerId).setParameter(2, Order.State.PAYMENT_FAILED)
                    .getResultList();
        } catch (PersistenceException e) {
            throw new OrderNotFound("Error getting Order");
        }
        return rejectedOrder;
    }

    public boolean updateCustomerOrderPayment(int idOrder, Long idUser) throws OrderNotFound {
        List<Order> rejectedOrder;
        try {
            rejectedOrder = em.createNamedQuery("Order.rejectedOrdersByID", Order.class)
                    .setParameter(1, idOrder).setParameter(2, idUser).setParameter(3, Order.State.PAYMENT_FAILED)
                    .getResultList();
            if (rejectedOrder != null && rejectedOrder.size() == 1) {
                Order order = rejectedOrder.get(0);
                Customer customer = order.getCustomer();
                boolean isPaymentValid = PaymentRevisionBot.review();

                if (isPaymentValid) {
                    order.setStatus(Order.State.PAID);
                    customer.removeOneFailedPayment();
                    if (customer.isAuditCustomer() && customer.getNumFailedPayments() == 0) {
                        customer.setAuditCustomer(false);
                        AuditCustomer a = em.find(AuditCustomer.class, customer.getId());
                        em.remove(a);
                    }
                } else {
                    order.setStatus(Order.State.PAYMENT_FAILED);
                }
                em.persist(order);
                return isPaymentValid;
            } else {
                throw new OrderNotFound(null);
            }

        } catch (PersistenceException e) {
            throw new OrderNotFound("Error getting Order");
        }
    }
}
