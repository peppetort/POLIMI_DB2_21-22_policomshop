package controllers;

import org.thymeleaf.util.StringUtils;
import services.BuyService;

import javax.persistence.PersistenceException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.BadRequestException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

@WebServlet(name = "SetOrder", value = "/SetOrder")
public class SetOrder extends HttpServletThymeleaf {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        try {
            BuyService buyService = (BuyService) request.getSession().getAttribute("BuyService");
            if (buyService == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            String offerIdParam = request.getParameter("offerRadio");
            String[] optionalProductIdListParam = request.getParameterValues("opProd");
            String startDateParam = request.getParameter("start_date");

            if (StringUtils.isEmptyOrWhitespace(offerIdParam)) {
                request.getSession().setAttribute("packageDetailsErrorMessage", "select a valid offer");
                response.sendRedirect("GetPackageDetails");
                return;
            }
            int offerId = Integer.parseInt(offerIdParam);
            buyService.setOffer(offerId);

            if (StringUtils.isEmptyOrWhitespace(startDateParam)) {
                request.getSession().setAttribute("packageDetailsErrorMessage", "select an activation date");
                response.sendRedirect("GetPackageDetails");
                return;
            }

            Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDateParam);
            if (startDate.after(new java.util.Date()))
                buyService.setStartDate(startDate);
            else {
                request.getSession().setAttribute("packageDetailsErrorMessage", "activation date is before actual date");
                response.sendRedirect("GetPackageDetails");
                return;
            }

            if (optionalProductIdListParam != null) {
                buyService.setOptionalProducts(Arrays.stream(optionalProductIdListParam).map(Integer::parseInt).collect(Collectors.toList()));
            } else {
                buyService.setOptionalProducts(new ArrayList<>());
            }

            response.sendRedirect("ReviewOrder");
        } catch (NumberFormatException | ParseException | BadRequestException e) {
            BuyService buyService = (BuyService) request.getSession().getAttribute("BuyService");
            buyService.stopProcess();
            request.getSession().removeAttribute("BuyService");
            response.sendRedirect(request.getContextPath());
        } catch (PersistenceException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }
}
