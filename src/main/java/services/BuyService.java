package services;

import entities.Offer;
import entities.OptionalProduct;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.BadRequestException;
import java.util.HashSet;
import java.util.Set;

@Stateful
public class BuyService {
    @PersistenceContext(unitName = "db2_project")
    private EntityManager em;
    private Offer offer = null;
    private final Set<OptionalProduct> optionalProductSet = new HashSet<>();

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(int id) {
        Offer se = em.find(Offer.class, id);
        if (offer != null || !se.isActive()) throw new BadRequestException();
        offer = se;
    }

    public Set<OptionalProduct> getOptionalProductSet() {
        return optionalProductSet;
    }

    public void addOptionalProduct(OptionalProduct op) {
        if (offer == null) throw new BadRequestException();
        if (!offer.getServicePackage().getOptionalProductList().contains(op)) throw new BadRequestException();
        optionalProductSet.add(op);
    }

    public void removeItem(OptionalProduct op) {
        optionalProductSet.remove(op);
    }

    @Remove
    public void checkout(int paymentId) {
        //TODO payment method's implementation
    }

    @Remove
    public void cancel() {
    }
}