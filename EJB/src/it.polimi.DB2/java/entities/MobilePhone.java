package entities;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "mobile_phone", schema = "db2_project")
@DiscriminatorValue("4")
public class MobilePhone extends Service {
    @Column(name = "n_minutes")
    private int nMinutes;
    @Column(name = "fee_minutes")
    private double feeMinutes;
    @Column(name = "n_sms")
    private int nSms;
    @Column(name = "fee_sms")
    private double feeSms;

    public int getnMinutes() {
        return nMinutes;
    }

    public void setnMinutes(int nMinutes) {
        this.nMinutes = nMinutes;
    }

    public double getFeeMinutes() {
        return feeMinutes;
    }

    public void setFeeMinutes(double feeMinutes) {
        this.feeMinutes = feeMinutes;
    }

    public int getnSms() {
        return nSms;
    }

    public void setnSms(int nSms) {
        this.nSms = nSms;
    }

    public double getFeeSms() {
        return feeSms;
    }

    public void setFeeSms(double feeSms) {
        this.feeSms = feeSms;
    }

    @Override
    public String getHTMLFields() {
        return "<li>" + "Number of minutes: " + nMinutes + "</li>" + "<li>" + "Fee" + feeMinutes + "</li>"
                + "<li>" + "Number of sms: " + nSms + "</li>" + "<li>" + "Fee" + feeSms + "</li>";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MobilePhone that = (MobilePhone) o;

        if (nMinutes != that.nMinutes) return false;
        if (Double.compare(that.feeMinutes, feeMinutes) != 0) return false;
        if (nSms != that.nSms) return false;
        return Double.compare(that.feeSms, feeSms) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = nMinutes;
        temp = Double.doubleToLongBits(feeMinutes);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + nSms;
        temp = Double.doubleToLongBits(feeSms);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
