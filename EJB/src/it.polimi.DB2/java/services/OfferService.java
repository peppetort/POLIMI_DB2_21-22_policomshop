package services;

import entities.Offer;
import entities.OptionalProduct;
import entities.ServicePackage;
import exception.OfferException;

import javax.ejb.Stateless;
import javax.persistence.*;
import javax.ws.rs.BadRequestException;

@Stateless
public class OfferService {
    @PersistenceContext(unitName = "db2_project")
    EntityManager em;

    public OfferService() {
    }

    public void createNewOffer(long idPackage, int validityPeriod, double monthlyFee) throws PersistenceException, OfferException {
        try {
            Query query = em.createQuery("select o.id from Offer o where o.servicePackage.id = :spId and o.validityPeriod = :validity and o.monthlyFee = :monthFee", Offer.class);
            query.setParameter("spId", idPackage);
            query.setParameter("validity", validityPeriod);
            query.setParameter("monthFee", monthlyFee);
            query.getSingleResult();
            throw new OfferException("Offer already exists");
        } catch (NoResultException e) {
            ServicePackage sp = em.find(ServicePackage.class, idPackage);

            if (sp == null) {
                throw new OfferException("Selected service package does not exists");
            }


            em.persist(new Offer(validityPeriod, monthlyFee, true, sp));
        }
    }
}
