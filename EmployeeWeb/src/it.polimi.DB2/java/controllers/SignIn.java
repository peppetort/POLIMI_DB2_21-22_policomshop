package controllers;

import entities.Employee;
import exception.UserNotFound;
import org.thymeleaf.context.WebContext;
import services.UserService;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "SignIn", value = "/SignIn")
public class SignIn extends HttpServletThymeleaf {
    @EJB(beanName = "UserService")
    UserService userService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        renderPage(request, response, (request.getSession().getAttribute("user") != null ? "You are already logged in" : null));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email"), //email is the id of the field in the form
                pwd = request.getParameter("password");
        if (email == null || email.isEmpty() ||
                pwd == null || pwd.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            Employee employee = userService.checkCredentialsEmployee(email, pwd);
            request.getSession().setAttribute("user", employee);
            response.sendRedirect(request.getContextPath());
        } catch (UserNotFound e) {
            renderPage(request, response, "Invalid Credentials");
        }
    }

    private void renderPage(HttpServletRequest request, HttpServletResponse response, String errorMsg) throws IOException {
        final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
        ctx.setVariable("errorMsg", errorMsg);
        templateEngine.process("SignInPage", ctx, response.getWriter());
    }
}
