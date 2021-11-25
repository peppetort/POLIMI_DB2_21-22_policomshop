package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "customer", schema = "db2_project")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.INTEGER, name = "audit", columnDefinition = "TINYINT(1)")
@DiscriminatorValue("0")
@NamedQuery(name = "Customer.checkCredentials", query = "SELECT r FROM Customer r  WHERE r.email = ?1 and r.password = ?2")
public class Customer extends User implements Serializable {
    @Column(name = "n_payment_attempts")
    private int nPaymentAttempts;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH})
    private List<Order> orders;

    public int getnNPaymentAttempts() {
        return nPaymentAttempts;
    }

    public void setnNPaymentAttempts(int nPaymentAttemps) {
        this.nPaymentAttempts = nPaymentAttemps;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}