package controllers;

import entities.Customer;
import entities.Order;
import entities.ServicePackage;
import org.thymeleaf.context.WebContext;
import services.OrderService;
import services.PackageService;

import javax.ejb.EJB;
import javax.persistence.PersistenceException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "GetHome", value = "")
public class GetHome extends HttpServletThymeleaf {

    @EJB(beanName = "PackageService")
    PackageService packageService;
    @EJB(beanName = "OrderService")
    OrderService orderService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            List<ServicePackage> servicePackages = packageService.getAvailableServicePackages();
            List<Order> rejectedPayments = null;
            Customer customer = (Customer) request.getSession().getAttribute("user");

            if (customer != null) {
                rejectedPayments = orderService.getRejectedOrdersByCustomer(customer.getId());
            }

            final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
            ctx.setVariable("user", customer);
            ctx.setVariable("servicePackages", servicePackages);
            ctx.setVariable("rejPayments", rejectedPayments);
            templateEngine.process("HomePage", ctx, response.getWriter());
        } catch (PersistenceException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }
}
