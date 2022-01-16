package controllers;

import exception.ServicePackageException;
import org.thymeleaf.context.WebContext;
import services.BuyService;
import services.PackageService;

import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "GetPackageDetails", value = "/GetPackageDetails")
public class GetPackageDetails extends HttpServletThymeleaf {

    @EJB(name = "PackageService")
    PackageService packageService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {

            BuyService buyService = (BuyService) request.getSession().getAttribute("BuyService");
            if (buyService != null) {
                String errorMessage = (String) request.getSession().getAttribute("packageDetailsErrorMessage");

                if (errorMessage != null) {
                    request.getSession().removeAttribute("packageDetailsErrorMessage");
                }

                renderPage(request, response, buyService, errorMessage);
                return;
            }


            String servicePackageIdParam = request.getParameter("id_sp");
            Long servicePackageId = Long.parseLong(servicePackageIdParam);
            packageService.findById(servicePackageId);

            InitialContext ic = new InitialContext();
            // Retrieve the EJB using JNDI lookup
            buyService = (BuyService) ic.lookup("java:module/BuyService");
            request.getSession().setAttribute("BuyService", buyService);
            buyService.initOrder(servicePackageId);


            renderPage(request, response, buyService, null);
        } catch (NumberFormatException | ServicePackageException e) {
            response.sendRedirect(request.getContextPath());
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }


    }

    private void renderPage(HttpServletRequest request, HttpServletResponse response, BuyService buyService, String errorMessage) throws IOException {
        String path = "PackageDetailsPage";
        final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
        ctx.setVariable("user", request.getSession().getAttribute("user"));
        ctx.setVariable("buyService", buyService);
        ctx.setVariable("errorMes", errorMessage);
        templateEngine.process(path, ctx, response.getWriter());
    }
}
