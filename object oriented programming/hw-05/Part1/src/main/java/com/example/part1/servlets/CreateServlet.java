package com.example.part1.servlets;

import com.example.part1.AccountManager;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "CreateServlet", value = "/CreateServlet")
public class CreateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AccountManager manager = (AccountManager) getServletContext().getAttribute("AccountManager");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (manager.hasAccount(username))
            request.getRequestDispatcher("nameinuse.jsp").forward(request, response);
        else if (manager.addAccount(username, password))
            request.getRequestDispatcher("welcome.jsp").forward(request, response);
        else
            request.getRequestDispatcher("createaccount.jsp").forward(request, response);
    }
}
