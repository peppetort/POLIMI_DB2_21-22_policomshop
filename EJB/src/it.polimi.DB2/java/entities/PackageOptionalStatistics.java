package entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@IdClass(PackageOptionalStatistics.PackageOptionalKey.class)
@Table(name = "stat_optional_package", schema = "db2_project")
public class PackageOptionalStatistics {

    @Id
    @Column(name = "id_package", insertable = false, updatable = false)
    private Long idPackage;
    @Id
    @Column(name = "id_optional", insertable = false, updatable = false)
    private Long idOptional;
    @Column(name = "num_purchases")
    private int numPurchases;
    @ManyToOne
    @JoinColumn(name = "id_package", nullable = false)
    private ServicePackage servicePackage;
    @ManyToOne
    @JoinColumn(name = "id_optional", nullable = false)
    private OptionalProduct optionalProduct;

    public int getNumPurchases() {
        return numPurchases;
    }

    public ServicePackage getServicePackage() {
        return servicePackage;
    }

    public OptionalProduct getOptionalProduct() {
        return optionalProduct;
    }

    public static class PackageOptionalKey implements Serializable {
        private Long idPackage;
        private Long idOptional;

        public PackageOptionalKey() {
        }

        public PackageOptionalKey(Long idPackage, Long idOptional) {
            this.idPackage = idPackage;
            this.idOptional = idOptional;
        }
    }
}
