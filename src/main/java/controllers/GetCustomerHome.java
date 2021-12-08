package controllers;

import entities.Customer;
import entities.Offer;
import entities.Order;
import entities.User;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import services.OrderService;
import services.PackageService;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "GetCustomerHome", value = "")
public class GetCustomerHome extends HttpServletThymeleaf {

    @EJB(beanName = "PackageService")
    PackageService packageService;
    @EJB(beanName = "OrderService")
    OrderService orderService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Offer> offerList = packageService.getAvailableOffer();
        List<Order> rejectedPayments = null;
        Customer customer = (Customer) request.getSession().getAttribute("user");
        if (customer != null) {
            rejectedPayments = orderService.getRejectedOrdersByCustomer(customer.getId());
        }

        String path = "CustomerHomePage";
        final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
        ctx.setVariable("offerList", offerList);
        ctx.setVariable("rejPayments", rejectedPayments);
        templateEngine.process(path, ctx, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }
}
