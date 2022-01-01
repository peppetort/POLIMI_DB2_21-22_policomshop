package controllers;

import services.OfferService;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "DisableOffer", urlPatterns = "/Offer/disable")
public class DisableOffer extends HttpServlet {

    @EJB
    private OfferService offerService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idOffer = req.getParameter("id");
        if (idOffer == null || idOffer.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int id = Integer.parseInt(idOffer);
        offerService.disable(id);
        resp.sendRedirect(getServletContext().getContextPath());
    }
}
