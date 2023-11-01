<%@ page import="com.example.part2.Cart" %>
<%@ page import="com.example.part2.Item" %><%--
  Created by IntelliJ IDEA.
  User: georgi
  Date: 29.06.22
  Time: 22:36
  To change this template use File | Settings | File Templates.
--%>
<%
    Cart cart = (Cart)request.getSession().getAttribute("cart");
    double total = 0;
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Shopping Cart</title>
</head>
<body>
<h1>Shopping Cart</h1>
<form action="UpdateCartServlet" method="post">
<ul>
    <%
    for (Item item : cart.Counts.keySet()) {
        total += item.getPrice() * cart.Counts.get(item);
        out.println("<li>");
        out.println("<input type='text' value='" + cart.Counts.get(item) + "' name='" + item.getProductId() + "'>");
        out.println("<label>" + item.getItemName() + ", $" + item.getPrice() + "</label>");
        out.println("</li>");
    }

    out.println("<label>Total: $" + total + "</label>");
    out.println("<input type='submit' value='Update Cart'>");
    %>
</form>
</ul>
<a href="index.jsp">Continue Shopping</a>
</body>
</html>
