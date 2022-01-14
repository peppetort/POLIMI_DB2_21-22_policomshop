package controllers;

import entities.*;
import org.thymeleaf.context.WebContext;
import services.OptionalProductService;
import services.PackageService;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.BadRequestException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name="GetEmployeeHome", urlPatterns = "/")
public class GetEmployeeHome extends HttpServletThymeleaf{

    @EJB(beanName = "PackageService")
    PackageService packageService;
    @EJB(beanName = "OptionalProductService")
    OptionalProductService optionalProductService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            FixedPhone fixedPhone = null;
            List<FixedInternet> fixedInternetList = new ArrayList<>();
            List<MobileInternet> mobileInternetList = new ArrayList<>();
            List<MobilePhone> mobilePhoneList = new ArrayList<>();
            List<OptionalProduct> optionalProducts = optionalProductService.getAllOptionalProducts();
            System.out.println(optionalProducts);

            for (Service s : packageService.getAllService()) {
                if (s instanceof FixedInternet) fixedInternetList.add((FixedInternet) s);
                else if (s instanceof MobileInternet) mobileInternetList.add((MobileInternet) s);
                else if (s instanceof MobilePhone) mobilePhoneList.add((MobilePhone) s);
                else if (s instanceof FixedPhone) fixedPhone = (FixedPhone) s;
            }

            final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
            ctx.setVariable("user", request.getSession().getAttribute("user"));
            ctx.setVariable("fixedInternet", fixedInternetList);
            ctx.setVariable("mobileInternet", mobileInternetList);
            ctx.setVariable("fixedPhone", fixedPhone);
            ctx.setVariable("mobilePhone", mobilePhoneList);
            ctx.setVariable("optionalProducts", optionalProducts);
            templateEngine.process("HomePage", ctx, response.getWriter());
        }catch (BadRequestException e){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }
}
