package services;

import entities.Customer;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class UserService {
    @PersistenceContext(unitName = "db2_project")
    EntityManager em;

    public UserService() {
    }

    public Customer registerNewUser(String usrname, String email, String psw){
        Customer newUsr = new Customer();
        newUsr.setEmail(email);
        newUsr.setPassword(psw);
        newUsr.setUsername(usrname);
        try{
            em.persist(newUsr);
            em.flush();
        }catch (Exception e){
            //TODO da sistemare non si può lasciare cosi
            System.out.println(e.getMessage());
            return null;
        }
        return newUsr;
    }
}
