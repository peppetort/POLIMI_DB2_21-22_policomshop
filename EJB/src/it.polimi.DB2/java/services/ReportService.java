package services;

import entities.PackageStatistics;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class ReportService {

    @PersistenceContext(unitName = "db2_project")
    EntityManager em;

    public List<PackageStatistics> getAllStatForPackage() {
        return em.createQuery("select stat from PackageStatistics stat").getResultList();
    }
}