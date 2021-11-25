package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "order", schema = "db2_project")
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Temporal(TemporalType.DATE)
    @Column(name = "creation_date")
    private Date creationDate;
    @Temporal(TemporalType.DATE)
    @Column(name = "start_date")
    private Date startDate;
    @Column(name = "total_monthly_fee")
    private double totalMonthlyFee;
    @Column(name = "status")
    private byte status;
    @ManyToOne
    @JoinColumn(name = "id_user")
    private Customer customer;
    @ManyToOne
    @JoinColumn(name = "id_offer")
    private Offer offer;
    @ManyToMany
    @JoinTable(name = "order_to_optional_product", joinColumns = @JoinColumn(name = "id_order"), inverseJoinColumns = @JoinColumn(name = "id_optional_product"))
    private List<OptionalProduct> optionalProductList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    

    public double getTotalMonthlyFee() {
        return totalMonthlyFee;
    }

    public void setTotalMonthlyFee(double totalMonthlyFee) {
        this.totalMonthlyFee = totalMonthlyFee;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (id != order.id) return false;
        if (Double.compare(order.totalMonthlyFee, totalMonthlyFee) != 0) return false;
        if (status != order.status) return false;
        if (!Objects.equals(creationDate, order.creationDate)) return false;
        return Objects.equals(startDate, order.startDate);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        temp = Double.doubleToLongBits(totalMonthlyFee);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (int) status;
        return result;
    }
}
