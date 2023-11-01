package com.example.part1.servlets;

import com.example.part1.AccountManager;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "LoginServlet", value = "/LoginServlet")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AccountManager manager = (AccountManager) getServletContext().getAttribute("AccountManager");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (manager.login(username, password))
            request.getRequestDispatcher("welcome.jsp").forward(request, response);
        else
            request.getRequestDispatcher("loginfailed.jsp").forward(request, response);
    }
}
