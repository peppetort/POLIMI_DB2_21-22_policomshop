package controllers;

import entities.Customer;
import entities.Employee;
import exception.UserNotFound;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import services.UserService;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "SignIn", value = "/SignIn")
public class SignIn extends HttpServletThymeleaf {
    @EJB(beanName = "UserService")
    UserService userService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        renderPage(request, response, (request.getSession().getAttribute("user") != null ? "You are already logged in" : null), request.getParameter("paymentInProgress") != null);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email"), //email is the id of the field in the form
                pwd = request.getParameter("password"), //same as above
                role = request.getParameter("employee_role"),
                paymentInProgress = request.getParameter("paymentInProgress");
        if (email == null || email.isEmpty() ||
                pwd == null || pwd.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            String servlet;
            if (role == null) {
                Customer customer = userService.checkCredentialsCustomer(email, pwd);
                request.getSession().setAttribute("user", customer);
                if (paymentInProgress != null) {
                    servlet = "Payment";
                } else {
                    servlet = request.getContextPath();
                }
            } else {
                Employee employee = userService.checkCredentialsEmployee(email, pwd);
                servlet = "EmployeeHomePage";
                request.getSession().setAttribute("user", employee);
            }
            response.sendRedirect(servlet);
        } catch (UserNotFound e) {
            renderPage(request, response, "Invalid Credentials", paymentInProgress != null);
        }
    }

    private void renderPage(HttpServletRequest request, HttpServletResponse response, String errorMsg, boolean paymentInProgress) throws IOException {
        final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
        ctx.setVariable("errorMsg", errorMsg);
        ctx.setVariable("paymentInProgress", paymentInProgress);
        templateEngine.process("SignInPage", ctx, response.getWriter());
    }
}
