package entities;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "mobile_internet", schema = "db2_project")
@DiscriminatorValue("3")
public class MobileInternet extends Service {
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
    public String getHTMLFields() {
        return "<li>" + "Number of GigaBytes: " + nGigabytes + "</li>" + "<li>" + "Fee" + feeGigabytes + "</li>";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MobileInternet that = (MobileInternet) o;

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
