package controllers;

import services.OfferService;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "NewOffer", urlPatterns = "/Offer/new")
public class NewOffer extends HttpServlet {

    @EJB
    private OfferService offerService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idPackageString = req.getParameter("id_package");
        String validityPeriodString = req.getParameter("validity_period");
        String monthlyFeeString = req.getParameter("monthly_fee");
        if (idPackageString == null || idPackageString.isEmpty()
                || validityPeriodString == null || validityPeriodString.isEmpty()
                || monthlyFeeString == null || monthlyFeeString.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int idPackage = Integer.parseInt(idPackageString);
        int validityPeriod = Integer.parseInt(validityPeriodString);
        double monthlyFee = Double.parseDouble(monthlyFeeString);
        offerService.createNewOffer(idPackage, validityPeriod, monthlyFee);
        resp.sendRedirect(getServletContext().getContextPath());
    }
}
