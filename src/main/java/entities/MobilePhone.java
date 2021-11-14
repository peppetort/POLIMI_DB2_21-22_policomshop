package entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "mobile_phone", schema = "db2_project")
public class MobilePhone implements Serializable {
    private int nMinutes;
    private double feeMinutes;
    private int nSms;
    private double feeSms;
    @Id
    @OneToOne
    @JoinColumn(name = "id_service")
    private Service service;

    @Basic
    @Column(name = "n_minutes")
    public int getnMinutes() {
        return nMinutes;
    }

    public void setnMinutes(int nMinutes) {
        this.nMinutes = nMinutes;
    }

    @Basic
    @Column(name = "fee_minutes")
    public double getFeeMinutes() {
        return feeMinutes;
    }

    public void setFeeMinutes(double feeMinutes) {
        this.feeMinutes = feeMinutes;
    }

    @Basic
    @Column(name = "n_sms")
    public int getnSms() {
        return nSms;
    }

    public void setnSms(int nSms) {
        this.nSms = nSms;
    }

    @Basic
    @Column(name = "fee_sms")
    public double getFeeSms() {
        return feeSms;
    }

    public void setFeeSms(double feeSms) {
        this.feeSms = feeSms;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MobilePhone that = (MobilePhone) o;

        if (nMinutes != that.nMinutes) return false;
        if (Double.compare(that.feeMinutes, feeMinutes) != 0) return false;
        if (nSms != that.nSms) return false;
        if (Double.compare(that.feeSms, feeSms) != 0) return false;

        return true;
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
