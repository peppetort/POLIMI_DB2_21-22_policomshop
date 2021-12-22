package services;

import entities.*;
import exception.OrderNotFound;

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
        List<Order> rejectedOrder = null;
        try {
            rejectedOrder = em.createNamedQuery("Order.rejectedOrders", Order.class)
                    .setParameter(1, customerId).setParameter(2, Order.State.PAYMENT_FAILED)
                    .getResultList();
        } catch (PersistenceException e) {
            throw new OrderNotFound("Error getting Order");
        }
        return rejectedOrder;
    }

    public Order getRejectedOrderByIdAndUser(int idOrder, Long idUser) throws OrderNotFound {
        List<Order> rejectedOrder = null;
        try {
            rejectedOrder = em.createNamedQuery("Order.rejectedOrdersByID", Order.class)
                    .setParameter(1, idOrder).setParameter(2, idUser).setParameter(3, Order.State.PAYMENT_FAILED)
                    .getResultList();
        } catch (PersistenceException e) {
            throw new OrderNotFound("Error getting Order");
        }

        if (rejectedOrder != null && rejectedOrder.size() == 1) {
            return rejectedOrder.get(0);
        }
        return null;

    }
}
