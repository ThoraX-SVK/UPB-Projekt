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

<h1>Symmetrical decryption</h1>

<h2>DISCLAIMER: Skúšajte so súbormi malej veľkosti!</h2>

<a href="${pageContext.request.contextPath}/home">Homepage</a> <br/>

<h2>${message}</h2>

<div>
    <form action="${pageContext.request.contextPath}/decrypt" method="post" enctype="multipart/form-data">
        Key: <input type="file" name="file">
        Encrypted file: <input type="file" name="key">
        <input type="submit" value="Decrypt symmetrically">
    </form>
</div>

</body>
</html>
