package services;

import entities.OptionalProduct;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.ws.rs.BadRequestException;
import java.util.List;

@Stateless
public class OptionalProductService {
    @PersistenceContext(name = "db2_project")
    private EntityManager em;

    public void saveNewProd(String name, Double monthlyFee) {
        try {
            em.persist(new OptionalProduct(name, monthlyFee));
        } catch (PersistenceException e) {
            throw new BadRequestException();
        }
    }

    public OptionalProduct getById(long id) {
        try {
            return em.find(OptionalProduct.class, id);
        } catch (PersistenceException e) {
            throw new BadRequestException();
        }
    }

    public List<OptionalProduct> getAllOptionalProducts() {
        try {
            return em.createQuery("select o from OptionalProduct o", OptionalProduct.class).getResultList();
        } catch (PersistenceException e) {
            throw new BadRequestException();
        }
    }
}
