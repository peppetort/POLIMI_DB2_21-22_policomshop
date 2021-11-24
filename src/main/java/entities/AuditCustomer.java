package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "audit_customer", schema = "db2_project")
public class AuditCustomer implements Serializable {
    @Column(name = "amount")
    private double amount;
    @Column(name = "last_rejection")
    private Timestamp lastRejection;

    //Same Id of the customer and each customer has one and only one entry in AuditCustomer
    @Id
    @OneToOne
    @JoinColumn(name = "id_user")
    private Customer customer;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Timestamp getLastRejection() {
        return lastRejection;
    }

    public void setLastRejection(Timestamp lastRejection) {
        this.lastRejection = lastRejection;
    }

}
