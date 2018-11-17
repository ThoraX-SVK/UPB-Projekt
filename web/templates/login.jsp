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

<h2>${message}</h2>

<form action="${pageContext.request.contextPath}/login" method="post">
    <input type="text" name="username" value="${username}" placeholder="Username" required>
    <input type="password" name="password" placeholder="Password" required>
    <input type="submit" value="Login">
</form>

</body>
</html>
