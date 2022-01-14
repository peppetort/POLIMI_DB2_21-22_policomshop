package controllers;

import entities.Customer;
import entities.Order;
import entities.ServicePackage;
import exception.OrderNotFound;
import org.thymeleaf.context.WebContext;
import services.OrderService;
import services.PackageService;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.BadRequestException;
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
                try {
                    rejectedPayments = orderService.getRejectedOrdersByCustomer(customer.getId());
                } catch (OrderNotFound e) {
                    e.printStackTrace();
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
            }

            final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
            ctx.setVariable("user", customer);
            ctx.setVariable("servicePackages", servicePackages);
            ctx.setVariable("rejPayments", rejectedPayments);
            templateEngine.process("HomePage", ctx, response.getWriter());
        }catch (BadRequestException e){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }
}
