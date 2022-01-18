package controllers;

import entities.Employee;
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
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (StringUtils.isEmptyOrWhitespace(email) && StringUtils.isEmptyOrWhitespace(password)) {
            renderPage(request, response, "Invalid data");
            return;
        }

        try {
            Employee employee = userService.checkCredentialsEmployee(email, password);
            request.getSession().setAttribute("user", employee);
            response.sendRedirect(request.getContextPath());
        } catch (UserExeption e) {
            renderPage(request, response, e.getMessage());
        } catch (PersistenceException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void renderPage(HttpServletRequest request, HttpServletResponse response, String errorMsg) throws IOException {
        final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
        ctx.setVariable("errorMsg", errorMsg);
        templateEngine.process("SignInPage", ctx, response.getWriter());
    }
}
