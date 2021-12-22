package services;

import entities.*;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class PackageService {
    @PersistenceContext(unitName = "db2_project")
    EntityManager em;

    public PackageService() {
    }

    public ServicePackage findById(int id) {
        return em.find(ServicePackage.class, id);
    }

    public List<ServicePackage> getAvailableServicePackages() {
        List<ServicePackage> servicePackages;
        servicePackages = em.createQuery("SELECT distinct o.servicePackage from Offer o where o.active = true", ServicePackage.class).getResultList();
        return servicePackages;
    }

    public List<OptionalProduct> getAvailableOptionalProductByPackage(int servicePackageId) {
        ServicePackage servicePackage;
        servicePackage = em.find(ServicePackage.class, servicePackageId);
        return servicePackage.getOptionalProductList();
    }
}
