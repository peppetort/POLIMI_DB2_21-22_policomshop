package filters;

import services.BuyService;

import java.io.IOException;
import java.util.Collections;
import java.util.Objects;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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