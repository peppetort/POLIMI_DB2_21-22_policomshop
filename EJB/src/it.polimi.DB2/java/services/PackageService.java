package services;

import entities.*;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
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

    public List<Service> getAllService(){
        return em.createQuery("select s from Service s", Service.class).getResultList();
    }

    public List<OptionalProduct> getAllOptional(){
        return em.createQuery("select o from OptionalProduct o", OptionalProduct.class).getResultList();
    }

    public void saveNewServicePackage(String name, List<Long> servicesIDs, List<Long> optionalIDs){
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
    }
}
