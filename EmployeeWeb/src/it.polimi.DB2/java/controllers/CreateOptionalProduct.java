package controllers;

import services.OptionalProdService;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

@WebServlet(name = "CreateOptionalProduct", urlPatterns = "/CreateOptionalProduct")
public class CreateOptionalProduct extends HttpServlet {

    @EJB
    private OptionalProdService optionalProdService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("opName");
        String monthlyFeeString = request.getParameter("opMonthFee");

        if (name == null || monthlyFeeString == null || name.isEmpty() || monthlyFeeString.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            Double monthlyFee = Double.parseDouble(monthlyFeeString);
            optionalProdService.saveNewProd(name, monthlyFee);
        }catch (NumberFormatException e){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        response.sendRedirect(getServletContext().getContextPath());
    }
}
