package controllers;

import entities.Customer;
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
import javax.ws.rs.BadRequestException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet(name = "CustomizeOrder", value = "/CustomizeOrder")
public class CustomizeOrder extends HttpServlet {

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
        try {
            String idOffer = request.getParameter("id_offer");
            if (idOffer == null || idOffer.isEmpty()) {
                if (buyService.getOrder() == null) {
                    response.sendRedirect(request.getContextPath());
                    return;
                }
            } else {
                int newId = Integer.parseInt(idOffer);
                if (buyService.getOrder() == null ||
                        (buyService.getOrder().getOffer() != null && buyService.getOrder().getOffer().getId() != newId)) {
                    //TODO per far funionare questa cosa bisogna implmentare un filtro di accesso, a questa pagina non possono accedere gli employee
                    buyService.init((Customer) request.getSession().getAttribute("user"));
                    buyService.setOffer(newId);
                }
            }
            renderPage(request, response, null);
        } catch (IllegalAccessException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] opProd = request.getParameterValues("opProd");
        String startDateString = request.getParameter("start_date");
        try {
            if (startDateString == null || startDateString.isEmpty())
                throw new BadRequestException("Selezionare un valore valido per Start Date!");
            Date d = new SimpleDateFormat("yyyy-MM-dd").parse(startDateString);
            if (d.after(new java.util.Date()))
                buyService.setStartDate(d);
            else {
                throw new BadRequestException("Start date must be after now");
            }
            if (opProd != null) {
                for (String opId : opProd) {
                    buyService.addOptionalProduct(Integer.parseInt(opId));
                }
            }
            if (buyService.isCorrectFilled(false)) response.sendRedirect("ReviewOrder");
            else renderPage(request, response, "Your order is not correct filled, sorry");
        } catch (BadRequestException | NumberFormatException | IllegalAccessException | ParseException e) {
            renderPage(request, response, e.getMessage());
        }
    }

    private void renderPage(HttpServletRequest request, HttpServletResponse response, String errorMes) throws IOException {
        String path = "WEB-INF/templates/CustomizeOrderPage";
        final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
        ctx.setVariable("order", buyService.getOrder());
        ctx.setVariable("optionalProductMap", buyService.getOptionalProduct());
        if (errorMes != null && !errorMes.isEmpty()) ctx.setVariable("errorMes", errorMes);
        templateEngine.process(path, ctx, response.getWriter());
    }
}
