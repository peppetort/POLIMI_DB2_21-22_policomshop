package services;

import entities.Customer;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.List;

@Stateless
public class UserService {
    @PersistenceContext(unitName = "db2_project")
    EntityManager em;

    public UserService() {
    }

    public Customer checkCredentialsCustomer(String email, String password) {
        List<Customer> userList;

        userList = em.createNamedQuery("Customer.checkCredentials", Customer.class)
                .setParameter(1, email)
                .setParameter(2, password)
                .getResultList();

        if(userList.size() == 1){
            return userList.get(0);
        }
        return null;
    }
    
    public Customer registerNewUser(String username, String email, String psw){
        Customer newUser = new Customer();
        newUser.setEmail(email);
        newUser.setPassword(psw);
        newUser.setUsername(username);
        try{
            em.persist(newUser);
            em.flush();
        }catch (PersistenceException e){
            if(e.getCause().getMessage().contains("java.sql.SQLIntegrityConstraintViolationException")) return null;
            throw e;
        }
        return newUser;
    }
}
