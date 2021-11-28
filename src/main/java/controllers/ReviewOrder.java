package controllers;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import services.BuyService;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ReviewOrder", urlPatterns = "/ReviewOrder")
public class ReviewOrder extends HttpServlet {

    private final TemplateEngine templateEngine = new TemplateEngine();

    @Inject
    BuyService buyService;

    @Override
    public void init() {
        ServletContext servletContext = getServletContext();
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setSuffix(".html");
        templateEngine.setTemplateResolver(templateResolver);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //TODO ma quando viene tolto??
        request.getSession().setAttribute("paymentInProgress", true);
        final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
        ctx.setVariable("isLoggedIn", request.getSession().getAttribute("user") != null);
        ctx.setVariable("order", buyService.getOrder());
        templateEngine.process("/WEB-INF/templates/ReviewOrderPage", ctx, response.getWriter());
    }
}
