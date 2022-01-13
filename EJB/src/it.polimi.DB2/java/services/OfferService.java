package services;

import entities.Offer;
import entities.ServicePackage;
import exception.OfferNotFound;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.List;

@Stateless
public class OfferService {
    @PersistenceContext(unitName = "db2_project")
    EntityManager em;

    public OfferService() {
    }

    public List<Offer> getAvailableOffer() throws OfferNotFound {
        List<Offer> availableOffer;
        try {
            availableOffer = em.createQuery("SELECT o FROM Offer o WHERE o.active = true", Offer.class).getResultList();
        } catch (PersistenceException e) {
            throw new OfferNotFound("Error getting Offer");
        }
        return availableOffer;
    }

    public Offer getOfferById(int offerId) {
        return em.find(Offer.class, offerId);
    }

    public void saveNew(long idPackage, int validityPeriod, double monthlyFee) {
        ServicePackage sp = em.find(ServicePackage.class, idPackage);
        em.persist(new Offer(validityPeriod, monthlyFee, true, sp));
    }
}
