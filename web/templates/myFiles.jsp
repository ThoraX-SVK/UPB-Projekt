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
    <title>My files</title>
</head>

<body>

<h1>My Files</h1>

<div>Logged in as: ${sessionScope.username}</div>
&nbsp;
<a href="${pageContext.request.contextPath}/logout">Logout</a>

<br/>
<form action="${pageContext.request.contextPath}/myFiles" method="get">
    <input style="width: 150px;" type="text" name="search" value="${pageContext.request.getParameter("search")}">
    <input type="submit" value="Search">
</form>

<br/><br/>
<h2>Owned files</h2>
<c:forEach items="${ownFiles}" var="element">
    <div class="file-wrapper">
        <a href="${pageContext.request.contextPath}/detail?fileId=${element.getFileId()}">
            ${element.getFileName()}
        </a>

        &nbsp;
        <a href="${pageContext.request.contextPath}/download/${element.getFileId()}">
                download file
        </a>

        &nbsp;
        <span>${element.getEncryptionKey()}</span> &nbsp;
        <span>${element.getEncryptionType()}</span> &nbsp;&nbsp;
    </div> <br/>
</c:forEach>

<h2>Guest files</h2>
<c:forEach items="${guestFiles}" var="element">
    <div class="file-wrapper">
        <a href="${pageContext.request.contextPath}/detail?fileId=${element.getFileId()}">
            ${element.getFileName()}
        </a>

        &nbsp;
        <a href="${pageContext.request.contextPath}/download/${element.getFileId()}">
                download file
        </a>

        &nbsp;
        <span>${element.getEncryptionKey()}</span> &nbsp;
        <span>${element.getEncryptionType()}</span> &nbsp;&nbsp;
    </div> <br/>
</c:forEach>

<div class="upload-file-wrapper">
    <a href="${pageContext.request.contextPath}/upload">
        Upload new file
    </a>
</div>

</body>
</html>
