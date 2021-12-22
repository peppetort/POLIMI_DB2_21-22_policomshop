package filters;

import services.BuyService;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter("/ValidBuyProcess")
public class ValidBuyProcess implements Filter {

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String homePath = req.getServletContext().getContextPath();

        BuyService buyService = (BuyService) req.getSession().getAttribute("BuyService");

        if (buyService == null || buyService.getOrder() == null || buyService.getOrder().getOffer() == null || buyService.getOrder().getActivationDate() == null) {
            res.sendRedirect(homePath);
            return;
        }
        chain.doFilter(request, response);

    }


}