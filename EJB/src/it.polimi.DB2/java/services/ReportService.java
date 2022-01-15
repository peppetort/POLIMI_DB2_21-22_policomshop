package services;

import entities.*;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.ws.rs.BadRequestException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class ReportService {

    @PersistenceContext(unitName = "db2_project")
    EntityManager em;

    public Map<Offer, Long> getTotalPurchasePerPackageAndValidityPeriod() {
        try {
            Map<Offer, Long> stat = new HashMap<>();
            List<Offer> offerList = em.createQuery("select o from Offer o order by o.servicePackage.id", Offer.class).getResultList();

            for (Offer o : offerList) {
                Long numberOfPurchasedOffer = em.createQuery("select count(a) from ActivationSchedule a where a.offer.id = :offerId ", Long.class).setParameter("offerId", o.getId()).getSingleResult();
                stat.put(o, numberOfPurchasedOffer);
            }

            return stat;
        } catch (PersistenceException e) {
            throw new BadRequestException();
        }
    }

    public List<PackagePurchasesStatistics> getAllStatPackagePurchases() {
        return em.createQuery("select stat from PackagePurchasesStatistics stat", PackagePurchasesStatistics.class).getResultList();
    }

    public List<PackageOptionalStatistics> getAllStatPackageOptional() {
        return em.createQuery("select stat from PackageOptionalStatistics stat", PackageOptionalStatistics.class).getResultList();
    }

    public List<AuditCustomer> getAllAuditCustomer() {
        return em.createQuery("select ac from AuditCustomer ac", AuditCustomer.class).getResultList();
    }

    public List<Order> getSuspendedOrder() {
        return em.createQuery("select o from Order o where o.status = ?1", Order.class).setParameter(1, Order.State.PAYMENT_FAILED).getResultList();
    }
}