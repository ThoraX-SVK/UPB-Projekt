<%--
  Created by IntelliJ IDEA.
  User: timbe
  Date: 3.11.2018
  Time: 17:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Homepage</title>
</head>

<body>

<h1>Homepage</h1>

<div>Logged in as: ${sessionScope.username}</div>
&nbsp;
<a href="${pageContext.request.contextPath}/logout">Logout</a>

<br/>

<h2>Library</h2>
<a href="${pageContext.request.contextPath}/myFiles">My files</a>

<h2>Symmetrical cryptography: </h2>
<a href="${pageContext.request.contextPath}/encrypt">symmetric encrypt</a> <br/>
<a href="${pageContext.request.contextPath}/decrypt">symmetric decrypt</a> <br/>

<h2>Asymmetrical cryptography: </h2>
<a href="${pageContext.request.contextPath}/generateKeyPair">generate key pair</a> <br/>
<a href="${pageContext.request.contextPath}/asymmetricEncrypt">asymmetric encrypt</a> <br/>
<a href="${pageContext.request.contextPath}/asymmetricDecrypt">asymmetric decrypt</a> <br/>

<h2>Offline decryption: </h2>
<a href="${pageContext.request.contextPath}/offlineDecrypt">Download .jar</a> <br/>

<br/><br/>
<div style="font-size: small">Authors: Bezák T., Krasoň T., Sestrienka L.</div>
</body>
</html>
