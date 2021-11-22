package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "audit_customer", schema = "db2_project")
public class AuditCustomer implements Serializable {
    private double amount;
    private Timestamp lastRejection;

    //Same Id of the customer and each customer has one and only one entry in AuditCustomer
    @Id
    @OneToOne
    @JoinColumn(name = "id_user")
    private Customer customer;

    @Basic
    @Column(name = "amount")
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Basic
    @Column(name = "last_rejection")
    public Timestamp getLastRejection() {
        return lastRejection;
    }

    public void setLastRejection(Timestamp lastRejection) {
        this.lastRejection = lastRejection;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuditCustomer that = (AuditCustomer) o;

        if (Double.compare(that.amount, amount) != 0) return false;
        if (lastRejection != null ? !lastRejection.equals(that.lastRejection) : that.lastRejection != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(amount);
        result = (int) (temp ^ (temp >>> 32));
        result = 31 * result + (lastRejection != null ? lastRejection.hashCode() : 0);
        return result;
    }
}
