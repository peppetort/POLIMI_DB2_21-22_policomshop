package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
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
    @JoinColumn(name = "id_package")
    private ServicePackage servicePackage;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "offer", cascade = {CascadeType.PERSIST, CascadeType.REMOVE,
            CascadeType.REFRESH})
    private List<Order> orders;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValidityPeriod() {
        return validityPeriod;
    }

    public void setValidityPeriod(int validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    public double getMonthlyFee() {
        return monthlyFee;
    }

    public void setMonthlyFee(double monthlyFee) {
        this.monthlyFee = monthlyFee;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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
