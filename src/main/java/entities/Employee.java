package entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "employee", schema = "db2_project")
@NamedQuery(name = "Employee.checkCredentials", query = "SELECT r FROM Employee r  WHERE r.email = ?1 and r.password = ?2")
public class Employee extends User implements Serializable {
}
