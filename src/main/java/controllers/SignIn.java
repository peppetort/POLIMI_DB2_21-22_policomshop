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
public class SignIn extends HttpServlet {

    private final TemplateEngine templateEngine = new TemplateEngine();

    @EJB(beanName = "UserService")
    UserService userService;

    @Override
    public void init() {
        ServletContext servletContext = getServletContext();
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setSuffix(".html");
        templateEngine.setTemplateResolver(templateResolver);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email"), //email is the id of the field in the form
                pwd = request.getParameter("password"), //same as above
                role = request.getParameter("employee_role");
        if (email == null || email.isEmpty() ||
                pwd == null || pwd.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            String servlet;
            if (role == null) {
                Customer customer = userService.checkCredentialsCustomer(email, pwd);
                servlet = "UserHomePage";
                request.getSession().setAttribute("user", customer);
            } else {
                Employee employee = userService.checkCredentialsEmployee(email, pwd);
                servlet = "EmployeeHomePage";
                request.getSession().setAttribute("user", employee);
            }
            response.sendRedirect(servlet);
        }catch (UserNotFound e) {
            final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
            ctx.setVariable("errorMsg", "Invalid Credentials");
            String path = "/WEB-INF/templates/SignInPage.html";
            templateEngine.process(path, ctx, response.getWriter());
        }
    }
}
