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
    @Column(name = "tot_monthly_fee")
    private double totalMonthlyFee;
    @Column(name = "amount_for_optional_prods")
    private double amountForOptionalProds;
    @ManyToOne
    @JoinColumn(name = "id_package", nullable = false, updatable = false)
    private ServicePackage servicePackage;

    public int getValidityPeriod() {
        return validityPeriod;
    }

    public int getNumPurchases() {
        return numPurchases;
    }

    public double getTotalMonthlyFee() {
        return totalMonthlyFee;
    }

    public double getAmountForOptionalProds() {
        return amountForOptionalProds;
    }

    public ServicePackage getServicePackage() {
        return servicePackage;
    }

    @SuppressWarnings("FieldCanBeLocal")
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
