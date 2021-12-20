package controllers;

import entities.Customer;
import org.thymeleaf.context.WebContext;
import services.BuyService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.BadRequestException;
import java.io.IOException;

@WebServlet(name = "Payment", urlPatterns = "/Payment")
public class Payment extends HttpServletThymeleaf {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        boolean flag;
        HttpSession session = request.getSession();
        BuyService buyService = (BuyService) session.getAttribute("BuyService");
        if (buyService == null) {
            response.sendRedirect(getServletContext().getContextPath());
            return;
        }
        try {
            if (buyService.getOrder().getCustomer() == null)
                buyService.getOrder().setCustomer((Customer) request.getSession().getAttribute("user"));
            flag = buyService.executePayment();
            session.removeAttribute("BuyService");
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
