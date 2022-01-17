package controllers;

import entities.*;
import org.thymeleaf.context.WebContext;
import services.ReportService;
import utils.Pair;

import javax.ejb.EJB;
import javax.persistence.PersistenceException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@WebServlet(name = "Report", urlPatterns = "/Report")
public class Report extends HttpServletThymeleaf {

    @EJB(name = "ReportService")
    ReportService reportService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<PackagePurchasesStatistics> packagePurchasesStatistics = reportService.getAllStatPackagePurchases();
        Map<ServicePackage, Integer> numberPurchases = new HashMap<>();
        Map<Pair<ServicePackage, Integer>, Integer> numberPurchasesForValidityPeriod = new HashMap<>();
        Map<ServicePackage, Pair<Double, Double>> amountPurchases = new HashMap<>();

        try {
            for (PackagePurchasesStatistics s : packagePurchasesStatistics) {
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

                if (amountPurchases.containsKey(s.getServicePackage())) {
                    Pair<Double, Double> temp = amountPurchases.get(s.getServicePackage());
                    Pair<Double, Double> newPair = new Pair(temp.getObject1() + s.getTotalMonthlyFee(), temp.getObject2() + s.getAmountForOptionalProds());
                    amountPurchases.put(s.getServicePackage(), newPair);
                } else {
                    Pair<Double, Double> newPair = new Pair(s.getTotalMonthlyFee(), s.getAmountForOptionalProds());
                    amountPurchases.put(s.getServicePackage(), newPair);
                }
            }

            List<PackageOptionalStatistics> packageOptionalStatisticsList = reportService.getAllStatPackageOptional();

            Map<ServicePackage, Integer> averageOptionalForEachServicePackage = new HashMap<>();

            for (PackageOptionalStatistics s : packageOptionalStatisticsList) {
                Integer numPurchases = averageOptionalForEachServicePackage.get(s.getServicePackage());
                if (numPurchases == null) {
                    averageOptionalForEachServicePackage.put(s.getServicePackage(), s.getNumPurchases());
                } else {
                    averageOptionalForEachServicePackage.put(s.getServicePackage(), numPurchases + s.getNumPurchases());
                }
            }

            Map<ServicePackage, Double> averageOptionalServicePackage = new HashMap<>();

            for (Map.Entry<ServicePackage, Integer> s : averageOptionalForEachServicePackage.entrySet()) {
                Integer numPurchases = numberPurchases.get(s.getKey());
                if (numPurchases != null) {
                    averageOptionalServicePackage.put(s.getKey(), (s.getValue() / (double) numPurchases));
                }
            }

            List<Order> suspendedOrders = reportService.getSuspendedOrder();

            Set<Customer> insolventCustomer = new TreeSet<>();
            for (Order o : suspendedOrders) {
                insolventCustomer.add(o.getCustomer());
            }
            List<AuditCustomer> auditCustomers = reportService.getAllAuditCustomer();

            final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
            ctx.setVariable("user", request.getSession().getAttribute("user"));
            ctx.setVariable("numberPurchases", numberPurchases);
            ctx.setVariable("numberPurchasesForValidityPeriod", numberPurchasesForValidityPeriod);
            ctx.setVariable("averageOptionalServicePackage", averageOptionalServicePackage);
            ctx.setVariable("amountPurchases", amountPurchases);
            ctx.setVariable("suspendedOrder", suspendedOrders);
            ctx.setVariable("insolventCustomer", insolventCustomer);
            ctx.setVariable("auditCustomers", auditCustomers);
            templateEngine.process("ReportPage", ctx, response.getWriter());
        }catch (PersistenceException e){
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
