package filters;

import entities.User;
import services.BuyService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/ValidBuyProcess")
public class LoggedIn implements Filter {

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String homePath = req.getServletContext().getContextPath();

        User user = (User) req.getSession().getAttribute("user");

        if (user == null) {
            res.sendRedirect(homePath);
            return;
        }
        chain.doFilter(request, response);

    }


}