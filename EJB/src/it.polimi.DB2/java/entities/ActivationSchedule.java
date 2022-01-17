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
}
