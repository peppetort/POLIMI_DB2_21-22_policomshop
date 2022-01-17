package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "activation_schedule", schema = "db2_project")
public class ActivationSchedule implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Temporal(TemporalType.DATE)
    @Column(name = "activation_date", nullable = false)
    private Date activationDate;
    @Temporal(TemporalType.DATE)
    @Column(name = "deactivation_date", nullable = false)
    private Date deactivationDate;
    @ManyToOne
    @JoinColumn(name = "id_order", nullable = false)
    private Order order;

    public ActivationSchedule() {
    }

    public Date getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(Date activationDate) {
        this.activationDate = activationDate;
    }

    public Date getDeactivationDate() {
        return deactivationDate;
    }

    public void setDeactivationDate(Date deactivationDate) {
        this.deactivationDate = deactivationDate;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
