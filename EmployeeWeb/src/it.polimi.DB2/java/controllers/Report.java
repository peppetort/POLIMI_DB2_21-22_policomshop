package controllers;

import entities.*;
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

    @EJB(name = "ReposrService")
    ReportService reportService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<ServicePackage, Long> stat1 = reportService.getTotalPurchasesPerPackage();
        Map<Offer, Long> stat2 = reportService.getTotalPurchasePerPackageAndValidityPeriod();
    }

//    @EJB
//    ReportService reportService;
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        List<PackageStatistics> packageStatistics = reportService.getAllStatForPackage();
//        Map<ServicePackage, Integer> numberPurchases = new HashMap<>();
//        Map<Pair<ServicePackage, Integer>, Integer> numberPurchasesForValidityPeriod = new HashMap<>();
//        Map<ServicePackage, Pair<Double, Double>> amountPurchases = new HashMap<>();
//
//        for (PackageStatistics s : packageStatistics) {
//            //Number purchases
//            if (numberPurchases.containsKey(s.getServicePackage())) {
//                //Here it's just an update for the value
//                numberPurchases.put(s.getServicePackage(), numberPurchases.get(s.getServicePackage()) + s.getNumPurchases());
//            } else {
//                numberPurchases.put(s.getServicePackage(), s.getNumPurchases());
//            }
//
//            /*Number purchases for validity period
//             * Nel db c'è il constrain che la coppia sia unica*/
//            numberPurchasesForValidityPeriod.put(new Pair<>(s.getServicePackage(), s.getValidityPeriod()), s.getNumPurchases());
//
//            if (amountPurchases.containsKey(s.getServicePackage())) {
//                Pair<Double, Double> temp = amountPurchases.get(s.getServicePackage());
//                Pair<Double, Double> newPair = new Pair(temp.getObject1() + s.getAmountWithOptional(), temp.getObject2() + s.getAmountWithoutOptional());
//                amountPurchases.put(s.getServicePackage(), newPair);
//            } else {
//                Pair<Double, Double> newPair = new Pair(s.getAmountWithOptional(), s.getAmountWithoutOptional());
//                amountPurchases.put(s.getServicePackage(), newPair);
//            }
//        }
//
//        List<AuditCustomer> auditCustomers = reportService.getAllAuditCustomer();
//
//        final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
//        ctx.setVariable("user", request.getSession().getAttribute("user"));
//        ctx.setVariable("numberPurchases", numberPurchases);
//        ctx.setVariable("numberPurchasesForValidityPeriod", numberPurchasesForValidityPeriod);
//        ctx.setVariable("amountPurchases", amountPurchases);
//        ctx.setVariable("auditCustomers", auditCustomers);
//        templateEngine.process("ReportPage", ctx, response.getWriter());
//    }
}
