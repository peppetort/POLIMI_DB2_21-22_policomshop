package controllers;

import services.OptionalProdService;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "NewOptionalProd", urlPatterns = "/SaveOptionalProd")
public class NewOptionalProd extends HttpServletThymeleaf {

    @EJB
    private OptionalProdService optionalProdService;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        String monthlyFeeString = request.getParameter("monthly_fee");
        if (name == null || monthlyFeeString == null || name.isEmpty() || monthlyFeeString.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Double monthlyFee = Double.parseDouble(monthlyFeeString);
        optionalProdService.saveNewProd(name, monthlyFee);
        response.sendRedirect(getServletContext().getContextPath());
    }
}
