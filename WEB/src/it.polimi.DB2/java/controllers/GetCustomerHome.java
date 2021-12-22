package controllers;

import entities.*;
import exception.OrderNotFound;
import org.thymeleaf.context.WebContext;
import services.OrderService;
import services.PackageService;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "GetCustomerHome", value = "")
public class GetCustomerHome extends HttpServletThymeleaf {

    @EJB(beanName = "PackageService")
    PackageService packageService;
    @EJB(beanName = "OrderService")
    OrderService orderService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /*TODO controllare i valori null di rejectedPayments*/
        List<ServicePackage> servicePackages = packageService.getAvailableServicePackages();
        List<Order> rejectedPayments = null;
        Customer customer = (Customer) request.getSession().getAttribute("user");
        if (customer != null) {
            try {
                rejectedPayments = orderService.getRejectedOrdersByCustomer(customer.getId());
            } catch (OrderNotFound e) {
                e.printStackTrace();
            }
        }

        String path = "CustomerHomePage";
        final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
        ctx.setVariable("user", customer);
        ctx.setVariable("servicePackages", servicePackages);
        ctx.setVariable("rejPayments", rejectedPayments);
        templateEngine.process(path, ctx, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }
}
