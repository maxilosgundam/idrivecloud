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
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 5.0 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="height:100%;">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="css/base.css" type="text/css">
<link rel="shortcut icon" href="/favicon.ico" type="image/x-icon">
<link rel="icon" href="/favicon.ico" type="image/x-icon">
<script src="https://www.google.com/recaptcha/api.js" async defer></script>
<title>iDrive</title>
</head>
<%
	response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);
%>
<script type="text/javascript">
	localStorage.clear();
</script>
<body style="background:url('http://iacademy.edu.ph/assets/themes/version2/images/portal-images/home-bg.jpg') no-repeat center center fixed;-webkit-background-size: cover;
  -moz-background-size: cover;
  -o-background-size: cover;
  background-size: cover;">
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
		String change = (String)request.getAttribute("change");
		if (error != null) {
	
			out.println("<div class='alert alert-danger fade in'>"+error+"</div>"); 
	
		}
		else if(change != null){
			out.println("<div class='alert alert-success fade in'>"+change+"</div>"); 
		}
	%>
	<form action="accountchecker.html" method="post">
	<div class="form-group">
    	<label for="username">Username:</label>
	    <input type="text" class="form-control" name="username" id="username" placeholder="Employee I.D." required="required" >
  	</div>
  	
  	<div class="form-group">
    	<label for="password">Password:</label>
	    <input type="password" class="form-control" name="password" id="password" placeholder="Password"required="required">
  	</div>
  	<div class="form-group">
        <center><div class="g-recaptcha" data-sitekey="6LfQNwwUAAAAAFAkuWhLKP_cDeCORsHW1vswPxcO" ></div></center>
        </div>
  	<div class="form-group">
		<a href="forgot.jsp"><b>Forgot your password?</b></a></div>
  	<div class="form-group"> 
	    <center><button type="submit" class="btn btn-primary btn-block"style="text-transform: uppercase;font-weight: 700;color: #fff !important;background-color: #3c8dbc;border-radius: 0;">Login</button></center>
  	</div>
	</form>
</div>
 </div>
  <div class="col-md-4"></div>
</div>
</div>
</body>
</html>