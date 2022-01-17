package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "offer", schema = "db2_project")
public class Offer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "validity_period")
    private int validityPeriod;
    @Column(name = "monthly_fee")
    private double monthlyFee;
    @Column(name = "is_active")
    private boolean active;
    @ManyToOne
    @JoinColumn(name = "id_package", nullable = false, updatable = false)
    private ServicePackage servicePackage;

    public Offer() {
    }

    public Offer(int validityPeriod, double monthlyFee, boolean active, ServicePackage servicePackage) {
        this.validityPeriod = validityPeriod;
        this.monthlyFee = monthlyFee;
        this.active = active;
        this.servicePackage = servicePackage;
    }

    public int getId() {
        return id;
    }

    public int getValidityPeriod() {
        return validityPeriod;
    }

    public double getMonthlyFee() {
        return monthlyFee;
    }

    public boolean isActive() {
        return active;
    }


    public ServicePackage getServicePackage(){
        return servicePackage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Offer offer = (Offer) o;
        return id == offer.id && validityPeriod == offer.validityPeriod && Double.compare(offer.monthlyFee, monthlyFee) == 0 && Objects.equals(servicePackage, offer.servicePackage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, validityPeriod, monthlyFee, servicePackage);
    }
}
