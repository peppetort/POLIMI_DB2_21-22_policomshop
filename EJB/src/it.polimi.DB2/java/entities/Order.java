package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

@Entity
@Table(name = "order", schema = "db2_project")
@NamedQuery(name = "Order.rejectedOrders", query = "SELECT r FROM Order r  WHERE r.customer.id = ?1 and r.status = ?2")
@NamedQuery(name = "Order.rejectedOrdersByID", query = "SELECT r FROM Order r  WHERE r.id = ?1 and r.customer.id = ?2 and r.status = ?3")
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Temporal(TemporalType.TIMESTAMP)
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
    @JoinColumn(name = "id_user", nullable = false)
    private Customer customer;
    @ManyToOne
    @JoinColumn(name = "id_offer", nullable = false)
    private Offer offer;
    @ManyToMany
    @JoinTable(name = "order_to_optional_product", joinColumns = @JoinColumn(name = "id_order"), inverseJoinColumns = @JoinColumn(name = "id_optional_product"))
    /*Ho scelto un set per evitare un inserimento doppio per lo stesso optional product*/
    private Set<OptionalProduct> optionalProductList;

    public Order() {
        optionalProductList = new HashSet<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCreationDate(Date creationDate) {
        if (this.creationDate != null) throw new IllegalCallerException();
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

    public void setStatus(State status) {
        this.status = status;
    }

    public State getStatus() {
        return status;
    }

    public void setTotalMonthlyFee(double totalMonthlyFee) {
        this.totalMonthlyFee = totalMonthlyFee;
    }

    public double getTotalMonthlyFee() {
        return totalMonthlyFee;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        if (this.customer != null) throw new IllegalArgumentException();
        this.customer = customer;
    }

    public Set<OptionalProduct> getOptionalProductList() {
        return optionalProductList;
    }

    //TODO: a che serve?
    public boolean isCorrectFilled(boolean userIsImportant) {
        Date now = new Date();
        if(status.equals(State.PAYMENT_FAILED) && startDate.before(now)) {
            Calendar c = Calendar.getInstance();
            c.setTime(now);
            c.add(Calendar.DAY_OF_MONTH, 1);
            startDate= c.getTime();
        }
        boolean flag = startDate.after(now) && offer != null;
        return flag && (!userIsImportant || customer != null);
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
        CREATED(0),
        PAID(1),
        PAYMENT_FAILED(2);
        final int idDB;

        State(int idDB) {
            this.idDB = idDB;
        }

        public int getIdDB() {
            return idDB;
        }
    }

    @Converter(autoApply = true)
    public static class StateConverter implements AttributeConverter<State, Integer> {

        @Override
        public Integer convertToDatabaseColumn(State state) {
            return state.getIdDB();
        }

        @Override
        public State convertToEntityAttribute(Integer integer) {
            for (State s :
                    State.values()) {
                if (s.getIdDB() == integer) return s;
            }
            return null;
        }
    }
}
