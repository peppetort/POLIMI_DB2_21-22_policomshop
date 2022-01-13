package services;

import entities.OptionalProduct;
import entities.Service;
import entities.ServicePackage;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class PackageService {
    @PersistenceContext(unitName = "db2_project")
    EntityManager em;

    public PackageService() {
    }

    public ServicePackage findById(Long id) {
        return em.find(ServicePackage.class, id);
    }

    public List<ServicePackage> getAvailableServicePackages() {
        List<ServicePackage> servicePackages;
        servicePackages = em.createQuery("SELECT distinct o.servicePackage from Offer o where o.active = true", ServicePackage.class).getResultList();
        return servicePackages;
    }

    public List<ServicePackage> getAllServicePackages() {
        return em.createQuery("SELECT distinct sp from ServicePackage sp", ServicePackage.class).getResultList();
    }

    public List<OptionalProduct> getAvailableOptionalProductByPackage(Long servicePackageId) {
        ServicePackage servicePackage;
        servicePackage = em.find(ServicePackage.class, servicePackageId);
        return servicePackage.getOptionalProductList();
    }

    public List<Service> getAllService() {
        return em.createQuery("select s from Service s", Service.class).getResultList();
    }

    public Service getServiceById(Long id) {
        return em.find(Service.class, id);
    }

    public List<OptionalProduct> getAllOptional() {
        return em.createQuery("select o from OptionalProduct o", OptionalProduct.class).getResultList();
    }

    public Long createNewServicePackage(String name, List<Long> servicesIDs, List<Long> optionalIDs) {
        List<Service> services = new ArrayList<>();
        List<OptionalProduct> optionalProductList = new ArrayList<>();
        for (Long i : servicesIDs) {
            services.add(em.find(Service.class, i));
        }
        for (Long i : optionalIDs) {
            optionalProductList.add(em.find(OptionalProduct.class, i));
        }
        ServicePackage sp = new ServicePackage(name, services, optionalProductList);
        em.persist(sp);
        em.flush();
        return sp.getId();
    }
}
