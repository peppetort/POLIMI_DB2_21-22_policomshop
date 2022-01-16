package controllers;

import entities.OptionalProduct;
import entities.Service;
import exception.OfferException;
import exception.OptionalProductException;
import exception.ServiceException;
import exception.ServicePackageException;
import org.thymeleaf.util.StringUtils;
import services.OfferService;
import services.OptionalProductService;
import services.PackageService;

import javax.ejb.EJB;
import javax.persistence.PersistenceException;
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

        if (StringUtils.isEmptyOrWhitespace(name) || StringUtils.isEmptyOrWhitespace(validityPeriod) || StringUtils.isEmptyOrWhitespace(monthFee)) {
            response.sendRedirect(request.getContextPath());
            return;
        }

        if (StringUtils.isEmptyOrWhitespace(fixedInternet) && StringUtils.isEmptyOrWhitespace(mobileInternet) && StringUtils.isEmptyOrWhitespace(mobilePhone) && StringUtils.isEmptyOrWhitespace(fixedPhone)) {
            response.sendRedirect(request.getContextPath());
            return;
        }

        List<Long> servicesIDs = new ArrayList<>();
        List<Long> optionalProductsIdList = new ArrayList<>();
        int validityPeriodInt;
        double monthFeeD;

        try {
            validityPeriodInt = Integer.parseInt(validityPeriod);
            monthFeeD = Double.parseDouble(monthFee);

            if (validityPeriodInt < 1 || monthFeeD < 0) {
                response.sendRedirect(request.getContextPath());
                return;
            }

            if (fixedInternet != null) {
                long fixedInternetId = Integer.parseInt(fixedInternet);
                Service s = packageService.getServiceById(fixedInternetId);
                if (s.getType() != 1) {
                    response.sendRedirect(request.getContextPath());
                    return;
                }
                servicesIDs.add(fixedInternetId);
            }

            if (mobileInternet != null) {
                long mobileInternetId = Integer.parseInt(mobileInternet);
                Service s = packageService.getServiceById(mobileInternetId);
                if (s.getType() != 3) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                servicesIDs.add(mobileInternetId);
            }

            if (mobilePhone != null) {
                long mobilePhoneId = Integer.parseInt(mobilePhone);
                Service s = packageService.getServiceById(mobilePhoneId);
                if (s.getType() != 4) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                servicesIDs.add(mobilePhoneId);
            }

            if (fixedPhone != null) {
                long fixedPhoneId = Integer.parseInt(fixedPhone);
                Service s = packageService.getServiceById(fixedPhoneId);
                if (s.getType() != 2) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                servicesIDs.add(fixedPhoneId);
            }

            if (optionalProductIdList != null) {
                for (String sId : optionalProductIdList) {
                    long id = Integer.parseInt(sId);
                    optionalProductsIdList.add(id);
                }
            }

            Long newServicePackageId = packageService.createNewServicePackage(name, servicesIDs, optionalProductsIdList);
            offerService.createNewOffer(newServicePackageId, validityPeriodInt, monthFeeD);
            response.sendRedirect(getServletContext().getContextPath());

        } catch (NumberFormatException | ServiceException | OptionalProductException | ServicePackageException | OfferException e) {
            request.getSession().setAttribute("errorMessageServicePackage", e.getMessage());
            response.sendRedirect(request.getContextPath());
        } catch (PersistenceException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
