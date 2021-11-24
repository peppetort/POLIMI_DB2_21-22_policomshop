package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "service", schema = "db2_project")
@Inheritance(strategy=InheritanceType.JOINED)
@DiscriminatorColumn(name="type", discriminatorType=DiscriminatorType.INTEGER)
public abstract class Service implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "type")
    private String type;
    @ManyToMany(mappedBy = "serviceList")
    @JoinTable(name = "service_package_to_service", joinColumns = @JoinColumn(name = "id_service"), inverseJoinColumns = @JoinColumn(name = "id_package"))
    private List<ServicePackage> servicePackagesList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Service service = (Service) o;
        if (id != service.id) return false;
        return Objects.equals(type, service.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, servicePackagesList);
    }
}
