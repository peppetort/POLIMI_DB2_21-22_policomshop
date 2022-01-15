package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "customer", schema = "db2_project")
@NamedQuery(name = "Customer.checkCredentials", query = "SELECT r FROM Customer r  WHERE r.email = ?1 and r.password = ?2")
public class Customer implements User, Serializable, Comparable {
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
    @Column(name = "num_failed_payments")
    private int numFailedPayments;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH})
    private List<Order> orders;

    public Long getId() {
        return id;
    }

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

    public int getNumFailedPayments() {
        return numFailedPayments;
    }

    public void addOneFailedPayment() {
        this.numFailedPayments = this.numFailedPayments + 1;
    }

    public void removeOneFailedPayment() {
        this.numFailedPayments = this.numFailedPayments - 1;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof Customer)) {
            return -1;
        }
        Customer o1 = (Customer) o;
        return (int) (this.id - o1.id);
    }
}