package controllers;

import entities.Customer;
import org.thymeleaf.context.WebContext;
import services.UserService;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "SignUp", value = "/SignUp")
public class SignUp extends HttpServletThymeleaf {
    @EJB(beanName = "UserService")
    UserService usrService;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());

        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        if (username == null || email == null || password == null ||
                username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            ctx.setVariable("errorMsg", "Invalid inserted credentials");
            templateEngine.process("SignUpPage", ctx, response.getWriter());
            return;
        }

        Customer newUser = usrService.registerNewUser(username, email, password);
        if (newUser == null) {
            ctx.setVariable("errorMsg", "Invalid data, username or password already present");
            templateEngine.process("SignUpPage", ctx, response.getWriter());
            return;
        }
        response.sendRedirect("SignInPage");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //when the servlet is called with a DoGet, it's display the SignUp form
        final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
        templateEngine.process("SignUpPage", ctx, response.getWriter());
    }
}
