package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "customer", schema = "db2_project")
@NamedQuery(name = "Customer.checkCredentials", query = "SELECT r FROM Customer r  WHERE r.email = ?1 and r.password = ?2")
public class Customer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "email")
    private String email;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "n_payment_attempts")
    private int nPaymentAttempts;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer", cascade = {CascadeType.PERSIST, CascadeType.REMOVE,
            CascadeType.REFRESH})
    private List<Order> orders;
    @OneToOne(mappedBy = "customer", orphanRemoval = true)
    private AuditCustomer auditCustomer;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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

    public int getnNPaymentAttempts() {
        return nPaymentAttempts;
    }

    public void setnNPaymentAttempts(int nPaymentAttemps) {
        this.nPaymentAttempts = nPaymentAttemps;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        if (id != customer.id) return false;
        if (nPaymentAttempts != customer.nPaymentAttempts) return false;
        if (email != null ? !email.equals(customer.email) : customer.email != null) return false;
        if (username != null ? !username.equals(customer.username) : customer.username != null) return false;
        if (password != null ? !password.equals(customer.password) : customer.password != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + nPaymentAttempts;
        return result;
    }
}
