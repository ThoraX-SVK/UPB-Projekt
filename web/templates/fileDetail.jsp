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

<br><br>

<div>Owners of file:</div>
<c:forEach items="${owners}" var="element">
    <div class="owners-wrapper">
        &nbsp;${element}
    </div>
</c:forEach>

<br>

<div>Guests (Viewers & commenters) of file:</div>
<c:forEach items="${guests}" var="element">
    <div class="owners-wrapper">
        &nbsp;${element}
    </div>
</c:forEach>

<c:choose>
    <c:when test="${isUserOwner}">
        <br/><br/>
        <div>Add privilege to User (Owner or Guest): <br/>
            <form action="${pageContext.request.contextPath}/privilege?fileId=${file.getFileId()}" method="post">
                <select name="selectedUser" required>
                        <%-- options of users - username is basically user id --%>
                    <c:forEach items="${allUsers}" var="username">
                        <option value="${username}">${username}</option>
                    </c:forEach>
                </select>

                <select name="relationshipType" required>
                    <option value="OWNER">Owner</option>
                    <option value="GUEST">Guest</option>
                </select>

                <input type="submit" value="Process">
            </form>
        </div>
    </c:when>
</c:choose>

<br/>
<div>Comments:</div>
<c:forEach items="${comments}" var="element">
    <div class="comment-wrapper">
        <h4 class="comment-title">${element.getTitle()} - ${element.getPublishDate()}</h4>
        <p>${element.getContent()}</p>
        <p>- submitted by: ${element.getAuthorUsername()}</p>
    </div>
</c:forEach>

<form method="post" action="${pageContext.request.contextPath}/submit?fileId=${file.getFileId()}" class="submit-new-comment-form">
    <input type="text" name="title"> <br/>
    <textarea name="content"></textarea> <br/>
    <input type="submit" value="Submit comment">
</form>

</body>
</html>
