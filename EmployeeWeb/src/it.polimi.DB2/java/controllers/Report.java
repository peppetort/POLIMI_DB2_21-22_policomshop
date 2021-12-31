package controllers;

import entities.PackageStatistics;
import entities.ServicePackage;
import org.thymeleaf.context.WebContext;
import services.ReportService;
import utils.Pair;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "Report", urlPatterns = "/Report")
public class Report extends HttpServletThymeleaf {

    @EJB
    ReportService reportService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<PackageStatistics> packageStatistics = reportService.getAllStatForPackage();
        Map<ServicePackage, Integer> numberPurchases = new HashMap<>();
        Map<Pair<ServicePackage, Integer>, Integer> numberPurchasesForValidityPeriod = new HashMap<>();

        for (PackageStatistics s : packageStatistics) {
            //Number purchases
            if (numberPurchases.containsKey(s.getServicePackage())) {
                //Here it's just an update for the value
                numberPurchases.put(s.getServicePackage(), numberPurchases.get(s.getServicePackage()) + s.getNumPurchases());
            } else {
                numberPurchases.put(s.getServicePackage(), s.getNumPurchases());
            }

            /*Number purchases for validity period
             * Nel db c'è il constrain che la coppia sia unica*/
            numberPurchasesForValidityPeriod.put(new Pair<>(s.getServicePackage(), s.getValidityPeriod()), s.getNumPurchases());


        }

        final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
        ctx.setVariable("user", request.getSession().getAttribute("user"));
        ctx.setVariable("numberPurchases", numberPurchases);
        ctx.setVariable("numberPurchasesForValidityPeriod", numberPurchasesForValidityPeriod);
        templateEngine.process("ReportPage", ctx, response.getWriter());
    }
}
