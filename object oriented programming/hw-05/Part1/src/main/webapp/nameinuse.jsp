<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Create Account</title>
</head>
<body>
<h1>The Name <%= request.getParameter("username") %> Is Already In Use</h1>
<p>Please enter another name and password.</p>
<form action="CreateServlet" method="post">
    <label>User Name: </label>
    <input type="text" name="username"> <br><br>
    <label>Password: </label>
    <input type="password" name="password">
    <input type="submit" value="Register">
</form>
<br>
<a href="index.html">Already Have Account?</a>
</body>
</html>