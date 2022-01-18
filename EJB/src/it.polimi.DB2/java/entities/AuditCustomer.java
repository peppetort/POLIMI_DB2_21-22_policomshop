package entities;

import javax.persistence.*;
import java.util.Date;

//Derived Identifiers Pag. 434
@Entity
@Table(name = "audit_customer", schema = "db2_project")
public class AuditCustomer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "amount")
    private double amount;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date")
    private Date date;
    @ManyToOne
    @JoinColumn(name = "id_customer", updatable = false, nullable = false)
    private Customer customer;

    public AuditCustomer() {
    }

    public AuditCustomer(Customer customer, double amount) {
        this.customer = customer;
        this.amount = amount;
        this.date = new Date();
    }

    public double getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }

    public Customer getCustomer() {
        return customer;
    }
}
