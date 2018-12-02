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

<h1>Upload file to your library</h1>

<a href="${pageContext.request.contextPath}/home">Homepage</a> <br/>

<h2>${message}</h2>

<div>
    <form action="${pageContext.request.contextPath}/upload" method="post" enctype="multipart/form-data">
        Encrypted file: <input type="file" name="file">
        Encryption key: <input type="text" name="encryptionKey">
        Encryption type:
        <select name="encryptionType">
            <option value="asymmetrical">Asymmetric</option>
            <option value="symmetrical">Symmetric</option>
        </select>
        <input type="submit" value="Upload">
    </form>
</div>

</body>
</html>
