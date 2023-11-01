package com.example.part2.servlets;

import com.example.part2.Cart;
import com.example.part2.Item;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "UpdateCartServlet", value = "/UpdateCartServlet")
public class UpdateCartServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = (Cart)request.getSession().getAttribute("cart");
        List<Item> itemsToRemove = new ArrayList<Item>();
        for (Item item : cart.Counts.keySet()) {
            int newCount = Integer.parseInt(request.getParameter(item.getProductId()));
            if (newCount == 0) {
                itemsToRemove.add(item);
            } else {
                cart.Counts.put(item, newCount);
            }
        }
        for (Item item : itemsToRemove) {
            cart.Counts.remove(item);
        }
        request.getRequestDispatcher("cart.jsp").forward(request,response);
    }
}
