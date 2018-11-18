<%--
  Created by IntelliJ IDEA.
  User: sestr
  Date: 17.11.2018
  Time: 17:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>
<body>

<h1>Login</h1>

<a href="${pageContext.request.contextPath}/register">Register an account here</a> <br/><br/>

<div style="color: darkred; font-weight: 500; font-family: Lato">
    ${message}
</div>

<form action="${pageContext.request.contextPath}/login" method="post">
    <input type="text" name="username" value="${username}" placeholder="Username" required>
    <input type="password" name="password" placeholder="Password" required>
    <input type="submit" value="Login">
</form>

</body>
</html>
