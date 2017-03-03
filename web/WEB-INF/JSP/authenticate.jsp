<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<form action ="<%=request.getContextPath()%>/RestoreDBServlet" method="post">
	<input type="text" class="form-control" name="username" id="username" placeholder="Employee I.D." required="required" >
	<input type="password" class="form-control" name="password" id="password" placeholder="Password"required="required">
	<button type="submit">Login</button>
</form>
</body>
</html>