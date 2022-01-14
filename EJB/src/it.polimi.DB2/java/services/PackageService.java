package services;

import entities.OptionalProduct;
import entities.Service;
import entities.ServicePackage;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.ws.rs.BadRequestException;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class PackageService {
    @PersistenceContext(unitName = "db2_project")
    EntityManager em;

    public PackageService() {
    }

    public ServicePackage findById(Long id) {
        try {
            return em.find(ServicePackage.class, id);
        } catch (PersistenceException ignored) {
            throw new BadRequestException();
        }
    }

    public List<ServicePackage> getAvailableServicePackages() {
        try {
            List<ServicePackage> servicePackages;
            servicePackages = em.createQuery("SELECT distinct o.servicePackage from Offer o where o.active = true", ServicePackage.class).getResultList();
            return servicePackages;
        } catch (PersistenceException e) {
            throw new BadRequestException();
        }
    }

    public List<Service> getAllService() {
        try {
            return em.createQuery("select s from Service s", Service.class).getResultList();
        } catch (PersistenceException e) {
            throw new BadRequestException();
        }
    }

    public Service getServiceById(Long id) {
        try {
            return em.find(Service.class, id);
        } catch (PersistenceException e) {
            throw new BadRequestException();
        }
    }

    public Long createNewServicePackage(String name, List<Long> servicesIDs, List<Long> optionalIDs) {
        try {
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
        } catch (PersistenceException e) {
            throw new BadRequestException();
        }
    }
}
