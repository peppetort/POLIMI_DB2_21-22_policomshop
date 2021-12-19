package controllers;

import entities.Customer;
import org.thymeleaf.context.WebContext;
import services.BuyService;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.BadRequestException;
import java.io.IOException;

@WebServlet(name = "Payment", urlPatterns = "/Payment")
public class Payment extends HttpServletThymeleaf {
    @Inject
    BuyService buyService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        boolean flag;
        try {
            if (buyService.getOrder().getCustomer() == null)
                buyService.getOrder().setCustomer((Customer) request.getSession().getAttribute("user"));
            flag = buyService.executePayment();
        } catch (BadRequestException e) {
            e.getStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String path = "PaymentPage";
        final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
        ctx.setVariable("user", request.getSession().getAttribute("user"));
        ctx.setVariable("result", flag);
        ctx.setVariable("nFailedPayments", ((Customer) request.getSession().getAttribute("user")).getNumFailedPayments());
        templateEngine.process(path, ctx, response.getWriter());
    }
}
