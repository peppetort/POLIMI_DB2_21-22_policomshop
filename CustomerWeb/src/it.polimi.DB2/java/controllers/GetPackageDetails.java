package controllers;

import entities.ServicePackage;
import org.thymeleaf.context.WebContext;
import services.BuyService;
import services.PackageService;

import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.BadRequestException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet(name = "GetPackageDetails", value = "/GetPackageDetails")
public class GetPackageDetails extends HttpServletThymeleaf {

    @EJB(name = "PackageService")
    PackageService packageService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            BuyService buyService = (BuyService) request.getSession().getAttribute("BuyService");
            if (buyService != null) {
                renderPage(request, response, buyService, null);
                return;
            }

            String servicePackageIdParam = request.getParameter("id_sp");
            Long servicePackageId = Long.parseLong(servicePackageIdParam);
            ServicePackage servicePackage = packageService.findById(servicePackageId);
            if (servicePackage == null) {
                response.sendRedirect(request.getContextPath());
                return;
            }
            InitialContext ic = new InitialContext();
            // Retrieve the EJB using JNDI lookup
            buyService = (BuyService) ic.lookup("java:module/BuyService");
            request.getSession().setAttribute("BuyService", buyService);
            buyService.initOrder(servicePackageId);
            renderPage(request, response, buyService, null);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath());
        } catch (BadRequestException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            BuyService buyService = (BuyService) request.getSession().getAttribute("BuyService");
            if (buyService == null) {
                response.sendRedirect(getServletContext().getContextPath());
                return;
            }

            String offerIdParam = request.getParameter("offerRadio");
            String[] optionalProductIdListParam = request.getParameterValues("opProd");
            String startDateParam = request.getParameter("start_date");

            if (offerIdParam == null || offerIdParam.isEmpty())
                throw new BadRequestException("Choose an offer among those available");
            int offerId = Integer.parseInt(offerIdParam);
            buyService.setOffer(offerId);

            if (startDateParam == null || startDateParam.isEmpty())
                throw new BadRequestException("Please select a valid start date");
            Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDateParam);
            if (startDate.after(new java.util.Date()))
                buyService.setStartDate(startDate);
            else {
                throw new BadRequestException("Selected date is too old");
            }

            if (optionalProductIdListParam != null) {
                buyService.setOptionalProducts(optionalProductIdListParam);
            }

            response.sendRedirect("ReviewOrder");


        } catch (NumberFormatException | ParseException | IllegalAccessException e) {
            BuyService buyService = (BuyService) request.getSession().getAttribute("BuyService");
            buyService.stopProcess();
            response.sendRedirect(request.getContextPath());
        } catch (BadRequestException e) {
            BuyService buyService = (BuyService) request.getSession().getAttribute("BuyService");
            renderPage(request, response, buyService, e.getMessage());
        }

    }

    private void renderPage(HttpServletRequest request, HttpServletResponse response, BuyService buyService, String errorMes) throws IOException {
        String path = "PackageDetailsPage";
        final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
        ctx.setVariable("user", request.getSession().getAttribute("user"));
        ctx.setVariable("servicePackage", buyService.getServicePackage());
        ctx.setVariable("order", buyService.getOrder());
        ctx.setVariable("optionalProductMap", buyService.getOptionalProduct());
        if (errorMes != null && !errorMes.isEmpty()) ctx.setVariable("errorMes", errorMes);
        templateEngine.process(path, ctx, response.getWriter());
    }
}
