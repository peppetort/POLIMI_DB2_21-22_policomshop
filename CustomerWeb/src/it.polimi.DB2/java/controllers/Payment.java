package controllers;

import entities.Customer;
import exception.OrderException;
import org.thymeleaf.context.WebContext;
import services.BuyService;
import services.OrderService;

import javax.ejb.EJB;
import javax.persistence.PersistenceException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "Payment", urlPatterns = "/Payment")
public class Payment extends HttpServletThymeleaf {

    @EJB(name = "OrderService")
    OrderService orderService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String orderIdParam = request.getParameter("id");

            HttpSession session = request.getSession();
            BuyService buyService = (BuyService) session.getAttribute("BuyService");
            Customer customer = (Customer) session.getAttribute("user");

            boolean result;

            if (buyService == null) {
                if (orderIdParam != null) {
                    int orderId = Integer.parseInt(orderIdParam);
                    result = orderService.updateCustomerOrderPayment(orderId, customer.getId());
                } else {
                    response.sendRedirect(getServletContext().getContextPath());
                    return;
                }
            } else {
                result = buyService.executePayment(customer);
                session.removeAttribute("BuyService");
            }

            String path = "PaymentPage";
            final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
            ctx.setVariable("user", request.getSession().getAttribute("user"));
            ctx.setVariable("result", result);
            ctx.setVariable("nFailedPayments", ((Customer) request.getSession().getAttribute("user")).getNumFailedPayments());
            templateEngine.process(path, ctx, response.getWriter());
        } catch (NumberFormatException | OrderException e) {
            response.sendRedirect(request.getContextPath());
        } catch (PersistenceException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }
}
