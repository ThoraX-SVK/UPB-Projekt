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

<h1>Asymmetrical encryption</h1>

<h2>DISCLAIMER: Skúšajte so súbormi malej veľkosti!</h2>

<a href="${pageContext.request.contextPath}/home">Homepage</a> <br/>

<h2>${message}</h2>

<div>
    <form action="${pageContext.request.contextPath}/asymmetricEncrypt" method="post" enctype="multipart/form-data">
        Public key: <input type="file" name="public_key">
        File: <input type="file" name="file">
        <input type="submit" value="Encrypt asymmetrically">
    </form>
</div>

</body>
</html>
