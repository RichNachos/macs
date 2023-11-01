package com.example.part2.servlets;

import com.example.part2.Cart;
import com.example.part2.Item;
import com.example.part2.database.WebstoreDAO;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "CartServlet", value = "/CartServlet")
public class CartServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        WebstoreDAO dao =(WebstoreDAO) request.getServletContext().getAttribute("dao");
        Cart cart = (Cart) request.getSession().getAttribute("cart");
        String productId = request.getParameter("productID").trim();
        Item item = dao.getItem(productId);
        if (cart.Counts.containsKey(item)) {
            cart.Counts.put(item, cart.Counts.get(item) + 1);
        } else {
            cart.Counts.put(item, 1);
        }
        response.sendRedirect(request.getContextPath() + "/cart.jsp");
    }
}
