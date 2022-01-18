package services;

import entities.Customer;
import entities.Employee;
import exception.UserExeption;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.List;

@Stateless
public class UserService {
    @PersistenceContext(unitName = "db2_project")
    private EntityManager em;

    public UserService() {
    }

    public Customer checkCredentialsCustomer(String email, String password) throws UserExeption, PersistenceException {
        List<Customer> userList;

        userList = em.createNamedQuery("Customer.checkCredentials", Customer.class)
                .setParameter(1, email)
                .setParameter(2, password)
                .getResultList();

        if (userList.size() == 1) {
            return userList.get(0);
        }
        throw new UserExeption("User not found");
    }

    public Employee checkCredentialsEmployee(String email, String password) throws UserExeption, PersistenceException {
        List<Employee> userList;

        userList = em.createNamedQuery("Employee.checkCredentials", Employee.class)
                .setParameter(1, email)
                .setParameter(2, password)
                .getResultList();

        if (userList.size() == 1) {
            return userList.get(0);
        }
        throw new UserExeption("User not found");
    }

    public void registerNewUser(String username, String email, String psw) throws PersistenceException, UserExeption {
        Customer newUser = new Customer();
        newUser.setEmail(email);
        newUser.setPassword(psw);
        newUser.setUsername(username);
        try {
            em.persist(newUser);
            em.flush();
        } catch (PersistenceException e) {
            if (e.getCause().getMessage().contains("java.sql.SQLIntegrityConstraintViolationException")) {
                throw new UserExeption("User already exists");
            }
            throw new PersistenceException();
        }
    }
}
