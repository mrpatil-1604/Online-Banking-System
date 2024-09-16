<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="css/styles.css">
</head>
<body>
	<h1>Error</h1>
    <p><%= request.getAttribute("errorMessage") %></p>
    <a href="dashboard.jsp">Back to Dashboard</a>
</body>
</html>