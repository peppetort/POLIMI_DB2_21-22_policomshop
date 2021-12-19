package entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "employee", schema = "db2_project")
@NamedQuery(name = "Employee.checkCredentials", query = "SELECT r FROM Employee r  WHERE r.email = ?1 and r.password = ?2")
public class Employee implements User, Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    protected Long id;
    @Column(name = "email")
    protected String email;
    @Column(name = "username")
    protected String username;
    @Column(name = "password")
    protected String password;
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
