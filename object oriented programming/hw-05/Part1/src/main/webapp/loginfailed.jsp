<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Information Incorrect</title>
</head>
<body>
<h1>Please Try Again</h1>
<p>Either your user name or password is incorrect. Please try again.</p>
<form action="LoginServlet" method="post">
    <label>User Name: </label>
    <input type="text" name="username"> <br><br>
    <label>Password: </label>
    <input type="password" name="password">
    <input type="submit" value="Login">
</form>
<br>
<a href="createaccount.jsp">Create New Account</a>
</body>
</html>