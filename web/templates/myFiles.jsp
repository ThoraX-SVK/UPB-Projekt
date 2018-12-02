<%--
  Created by IntelliJ IDEA.
  User: timbe
  Date: 3.11.2018
  Time: 17:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Homepage</title>
</head>

<body>

<h1>Homepage</h1>

<div>Logged in as: ${sessionScope.username}</div>
&nbsp;
<a href="${pageContext.request.contextPath}/logout">Logout</a>


</body>
</html>
