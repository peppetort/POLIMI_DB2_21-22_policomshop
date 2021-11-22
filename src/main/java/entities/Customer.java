package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "customer", schema = "db2_project")
public class Customer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String email;
    private String username;
    private String password;
    private int nPaymentAttemps;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer", cascade = {CascadeType.PERSIST, CascadeType.REMOVE,
            CascadeType.REFRESH})
    private List<Order> orders;
    @OneToOne(mappedBy = "customer", orphanRemoval = true)
    private AuditCustomer auditCustomer;

    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "n_payment_attemps")
    public int getnPaymentAttemps() {
        return nPaymentAttemps;
    }

    public void setnPaymentAttemps(int nPaymentAttemps) {
        this.nPaymentAttemps = nPaymentAttemps;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        if (id != customer.id) return false;
        if (nPaymentAttemps != customer.nPaymentAttemps) return false;
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
        result = 31 * result + nPaymentAttemps;
        return result;
    }
}
