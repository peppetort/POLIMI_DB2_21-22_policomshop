package filters;

import services.BuyService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter("/InvalidateBuyProcess")
public class InvalidateBuyProcess implements Filter {

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        BuyService buyService = (BuyService) req.getSession().getAttribute("BuyService");

        if (buyService != null) {
            buyService.stopProcess();
            req.getSession().removeAttribute("BuyService");
        }
        chain.doFilter(request, response);

    }


}