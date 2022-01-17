package controllers;

import entities.Service;
import exception.OfferException;
import exception.OptionalProductException;
import exception.ServiceException;
import exception.ServicePackageException;
import org.thymeleaf.util.StringUtils;
import services.OfferService;
import services.OptionalProductService;
import services.PackageService;
import utils.Pair;

import javax.ejb.EJB;
import javax.persistence.PersistenceException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        String validityPeriodAndFees = request.getParameter("validityPeriodAndFees");
        String fixedInternet = request.getParameter("fixedInternetRadio");
        String mobileInternet = request.getParameter("mobileInternetRadio");
        String mobilePhone = request.getParameter("mobilePhoneRadio");
        String fixedPhone = request.getParameter("fixedPhoneRadio");
        String[] optionalProductIdList = request.getParameterValues("opProd");


        Pattern pattern = Pattern.compile("(\\d+:\\d+[,]?)+");
        Matcher matcher = pattern.matcher(validityPeriodAndFees);
        List<Pair<Integer, Double>> validityPeriodAndMonthlyFee = new ArrayList<>();
        while (matcher.find()) {
            String[] values = matcher.group().replace(",", "").split(":");
            validityPeriodAndMonthlyFee.add(new Pair<>(Integer.parseInt(values[0]), Double.parseDouble(values[1])));
        }
        if (StringUtils.isEmptyOrWhitespace(name) || StringUtils.isEmptyOrWhitespace(validityPeriodAndFees)) {
            response.sendRedirect(request.getContextPath());
            return;
        }

        if (StringUtils.isEmptyOrWhitespace(fixedInternet) && StringUtils.isEmptyOrWhitespace(mobileInternet) && StringUtils.isEmptyOrWhitespace(mobilePhone) && StringUtils.isEmptyOrWhitespace(fixedPhone)) {
            response.sendRedirect(request.getContextPath());
            return;
        }

        List<Long> servicesIDs = new ArrayList<>();
        List<Long> optionalProductsIdList = new ArrayList<>();

        try {
            for (Pair<Integer, Double> p : validityPeriodAndMonthlyFee) {
                if (p.getObject1() < 1 || p.getObject2() < 0) {
                    response.sendRedirect(request.getContextPath());
                    return;
                }
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
            for (Pair<Integer, Double> p : validityPeriodAndMonthlyFee) {
                offerService.createNewOffer(newServicePackageId, p.getObject1(), p.getObject2());
            }
            response.sendRedirect(getServletContext().getContextPath());

        } catch (NumberFormatException | ServiceException | OptionalProductException | ServicePackageException | OfferException e) {
            request.getSession().setAttribute("errorMessageServicePackage", e.getMessage());
            response.sendRedirect(request.getContextPath());
        } catch (PersistenceException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
