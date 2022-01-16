package services;

import entities.AuditCustomer;
import entities.Customer;
import entities.Order;
import exception.OrderException;
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

    public List<Order> getRejectedOrdersByCustomer(Long customerId) throws PersistenceException {
        List<Order> rejectedOrder;
        rejectedOrder = em.createNamedQuery("Order.rejectedOrders", Order.class)
                .setParameter(1, customerId).setParameter(2, Order.State.PAYMENT_FAILED)
                .getResultList();
        return rejectedOrder;
    }

    public boolean updateCustomerOrderPayment(int idOrder, Long idUser) throws PersistenceException, OrderException {
        List<Order> rejectedOrder;
        rejectedOrder = em.createNamedQuery("Order.rejectedOrdersByID", Order.class)
                .setParameter(1, idOrder).setParameter(2, idUser).setParameter(3, Order.State.PAYMENT_FAILED)
                .getResultList();

        if (rejectedOrder != null && rejectedOrder.size() == 1) {
            Order order = rejectedOrder.get(0);
            Customer customer = order.getCustomer();
            boolean isPaymentValid = PaymentRevisionBot.review();

            if (isPaymentValid) {
                order.setStatus(Order.State.PAID);
            } else {
                order.setStatus(Order.State.PAYMENT_FAILED);
                customer.addOneFailedPayment();
                AuditCustomer a = new AuditCustomer(customer, order.getTotalMonthlyFee(), order.getCreationDate());
                em.persist(a);
            }
            em.persist(order);
            return isPaymentValid;
        } else {
            throw new OrderException("Order not found");
        }

    }
}
