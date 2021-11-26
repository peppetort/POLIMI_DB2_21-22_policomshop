package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "service_package", schema = "db2_project")
public class ServicePackage implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "servicePackage", cascade = {CascadeType.PERSIST, CascadeType.REMOVE,
            CascadeType.REFRESH})
    private List<Offer> offers;
    @ManyToMany
    @JoinTable(name = "service_package_to_optional_product", joinColumns = @JoinColumn(name = "id_service_package"), inverseJoinColumns = @JoinColumn(name = "id_optional_product"))
    private List<OptionalProduct> optionalProductList;
    @ManyToMany
    @JoinTable(name = "service_package_to_service", joinColumns = @JoinColumn(name = "id_package"), inverseJoinColumns = @JoinColumn(name = "id_service"))
    private List<Service> serviceList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<OptionalProduct> getOptionalProductList(){
        return optionalProductList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServicePackage that = (ServicePackage) o;

        if (id != that.id) return false;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
