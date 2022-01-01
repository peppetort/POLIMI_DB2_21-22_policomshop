package controllers;

import services.PackageService;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "NewServicePackage", urlPatterns = "/SaveServicePackage")
public class NewServicePackage extends HttpServletThymeleaf {
    @EJB(name = "PackageService")
    PackageService packageService;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        String[] serviceIDs = request.getParameterValues("serviceIDs");
        String[] optionalIDs = request.getParameterValues("optionalIDs");
        if (name == null || name.isEmpty() || serviceIDs == null || optionalIDs == null)   {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        List<Long> tempService = new ArrayList<>();
        for (String s : serviceIDs) {
            tempService.add(Long.parseLong(s));
        }
        List<Long> tempOptional = new ArrayList<>();
        for (String s : optionalIDs) {
            tempOptional.add(Long.parseLong(s));
        }
        packageService.saveNewServicePackage(name, tempService, tempOptional);
        response.sendRedirect("Employee");
    }
}
