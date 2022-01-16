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

//    public Map<ServicePackage, Long> getTotalPurchasesPerPackage() {
//        try {
//            Map<ServicePackage, Long> stat = new HashMap<>();
//            List<ServicePackage> servicePackageList = em.createQuery("select sp from ServicePackage sp", ServicePackage.class).getResultList();
//
//            for (ServicePackage sp : servicePackageList) {
//                Long numberOfPurchasedPackage = em.createQuery("select count(a) from ActivationSchedule a where a.servicePackage.id = :servicePackageId", Long.class).setParameter("servicePackageId", sp.getId()).getSingleResult();
//                stat.put(sp, numberOfPurchasedPackage);
//            }
//
//            return stat;
//        } catch (PersistenceException e) {
//            throw new BadRequestException();
//        }
//    }
//
//    public Map<Offer, Long> getTotalPurchasePerPackageAndValidityPeriod() {
//        try {
//            Map<Offer, Long> stat = new HashMap<>();
//            List<Offer> offerList = em.createQuery("select o from Offer o order by o.servicePackage.id", Offer.class).getResultList();
//
//            for (Offer o : offerList) {
//                Long numberOfPurchasedOffer = em.createQuery("select count(a) from ActivationSchedule a where a.offer.id = :offerId ", Long.class).setParameter("offerId", o.getId()).getSingleResult();
//                stat.put(o, numberOfPurchasedOffer);
//            }
//
//            return stat;
//        } catch (PersistenceException e) {
//            throw new BadRequestException();
//        }
//    }

    public List<PackageStatistics> getAllStatForPackage() throws PersistenceException{
        return em.createQuery("select stat from PackageStatistics stat", PackageStatistics.class).getResultList();
    }

    public List<AuditCustomer> getAllAuditCustomer() throws PersistenceException{
        return em.createQuery("select ac from AuditCustomer ac", AuditCustomer.class).getResultList();
    }

    public List<Order> getSuspendedOrder() throws PersistenceException{
        return em.createQuery("select o from Order o where o.status = ?1", Order.class).setParameter(1, Order.State.PAYMENT_FAILED).getResultList();
    }
}