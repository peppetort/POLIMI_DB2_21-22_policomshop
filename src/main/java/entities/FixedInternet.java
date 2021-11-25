package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "fixed_internet", schema = "db2_project")
@DiscriminatorValue("1")
public class FixedInternet extends Service {
    @Column(name = "n_gigabytes")
    private int nGigabytes;
    @Column(name = "fee_gigabytes")
    private double feeGigabytes;

    public int getnGigabytes() {
        return nGigabytes;
    }

    public void setnGigabytes(int nGigabytes) {
        this.nGigabytes = nGigabytes;
    }

    public double getFeeGigabytes() {
        return feeGigabytes;
    }

    public void setFeeGigabytes(double feeGigabytes) {
        this.feeGigabytes = feeGigabytes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FixedInternet that = (FixedInternet) o;

        if (nGigabytes != that.nGigabytes) return false;
        if (Double.compare(that.feeGigabytes, feeGigabytes) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = nGigabytes;
        temp = Double.doubleToLongBits(feeGigabytes);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
