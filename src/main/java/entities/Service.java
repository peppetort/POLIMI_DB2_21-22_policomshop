package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "service", schema = "db2_project")
public class Service implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String type;
    @ManyToMany(mappedBy = "serviceList")
    @JoinTable(name = "service_package_to_service", joinColumns = @JoinColumn(name = "id_service"), inverseJoinColumns = @JoinColumn(name = "id_package"))
    private List<ServicePackage> servicePackagesList;
    //TODO: rivedere. non sono sicuro sia il modo corretto
    @OneToOne(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
    private FixedInternet fixedInternet;
    @OneToOne(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
    private FixedPhone fixedPhone;
    @OneToOne(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
    private MobileInternet mobileInternet;
    @OneToOne(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
    private MobilePhone mobilePhone;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "type")
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
        if (type != null ? !type.equals(service.type) : service.type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
