package controllers;

import services.BuyService;
import services.UserService;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.BadRequestException;

@WebServlet(name = "BuyOffer", value = "/BuyOffer")
public class BuyServlet extends HttpServlet {

    @EJB(beanName = "BuyService")
    BuyService buyService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String idOffer = request.getParameter("id_offer");
        if (idOffer == null || idOffer.isEmpty()) throw new BadRequestException();
        buyService.setOffer(Integer.parseInt(idOffer));
        //TODO incoming....
    }
}
