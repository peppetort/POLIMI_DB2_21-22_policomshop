package services;

import entities.OptionalProduct;
import entities.Service;
import entities.ServicePackage;
import exception.OptionalProductException;
import exception.ServiceException;
import exception.ServicePackageException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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

    public ServicePackage findById(Long id) throws PersistenceException, ServicePackageException {
        ServicePackage servicePackage = em.find(ServicePackage.class, id);

        if (servicePackage == null) {
            throw new ServicePackageException("Service Package not found");
        }
        return servicePackage;
    }

    public List<ServicePackage> getAvailableServicePackages() throws PersistenceException {
        List<ServicePackage> servicePackages;
        servicePackages = em.createQuery("SELECT distinct o.servicePackage from Offer o where o.active = true", ServicePackage.class).getResultList();
        return servicePackages;
    }

    public List<Service> getAllService() throws PersistenceException {
        return em.createQuery("select s from Service s", Service.class).getResultList();
    }

    public Service getServiceById(Long id) throws ServiceException, PersistenceException {
        Service service = em.find(Service.class, id);

        if (service == null) {
            throw new ServiceException("Service not found");
        }
        return service;
    }

    public Long createNewServicePackage(String name, List<Long> servicesIDs, List<Long> optionalIDs) throws PersistenceException, OptionalProductException, ServicePackageException, ServiceException {
        List<Service> services = new ArrayList<>();
        List<OptionalProduct> optionalProductList = new ArrayList<>();
        for (Long i : servicesIDs) {
            Service service = getServiceById(i);
            services.add(service);
        }
        for (Long i : optionalIDs) {
            OptionalProduct optionalProduct = em.find(OptionalProduct.class, i);
            if (optionalProduct == null) {
                throw new OptionalProductException("Optional Product not found");
            }
            optionalProductList.add(em.find(OptionalProduct.class, i));
        }

        try {
            em.createQuery("SELECT sp.id from ServicePackage sp where sp.name = :name", ServicePackage.class).setParameter("name", name).getSingleResult();
            throw new ServicePackageException("Service Package " + name + " already exists");
        } catch (NoResultException e) {
            ServicePackage sp = new ServicePackage(name, services, optionalProductList);
            em.persist(sp);
            em.flush();
            return sp.getId();
        }
    }
}
