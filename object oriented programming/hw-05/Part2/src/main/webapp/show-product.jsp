<%@ page import="com.example.part2.database.WebstoreDAO" %>
<%@ page import="com.example.part2.Item" %><%--
  Created by IntelliJ IDEA.
  User: georgi
  Date: 29.06.22
  Time: 21:57
  To change this template use File | Settings | File Templates.
--%>
<%
    String productId = request.getParameter("id");
    WebstoreDAO dao = (WebstoreDAO) request.getServletContext().getAttribute("dao");
    Item item = dao.getItem(productId);
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title><% out.println(item.getItemName()); %></title>
</head>
<body>
<h1><% out.println(item.getItemName()); %></h1>
<img src="store-images/<% out.println(item.getImageFile()); %>">
<form action="CartServlet" method="post">
    <label>$<% out.println(item.getPrice()); %></label>
    <input name="productID" type="hidden" value="<% out.println(productId); %>"/>
    <input type="submit" value="Add to Cart">
</form>
</body>
</html>
