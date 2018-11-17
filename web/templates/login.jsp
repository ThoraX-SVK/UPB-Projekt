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
    <title>Title</title>
</head>
<body>

<form action="${pageContext.request.contextPath}/login" method="post">
    <input type="text" name="username" placeholder="Username">
    <input type="passAndSalt" name="passAndSalt" placeholder="Password">
    <input type="submit" value="Login">
</form>

</body>
</html>
