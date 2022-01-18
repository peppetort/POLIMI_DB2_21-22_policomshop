package services;

import entities.OptionalProduct;
import exception.OptionalProductException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.List;

@Stateless
public class OptionalProductService {
    @PersistenceContext(name = "db2_project")
    private EntityManager em;

    public void createNewOptionalProduct(String name, Double monthlyFee) throws PersistenceException, OptionalProductException {
        try {
            em.createQuery("select o.id from OptionalProduct o where o.name = :name", OptionalProduct.class).setParameter("name", name).getSingleResult();
            throw new OptionalProductException("Optional product " + name + " already exists");
        } catch (NoResultException e) {
            em.persist(new OptionalProduct(name, monthlyFee));
        }

    }

    public OptionalProduct getById(long id) throws PersistenceException, OptionalProductException {
        OptionalProduct optionalProduct = em.find(OptionalProduct.class, id);

        if (optionalProduct == null) {
            throw new OptionalProductException("Optional Product not found");
        }
        return optionalProduct;
    }

    public List<OptionalProduct> getAllOptionalProducts() throws PersistenceException {
        return em.createQuery("select o from OptionalProduct o", OptionalProduct.class).getResultList();
    }
}
