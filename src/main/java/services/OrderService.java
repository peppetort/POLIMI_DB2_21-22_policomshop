package services;

import entities.*;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class OrderService {
    @PersistenceContext(unitName = "db2_project")
    EntityManager em;

    public OrderService() {
    }

    public List<Order> getRejectedOrdersByCustomer(Long customerId) {
        List<Order> rejectedOrder;
        rejectedOrder = em.createNamedQuery("Order.rejectedOrders", Order.class)
                .setParameter(1, customerId).setParameter(2, Order.State.PAYMENT_FAILED)
                .getResultList();
        return rejectedOrder;

    }
}
