package services;

import entities.AuditCustomer;
import entities.Order;
import entities.PackageOptionalStatistics;
import entities.PackagePurchasesStatistics;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.List;

@Stateless
public class ReportService {

    @PersistenceContext(unitName = "db2_project")
    EntityManager em;

    public List<PackagePurchasesStatistics> getAllStatPackagePurchases() throws PersistenceException {
        return em.createQuery("select stat from PackagePurchasesStatistics stat", PackagePurchasesStatistics.class).getResultList();
    }

    public List<PackageOptionalStatistics> getAllStatPackageOptional() throws PersistenceException {
        return em.createQuery("select stat from PackageOptionalStatistics stat", PackageOptionalStatistics.class).getResultList();
    }

    public List<AuditCustomer> getAllAuditCustomer() throws PersistenceException {
        return em.createQuery("select ac from AuditCustomer ac", AuditCustomer.class).getResultList();
    }

    public List<Order> getSuspendedOrder() throws PersistenceException {
        return em.createQuery("select o from Order o where o.status = ?1", Order.class).setParameter(1, Order.State.PAYMENT_FAILED).getResultList();
    }
}