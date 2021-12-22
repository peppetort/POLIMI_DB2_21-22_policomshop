package controllers;

import entities.Customer;
import org.thymeleaf.context.WebContext;
import services.BuyService;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.BadRequestException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet(name = "CustomizeOrder", value = "/CustomizeOrder")
public class CustomizeOrder extends HttpServletThymeleaf {

    @EJB(name = "BuyService")
    BuyService buyService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String idServicePackage = request.getParameter("id_sp");
            request.getSession().setAttribute("BuyService", buyService);
            if (idServicePackage == null || idServicePackage.isEmpty()) {
                if (!buyService.isInitialized()) {
                    response.sendRedirect(request.getContextPath());
                    return;
                }
            } else {
                int newId = Integer.parseInt(idServicePackage);
                buyService.initOrder((Customer) request.getSession().getAttribute("user"), newId);
            }
            renderPage(request, response, buyService, null);
        } catch (BadRequestException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BuyService buyService = (BuyService) request.getSession().getAttribute("BuyService");
        if (buyService == null) {
            response.sendRedirect(getServletContext().getContextPath());
            return;
        }
        String offer = request.getParameter("offerRadio");
        String[] opProd = request.getParameterValues("opProd");
        String startDateString = request.getParameter("start_date");
        try {
            if (offer == null || offer.isEmpty())
                throw new BadRequestException("Choose an offer!");
            buyService.setOffer(Integer.parseInt(offer));
            if (startDateString == null || startDateString.isEmpty())
                throw new BadRequestException("Selezionare un valore valido per Start Date!");
            Date d = new SimpleDateFormat("yyyy-MM-dd").parse(startDateString);
            if (d.after(new java.util.Date()))
                buyService.setStartDate(d);
            else {
                throw new BadRequestException("Start date must be after now");
            }
            if (opProd != null) {
                buyService.setOptionalProducts(opProd);
            }
            if (buyService.isCorrectFilled(false)) response.sendRedirect("ReviewOrder");
            else renderPage(request, response, buyService, "Your order is not correct filled, sorry");
        } catch (BadRequestException | NumberFormatException | IllegalAccessException | ParseException e) {
            renderPage(request, response, buyService, e.getMessage());
        }
    }

    private void renderPage(HttpServletRequest request, HttpServletResponse response, BuyService buyService, String errorMes) throws IOException {
        String path = "CustomizeOrderPage";
        final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
        ctx.setVariable("user", request.getSession().getAttribute("user"));
        ctx.setVariable("servicePackage", buyService.getServicePackage());
        ctx.setVariable("order", buyService.getOrder());
        ctx.setVariable("optionalProductMap", buyService.getOptionalProduct());
        if (errorMes != null && !errorMes.isEmpty()) ctx.setVariable("errorMes", errorMes);
        templateEngine.process(path, ctx, response.getWriter());
    }
}
