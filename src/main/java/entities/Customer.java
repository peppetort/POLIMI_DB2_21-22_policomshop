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
public class Customer implements User, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    protected Long id;
    @Column(name = "email")
    protected String email;
    @Column(name = "username")
    protected String username;
    @Column(name = "password")
    protected String password;
    @Column(name = "n_payment_attempts")
    private int nPaymentAttempts;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH})
    private List<Order> orders;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getnPaymentAttempts() {
        return nPaymentAttempts;
    }

    public void setnPaymentAttempts(int nPaymentAttempts) {
        this.nPaymentAttempts = nPaymentAttempts;
    }

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