package controllers;

import entities.Customer;
import org.thymeleaf.context.WebContext;
import services.BuyService;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "Repay", value = "/Repay")
public class RepayRejectedOrder extends HttpServletThymeleaf{
    @EJB(name = "BuyService")
    BuyService buyService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        if(id == null || id.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Customer user = (Customer) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        boolean result = buyService.retryPayment(Integer.parseInt(id), user);
        String path = "PaymentPage";
        final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
        ctx.setVariable("user", request.getSession().getAttribute("user"));
        ctx.setVariable("result", result);
        templateEngine.process(path, ctx, response.getWriter());
    }
}
