package controllers;

import org.thymeleaf.util.StringUtils;
import services.BuyService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.BadRequestException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
                throw new BadRequestException();
            }
            int offerId = Integer.parseInt(offerIdParam);
            buyService.setOffer(offerId);

            if (StringUtils.isEmptyOrWhitespace(startDateParam)) {
                throw new BadRequestException();
            }

            Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDateParam);
            if (startDate.after(new java.util.Date()))
                buyService.setStartDate(startDate);
            else {
                throw new BadRequestException();
            }

            if (optionalProductIdListParam != null) {
                buyService.setOptionalProducts(Arrays.stream(optionalProductIdListParam).map(Integer::parseInt).collect(Collectors.toList()));
            }

            response.sendRedirect("ReviewOrder");
        } catch (NumberFormatException | ParseException e) {
            BuyService buyService = (BuyService) request.getSession().getAttribute("BuyService");
            buyService.stopProcess();
            response.sendRedirect(request.getContextPath());
        } catch (BadRequestException e) {
            response.sendRedirect("GetPackageDetails");
        }

    }
}
