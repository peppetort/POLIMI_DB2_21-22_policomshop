package controllers;

import entities.OptionalProduct;
import entities.Service;
import services.OfferService;
import services.OptionalProductService;
import services.PackageService;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.BadRequestException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "CreateServicePackage", urlPatterns = "/CreateServicePackage")
public class CreateServicePackage extends HttpServlet {

    @EJB(name = "PackageService")
    PackageService packageService;
    @EJB(name = "OptionalProdService")
    OptionalProductService optionalProdService;
    @EJB(name = "OfferService")
    OfferService offerService;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("spName");
        String validityPeriod = request.getParameter("spValidityPeriod");
        String monthFee = request.getParameter("spMonthFee");
        String fixedInternet = request.getParameter("fixedInternetRadio");
        String mobileInternet = request.getParameter("mobileInternetRadio");
        String mobilePhone = request.getParameter("mobilePhoneRadio");
        String fixedPhone = request.getParameter("fixedPhoneRadio");
        String[] optionalProductIdList = request.getParameterValues("opProd");

        if (name == null || name.isEmpty() || validityPeriod == null || validityPeriod.isEmpty() || monthFee == null || monthFee.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if ((fixedInternet == null || fixedInternet.isEmpty()) && (mobileInternet == null || mobileInternet.isEmpty()) && (mobilePhone == null || mobilePhone.isEmpty()) && (fixedPhone == null || fixedPhone.isEmpty())) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int validityPeriodInt;
        double monthFeeD;

        try {
            validityPeriodInt = Integer.parseInt(validityPeriod);
            if (validityPeriodInt < 1) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            monthFeeD = Double.parseDouble(monthFee);
            if (monthFeeD < 0) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        List<Long> servicesIDs = new ArrayList<>();
        List<Long> optionalProductsIdList = new ArrayList<>();

        if (fixedInternet != null) {
            try {
                long fixedInternetId = Integer.parseInt(fixedInternet);
                Service s = packageService.getServiceById(fixedInternetId);
                if (s == null || s.getType() != 1) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                servicesIDs.add(fixedInternetId);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        }

        if (mobileInternet != null) {
            try {
                long mobileInternetId = Integer.parseInt(mobileInternet);
                Service s = packageService.getServiceById(mobileInternetId);
                if (s == null || s.getType() != 3) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                servicesIDs.add(mobileInternetId);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        }

        if (mobilePhone != null) {
            try {
                long mobilePhoneId = Integer.parseInt(mobilePhone);
                Service s = packageService.getServiceById(mobilePhoneId);
                if (s == null || s.getType() != 4) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                servicesIDs.add(mobilePhoneId);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        }

        if (fixedPhone != null) {
            try {
                long fixedPhoneId = Integer.parseInt(fixedPhone);
                Service s = packageService.getServiceById(fixedPhoneId);
                if (s == null || s.getType() != 2) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                servicesIDs.add(fixedPhoneId);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        }

        if (optionalProductIdList != null) {
            for (String sId : optionalProductIdList) {
                try {
                    long id = Integer.parseInt(sId);
                    OptionalProduct o = optionalProdService.getById(id);
                    if (o == null) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                        return;
                    }
                    optionalProductsIdList.add(id);

                } catch (NumberFormatException | BadRequestException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
            }
        }

        try {
            Long newServicePackageId = packageService.createNewServicePackage(name, servicesIDs, optionalProductsIdList);
            offerService.createNewOffer(newServicePackageId, validityPeriodInt, monthFeeD);
            response.sendRedirect(getServletContext().getContextPath());
        }catch (BadRequestException e){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
