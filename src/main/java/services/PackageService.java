package services;

import entities.*;
import exception.UserNotFound;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.List;

@Stateless
public class PackageService {
    @PersistenceContext(unitName = "db2_project")
    EntityManager em;

    public PackageService() {
    }

    public List<Offer> getAvailableOffer() {
        List<Offer> availableOffer;
        availableOffer = em.createQuery("SELECT o FROM Offer o WHERE o.active = true", Offer.class).getResultList();
        return availableOffer;
    }

    public Offer getOfferById(int offerId) {
        return em.find(Offer.class, offerId);
    }

    public List<OptionalProduct> getAvailableOptionalProductByPackage(int servicePackageId) {
        ServicePackage servicePackage;
        servicePackage = em.find(ServicePackage.class, servicePackageId);
        em.refresh(servicePackage);
        return servicePackage.getOptionalProductList();
    }
}
