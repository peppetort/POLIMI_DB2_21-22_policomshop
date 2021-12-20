package controllers;

import entities.Customer;
import org.thymeleaf.context.WebContext;
import services.BuyService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ReviewOrder", urlPatterns = "/ReviewOrder")
public class ReviewOrder extends HttpServletThymeleaf {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BuyService buyService = (BuyService) request.getSession().getAttribute("BuyService");
        if (buyService == null) {
            response.sendRedirect(getServletContext().getContextPath());
            return;
        }
        //TODO ma quando viene tolto??
        Customer customer = (Customer) request.getSession().getAttribute("user");
        if (customer == null) {
            request.getSession().setAttribute("paymentInProgress", true);
        }
        final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
        ctx.setVariable("user", request.getSession().getAttribute("user"));
        ctx.setVariable("order", buyService.getOrder());
        templateEngine.process("ReviewOrderPage", ctx, response.getWriter());
    }
}
