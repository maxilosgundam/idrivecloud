<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="model.AccountClass" %>
<%
	if (session.getAttribute("accountSession")!=null) {
		
		AccountClass account = (AccountClass)session.getAttribute("accountSession");
		int type = account.getAccType();
		if(type == 1){
			response.sendRedirect("employee");
		}else if(type == 2){
			response.sendRedirect("manager");
		}else if(type == 3){
			response.sendRedirect("administrator");
		}
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 5.0 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="height:100%;">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="css/base.css" type="text/css">
<script src="https://www.google.com/recaptcha/api.js" async defer></script>
<title>iDrive</title>
</head>
<body style="background:url('http://iacademy.edu.ph/assets/themes/version2/images/portal-images/home-bg.jpg') no-repeat center center;background-size:cover;background-position:fixed;height:100%;">
<div class="container">
<div class="row">
  <div class="col-sm-4"></div>
  <div class="col-sm-4"></div>
  <div class="col-sm-4"></div>
</div>
<div class="row">
  <div class="col-md-4"></div>
  <div class="col-md-4">
<center><img style="width: 80px;margin-bottom: 10px;margin-top: 80px;" src="crest2.png"></center>
<center><img src="logo-white.png" alt="iDrive" style="width:300px;margin-bottom: 10px;"></center>
<div class="well">
	<%
		String error = (String)request.getAttribute("invalid");
		String success = (String)request.getAttribute("successMsg");
		String token = request.getParameter("token");
		System.out.println("they talk about the " + token);
		if (error != null) {

			out.println("<div class='alert alert-danger fade in'>"+error+"</div>"); 

		}
		else if(success!=null){
			out.println("<div class='alert alert-success fade in'>"+success+"</div>");
		}
	if(token == null)
	{
	%>

	<form action="emailchecker.html" method="post">
	<div class="form-group">
    	<label for="email">Find your account</label>
	    <input type="text" class="form-control" name="email" id="email" placeholder="E-Mail" required="required" >
  	</div>
  	<%
	}
	else
	{
		request.setAttribute(token, "token");
  	%>
  	<form action="changepassword.html" method="post">
	<div class="form-group">
    	<label for="password">Enter new password: </label>
	    <input type="password" class="form-control" name="password" id="password" placeholder="Enter Password" required="required" >
	    <label for="password2">Enter password again: </label>
	    <input type="password" class="form-control" name="password2" id="password2" placeholder="Enter Password Again" required="required">
	    <input type="hidden" class="form-control" name="token" id="token" value="${param.token}">
  	</div>
  	<%
	}
  	%>

  	<div class="form-group"> 
	    <center><button type="submit" class="btn btn-primary btn-block"style="text-transform: uppercase;font-weight: 700;color: #fff !important;background-color: #3c8dbc;border-radius: 0;">Submit</button></center>
  	</div>
	</form>
</div>
 </div>
  <div class="col-md-4"></div>
</div>
</div>
</body>
</html>