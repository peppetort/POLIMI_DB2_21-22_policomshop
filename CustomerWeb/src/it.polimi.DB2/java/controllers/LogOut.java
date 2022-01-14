package controllers;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@WebServlet(name = "Logout", value = "/Logout")
public class LogOut extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().invalidate();

        for (String s : Collections.list(request.getSession().getAttributeNames())) {
            request.getSession().removeAttribute(s);
        }

        response.sendRedirect(request.getContextPath());
    }

}