<%@ page import="model.ReservationViewer" %>
<%@ page import="model.AccountClass" %>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%
	if (session.getAttribute("accountSession")==null) {
		
		request.setAttribute("invalid", "Please login your account");
		request.getRequestDispatcher("login").forward(request,response);

	}else{
		AccountClass account = (AccountClass)session.getAttribute("accountSession");
		if(account.getAccType()!=3){
			request.setAttribute("invalid", "Unauthorized access");
			request.getRequestDispatcher("login").forward(request,response);
		}
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	    response.setDateHeader("Expires", 0);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 5.0 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="css/base.css" rel="stylesheet" type="text/css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script type="text/javascript">
$(document).ready(function(){
    $('a[data-toggle="tab"]').on('show.bs.tab', function(e) {
        localStorage.setItem('activeTab', $(e.target).attr('href'));
    });
    var activeTab = localStorage.getItem('activeTab');
    if(activeTab){
        $('#myTab a[href="' + activeTab + '"]').tab('show');
    }
});
</script>
<title>iDrive</title>
</head>
<body>
<div class="navbar navbar-default navbar-static-top">
<div class="container">
	<div class="container-fluid">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
		        <span class="icon-bar"></span>
		        <span class="icon-bar"></span>
		        <span class="icon-bar"></span> 
	      	</button>
			<a class="navbar-brand"	><img style="width: 36px;margin-top: -10px;margin-left: -15px" src="crest.png"><img src="logo-white.png" alt="iDrive" style="width:130px;margin-bottom: 15px;margin-top: -40px;margin-left: 30px;"></a>
		</div>
		<div class="collapse navbar-collapse" id="myNavbar">
			<%
				String first2 = account.getfName();
				String last2 = account.getlName();
				String fullName2 = first2+" "+last2;
			%>
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown" href="#"><%out.println(fullName2); %> <span class="caret"></span></a>
					<ul class="dropdown-menu">
						<li><a href="change.jsp">Change Password</a></li>
						<li><a href="#">Help</a></li>
						<li><a href="logoutservlet.html">Logout</a></li>
					</ul>
				</li>
			</ul>
		</div>
	</div>
	</div>
</div>
<div class="container">
<ul class="nav nav-tabs">
	<li class="active"><a data-toggle="tab" href="#changePass">Change Password</a></li>
  </ul>
  <br>
	<h5><b>CHANGE PASSWORD</b></h5>
	<hr style="border-color: #014FB3;">
	<%
		String error = (String)request.getAttribute("invalid");
		if (error != null) {
			out.println("<div class='alert alert-danger fade in'>"+error+"</div>"); 
		}
	%>
	<form class="form-vertical" action="passwordchange.html" method="post"><!-- Lagayan ng form action -->
		<div class="form-group">
			<label for="oldPass">Old Password:</label>
				<input type="password" id="oldPass" class="form-control" name="oldPass" required="required" value="${param.tripDate}" placeholder="Old Password"/>
		</div>

		<div class="form-group">
		<label for="newPass">New Password:</label>
			<input type="password" id="newPass" class="form-control" name="newPass" required="required" value="${param.tripDate}" placeholder="New Password"/>
		</div>
		
		<div class="form-group">
		<label for="newPass2">Confirm New Password:</label>
			<input type="password" id="newPass2" class="form-control" name="newPass2" required="required" value="${param.tripDate}" placeholder="Confirm Password"/>
		</div>
		
		<div class="form-group">
			<button type="submit" class="btn btn-success" style="border-color: #52a55f;text-transform: uppercase;font-weight: 700;color: #fff !important;background-color: #52a55f;border-radius: 0;">
				Submit
			</button>
			<a href="administrator.jsp" class="btn btn-primary" role="button">Back</a>
		</div>
	</form>
</div>
<div class="push"></div>
<footer class ="footer">
<div class="container">
	<div style="width:150px;margin:0 auto;position: relative;margin-bottom: -13rem;" class="hidden-xs">
	    <a href="http://iacademy.edu.ph/">
	    <img class="img-responsive" src="http://iacademy.edu.ph/assets/themes/version2/images/seal-small.png"></a>
	</div>
	<hr style="border-color: #00000;">
	<div class="row">
	    <div class="col-md-4" style="text-align:left;">
	        <span class="copyright">Copyright © iACADEMY 2017</span>
	    </div>
	    
	</div>
</div>
</footer>
</body>
</html>
<%
	}
%>
