package filters;

import entities.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoggedIn implements Filter {

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String baseURI = req.getServletContext().getContextPath();
        User user = (User) req.getSession().getAttribute("user");

        if (user == null && !req.getRequestURI().equals(baseURI + "/SignIn")) {
            res.sendRedirect(baseURI + "/SignIn");
            return;
        }
        chain.doFilter(request, response);
    }
}