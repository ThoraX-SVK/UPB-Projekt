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
    <title>${file.getFileName()}</title>
</head>

<body>

<h1>File: ${file.getFileName()}</h1>

<div style="color: darkred; font-weight: bold">${message}</div>

<div>Logged in as: ${sessionScope.username}</div>
&nbsp;
<a href="${pageContext.request.contextPath}/logout">Logout</a>

<br/><br/>
<h3>File info:</h3>
<div>${file.getEncryptionKey()}</div>
<div>${file.getEncryptionType()}</div>

<c:forEach items="${comments}" var="element">
    <div class="comment-wrapper">
        &nbsp;
        <h4 class="comment-title">${element.getTitle()} - ${element.getPublishDate()}</h4>
        <p>${element.getContent()}</p>
        <p>- submitted by: ${element.getAuthorUsername()}</p>
    </div> <br/>
</c:forEach>

<form method="post" action="${pageContext.request.contextPath}/submit?fileId=${file.getFileId()}" class="submit-new-comment-form">
    <input type="text" name="title"> <br/>
    <textarea name="content"></textarea> <br/>
    <input type="submit" value="Submit comment">
</form>

</body>
</html>
