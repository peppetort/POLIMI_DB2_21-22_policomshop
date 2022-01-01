package entities;

import javax.persistence.*;

@Entity
@Table(name = "stat_service_package", schema = "db2_project")
public class PackageStatistics {

    @Id
    @Column(name = "id_package")
    private Long id;
    @Column(name = "validity_period")
    private int validityPeriod;
    @Column(name = "num_purchases")
    private int numPurchases;
    @Column(name = "amount_with_optional")
    private double amountWithOptional;
    @Column(name = "amount_without_optional")
    private double amountWithoutOptional;
    @MapsId
    @OneToOne
    @JoinColumn(name = "id_package")
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
}
