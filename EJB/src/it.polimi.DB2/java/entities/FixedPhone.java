package entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "service", schema = "db2_project")
@DiscriminatorValue("2")
public class FixedPhone extends Service {
}
