package entities;

import javax.persistence.*;
import java.util.Date;

/*Todo Derived Identifiers Pag. 434*/
@Entity
@Table(name = "audit_customer", schema = "db2_project")
public class AuditCustomer {
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "amount")
    private double amount;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_rejection")
    private Date lastRejection;
    @MapsId
    @OneToOne
    @JoinColumn(name = "id")
    private Customer customer;

    public AuditCustomer() {
    }

    public AuditCustomer(Customer customer, double amount, Date lastRejection) {
        this.customer = customer;
        this.amount = amount;
        this.lastRejection = lastRejection;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getLastRejection() {
        return lastRejection;
    }

    public void setLastRejection(Date lastRejection) {
        this.lastRejection = lastRejection;
    }

    public Customer getCustomer() {
        return customer;
    }
}
