package controllers;

import exception.OptionalProductException;
import org.thymeleaf.util.StringUtils;
import services.OptionalProductService;

import javax.ejb.EJB;
import javax.persistence.PersistenceException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.BadRequestException;
import java.io.IOException;

@WebServlet(name = "CreateOptionalProduct", urlPatterns = "/CreateOptionalProduct")
public class CreateOptionalProduct extends HttpServlet {

    @EJB
    private OptionalProductService optionalProdService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("opName");
        String monthlyFeeString = request.getParameter("opMonthFee");

        if (StringUtils.isEmptyOrWhitespace(name) || StringUtils.isEmptyOrWhitespace(monthlyFeeString)) {
            response.sendRedirect(getServletContext().getContextPath());
            return;
        }

        try {
            Double monthlyFee = Double.parseDouble(monthlyFeeString);
            optionalProdService.createNewOptionalProduct(name, monthlyFee);

            response.sendRedirect(getServletContext().getContextPath());
        }catch (NumberFormatException | OptionalProductException e){
            //TODO: settare errori nella sessione
            response.sendRedirect(getServletContext().getContextPath());
        }catch (PersistenceException e){
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
