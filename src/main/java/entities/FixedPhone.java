package entities;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "fixed_phone", schema = "db2_project")
@DiscriminatorValue("2")
public class FixedPhone extends Service {
    @Column(name = "n_gigabytes")
    private int nMinutes;
    @Column(name = "fee_gigabytes")
    private double fee;

    public int getnMinutes() {
        return nMinutes;
    }

    public void setnMinutes(int nMinutes) {
        this.nMinutes = nMinutes;
    }

    public double getFee() {
        return fee;
    }

    @Override
    public String getHTMLFields() {
        return "<li>" + "Number of minutes: " + nMinutes + "</li>" + "<li>" + "Fee" + fee + "</li>";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FixedPhone that = (FixedPhone) o;

        if (nMinutes != that.nMinutes) return false;
        return Double.compare(that.fee, fee) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = nMinutes;
        temp = Double.doubleToLongBits(fee);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
