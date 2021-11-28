package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "service", schema = "db2_project")
@Inheritance(strategy=InheritanceType.JOINED)
@DiscriminatorColumn(name="type", discriminatorType=DiscriminatorType.INTEGER, columnDefinition = "TINYINT(1)")
public abstract class Service implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "type")
    private int type;
    @ManyToMany(mappedBy = "serviceList")
    @JoinTable(name = "service_package_to_service", joinColumns = @JoinColumn(name = "id_service"), inverseJoinColumns = @JoinColumn(name = "id_package"))
    private List<ServicePackage> servicePackagesList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public String getReadableType() {
        return Arrays.stream(ServiceType.values()).filter(x -> x.idTypeDB == type).findAny().get().readableName;//TODO + "<ul>" + getHTMLFields() + "</ul>";
    }

    protected abstract String getHTMLFields();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Service service = (Service) o;
        if (!Objects.equals(id, service.id)) return false;
        return Objects.equals(type, service.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, servicePackagesList);
    }

    public enum ServiceType {
        FIXED_INTERNET("Fixed internet", 1),
        FIXED_PHONE("Fixed phone", 2),
        MOBILE_INTERNET("Mobile internet", 3),
        MOBILE_PHONE("Mobile phone", 4);
        public final int idTypeDB;
        private final String readableName;

        ServiceType(String readableName, int idTypeDB) {
            this.readableName = readableName;
            this.idTypeDB = idTypeDB;
        }

        public String getReadableName() {
            return readableName;
        }
    }
}
