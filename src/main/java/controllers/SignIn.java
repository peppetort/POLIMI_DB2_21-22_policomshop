package controllers;

import entities.Customer;
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
                pwd = request.getParameter("password"); //same as above

        if (email == null || email.isEmpty() ||
                pwd == null || pwd.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Customer customer = userService.checkCredentialsCustomer(email, pwd);

        if (customer == null) {
            final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
            ctx.setVariable("errorMsg", "Invalid Credentials");
            String path = "index";
            templateEngine.process(path, ctx, response.getWriter());
            return;
        }
        request.getSession().setAttribute("user", customer);
        String servlet = "UserHomePage";
        response.sendRedirect(servlet);
    }
}
