package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "audit_customer", schema = "db2_project")
@DiscriminatorValue("1")
public class AuditCustomer extends Customer {
    @Column(name = "amount")
    private double amount;
    @Column(name = "last_rejection")
    private Timestamp lastRejection;

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
