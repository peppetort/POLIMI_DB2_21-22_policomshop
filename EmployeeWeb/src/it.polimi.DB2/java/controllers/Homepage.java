package controllers;

import entities.*;
import org.thymeleaf.context.WebContext;
import services.PackageService;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name="Homepage", urlPatterns = "/")
public class Homepage extends HttpServletThymeleaf{

    @EJB
    PackageService packageService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        FixedPhone fixedPhone = null;
        List<FixedInternet> fixedInternetList = new ArrayList<>();
        List<MobileInternet> mobileInternetList = new ArrayList<>();
        List<MobilePhone> mobilePhoneList = new ArrayList<>();
        List<OptionalProduct> optionalProducts = packageService.getAllOptional();

        for(Service s : packageService.getAllService()) {
            if (s instanceof FixedInternet) fixedInternetList.add((FixedInternet) s);
            else if(s instanceof MobileInternet) mobileInternetList.add((MobileInternet) s);
            else if(s instanceof MobilePhone) mobilePhoneList.add((MobilePhone) s);
            else if(s instanceof FixedPhone) fixedPhone = (FixedPhone) s;
        }

        final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
        ctx.setVariable("fixedInternet", fixedInternetList);
        ctx.setVariable("mobileInternet", mobileInternetList);
        ctx.setVariable("fixedPhone", fixedPhone);
        ctx.setVariable("mobilePhone", mobilePhoneList);
        ctx.setVariable("optionals", optionalProducts);
        templateEngine.process("HomePage", ctx, response.getWriter());
    }
}
