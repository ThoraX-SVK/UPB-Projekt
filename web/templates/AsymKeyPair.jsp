<%--
  Created by IntelliJ IDEA.
  User: timbe
  Date: 3.11.2018
  Time: 18:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>File upload</title>
</head>
<body>

<h1>Asymmetrical crypto: generate key pair</h1>

<a href="${pageContext.request.contextPath}/home">Homepage</a> <br/>

<h2>${message}</h2>

<div>
    <form action="${pageContext.request.contextPath}/generateKeyPair" method="post">
        <input type="submit" value="Generate key pair">
    </form>
</div>

</body>
</html>
