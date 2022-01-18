package controllers;

import exception.UserExeption;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.util.StringUtils;
import services.UserService;

import javax.ejb.EJB;
import javax.persistence.PersistenceException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "SignUp", value = "/SignUp")
public class SignUp extends HttpServletThymeleaf {
    @EJB(beanName = "UserService")
    UserService usrService;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (StringUtils.isEmptyOrWhitespace(username) && StringUtils.isEmptyOrWhitespace(email) && StringUtils.isEmptyOrWhitespace(password)) {
            renderPage(request, response, "Invalid Request");
            return;
        }

        try {
            usrService.registerNewUser(username, email, password);
            response.sendRedirect("SignIn");
        } catch (UserExeption e) {
            renderPage(request, response, e.getMessage());
        } catch (PersistenceException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
        templateEngine.process("SignUpPage", ctx, response.getWriter());
    }

    private void renderPage(HttpServletRequest request, HttpServletResponse response, String errorMsg) throws IOException {
        final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
        ctx.setVariable("errorMsg", errorMsg);
        templateEngine.process("SignUpPage", ctx, response.getWriter());
    }
}
