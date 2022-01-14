package entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "activation_schedule", schema = "db2_project")
public class ActivationSchedule implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_package", nullable = false)
    private ServicePackage servicePackage;
    @ManyToOne
    @JoinColumn(name = "id_offer", nullable = false)
    private Offer offer;
    @ManyToOne
    @JoinColumn(name = "id_order", nullable = false)
    private Order order;

    public ActivationSchedule() {
    }

    public ServicePackage getServicePackage() {
        return servicePackage;
    }

    public void setServicePackage(ServicePackage servicePackage) {
        this.servicePackage = servicePackage;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
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
