package services;

import entities.Offer;
import entities.ServicePackage;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.ws.rs.BadRequestException;

@Stateless
public class OfferService {
    @PersistenceContext(unitName = "db2_project")
    EntityManager em;

    public OfferService() {
    }

    public void createNewOffer(long idPackage, int validityPeriod, double monthlyFee) {
        try {
            ServicePackage sp = em.find(ServicePackage.class, idPackage);
            em.persist(new Offer(validityPeriod, monthlyFee, true, sp));
        }catch (PersistenceException e){
            throw new BadRequestException();
        }
    }
}
