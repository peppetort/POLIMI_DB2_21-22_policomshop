package services;

import entities.User;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class UserService {
    @PersistenceContext(unitName = "db2_project")
    EntityManager em;

    public UserService() {
    }

    public User registerNewUser(String usrname, String email, String psw){
        User newUsr = new User();
        newUsr.setEmail(email);
        newUsr.setPassword(psw);
        newUsr.setUsername(usrname);
        try{
            em.persist(newUsr);
            em.flush();
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
        return newUsr;
    }
}
