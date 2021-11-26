package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

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
    private byte isActive;
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

    public byte getIsActive() {
        return isActive;
    }

    public void setIsActive(byte isActive) {
        this.isActive = isActive;
    }

    public ServicePackage getServicePackage(){
        return servicePackage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Offer offer = (Offer) o;

        if (id != offer.id) return false;
        if (validityPeriod != offer.validityPeriod) return false;
        if (Double.compare(offer.monthlyFee, monthlyFee) != 0) return false;
        return isActive == offer.isActive;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + validityPeriod;
        temp = Double.doubleToLongBits(monthlyFee);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (int) isActive;
        return result;
    }
}
