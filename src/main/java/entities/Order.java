package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    private State status = State.CREATED;
    @ManyToOne
    @JoinColumn(name = "id_user")
    private Customer customer;
    @ManyToOne
    @JoinColumn(name = "id_offer")
    private Offer offer;
    @ManyToMany
    @JoinTable(name = "order_to_optional_product", joinColumns = @JoinColumn(name = "id_order"), inverseJoinColumns = @JoinColumn(name = "id_optional_product"))
    private List<OptionalProduct> optionalProductList;

    public Order() {
        optionalProductList = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public String getStartDateString() {
        if (startDate == null) return null;
        return new SimpleDateFormat("yyyy-MM-dd").format(startDate);
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setTotalMonthlyFee(double totalMonthlyFee) {
        this.totalMonthlyFee = totalMonthlyFee;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public List<OptionalProduct> getOptionalProductList() {
        return optionalProductList;
    }

    public boolean isCorrectFilled() {
        return creationDate != null && startDate != null && startDate.after(new Date()) && offer != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id && Double.compare(order.totalMonthlyFee, totalMonthlyFee) == 0 && Objects.equals(creationDate, order.creationDate) && Objects.equals(startDate, order.startDate) && status == order.status && Objects.equals(customer, order.customer) && Objects.equals(offer, order.offer) && Objects.equals(optionalProductList, order.optionalProductList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creationDate, startDate, totalMonthlyFee, status, customer, offer, optionalProductList);
    }

    public enum State {
        CREATED,
        PAYED,
        PAYMENT_FAILED
    }
}
