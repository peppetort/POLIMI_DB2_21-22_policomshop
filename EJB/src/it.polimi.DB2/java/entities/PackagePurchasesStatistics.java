package entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@IdClass(PackagePurchasesStatistics.PackageValidityPeriod.class)
@Table(name = "stat_service_package", schema = "db2_project")
public class PackagePurchasesStatistics {

    @Id
    @Column(name = "id_package", insertable = false, updatable = false)
    private Long idPackage;
    @Id
    @Column(name = "validity_period", insertable = false)
    private int validityPeriod;
    @Column(name = "num_purchases")
    private int numPurchases;
    @Column(name = "amount_with_optional")
    private double amountWithOptional;
    @Column(name = "amount_without_optional")
    private double amountWithoutOptional;
    @ManyToOne
    @JoinColumn(name = "id_package", nullable = false)
    private ServicePackage servicePackage;

    public int getValidityPeriod() {
        return validityPeriod;
    }

    public int getNumPurchases() {
        return numPurchases;
    }

    public double getAmountWithOptional() {
        return amountWithOptional;
    }

    public double getAmountWithoutOptional() {
        return amountWithoutOptional;
    }

    public ServicePackage getServicePackage() {
        return servicePackage;
    }

    public static class PackageValidityPeriod implements Serializable {
        private Long idPackage;
        private int validityPeriod;

        public PackageValidityPeriod() {
        }

        public PackageValidityPeriod(Long idPackage, int validityPeriod) {
            this.idPackage = idPackage;
            this.validityPeriod = validityPeriod;
        }
    }
}
