package services;

import entities.OptionalProduct;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class OptionalProdService {
    @PersistenceContext(name = "db2_project")
    private EntityManager em;

    public void saveNewProd(String name, Double monthlyFee) {
        em.persist(new OptionalProduct(name, monthlyFee));
    }

    public OptionalProduct getById(long id){
        return em.find(OptionalProduct.class, id);
    }
}
