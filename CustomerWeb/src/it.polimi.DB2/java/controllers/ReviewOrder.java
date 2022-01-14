package controllers;

import entities.*;
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
        Customer customer = (Customer) request.getSession().getAttribute("user");

        if (buyService == null || buyService.getOrder() == null || buyService.getOrder().getOffer() == null || buyService.getOrder().getActivationDate() == null) {
            response.sendRedirect(request.getServletContext().getContextPath());
            return;
        }

        if (customer == null) {
            request.getSession().setAttribute("paymentInProgress", true);
        }

        final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
        ctx.setVariable("user", request.getSession().getAttribute("user"));
        ctx.setVariable("buyService", buyService);
        templateEngine.process("ReviewOrderPage", ctx, response.getWriter());
    }
}
