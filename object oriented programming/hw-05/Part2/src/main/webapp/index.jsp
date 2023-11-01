<%@ page import="java.io.PrintWriter" %>
<%@ page import="com.example.part2.database.WebstoreDAO" %>
<%@ page import="com.example.part2.Item" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Student Store</title>
</head>
<body>
<h1>Student Store</h1>
<br/>
<p>Items available:</p>
<ul>
    <%

        WebstoreDAO dao = (WebstoreDAO) request.getServletContext().getAttribute("dao");
        List<Item> items = dao.getAllItems();
        for (Item item : items) {
            out.println("<li><a href='show-product.jsp?id=" + item.getProductId() + "'>" + item.getItemName() + "</a></li>");
        }
    %>
</ul>
</body>
</html>