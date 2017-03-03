<%@ page import="model.ReservationViewer" %>
<%@ page import="model.AccountClass" %>
<%@ page import="util.SessionHandlers" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%
	if (request.getParameter("token")==null) {
		
		request.setAttribute("invalid", "Cannot be accessed");
		request.getRequestDispatcher("login").forward(request,response);
	
	}else{
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	    response.setDateHeader("Expires", 0);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 5.0 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head style="height:100%;">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<style>
  .modal-header, h4, .close {
      background-color: #014FB3;
      color:white !important;
      text-align: center;
      font-size: 30px;
  }
  .modal-footer {
      background-color: #f9f9f9;
  }
</style>
<link rel="stylesheet" href="css/base.css" type="text/css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script type="text/javascript">
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
			<a class="navbar-brand" href="login.jsp"><img style="width: 36px;margin-top: -10px;margin-left: -15px" src="crest.png"><img src="logo-white.png" alt="iDrive" style="width:130px;margin-bottom: 15px;margin-top: -40px;margin-left: 30px;"></a>
		</div>

	</div>
	</div>
</div>
<%
	//Connection connection = (Connection) getServletContext().getAttribute("dbConnection");
	//String token = request.getParameter("token");
	//System.out.println(token);
	//SessionHandlers handler = new SessionHandlers();
	List<ReservationViewer> viewEach = (List<ReservationViewer>)request.getAttribute("reservationDetails");
%>
<div class="container">
<ul class="nav nav-tabs">
	<li class="active"><a data-toggle="tab" href="#view">View Reservation</a></li>
</ul>
<br>
	<h5><b>RESERVATION DETAILS</b></h5>
	<hr style="border-color: #014FB3;">
	<div class="row">
		<div class="col-sm-6">
			<div class="form-group">
				<label for="requester"><b>Requester:</b></label>
				<h5 id="requester"><%=viewEach.get(0).getName()%></h5>
			</div>
			<div class="form-group">
				<label for="departure"><b>Departure Time:</b></label>
				<h5 id="departure"><%=viewEach.get(0).getDeparture()%></h5>
			</div>
		</div>
		<div class="col-sm-6">
			<div class="form-group">
				<label for="trip"><b>Trip Date:</b></label>
				<h5 id="trip"><%=viewEach.get(0).getTripDate()%></h5>
			</div>
			<div class="form-group">
				<label for="destination"><b>Destination:</b></label>
				<h5 id="destination"><%=viewEach.get(0).getDestination()%></h5>
			</div>
		</div>
	</div>
			<div class="form-group">
				<label for="purpose"><b>Purpose of Travel:</b></label>
				<h5 id="purpose"><%=viewEach.get(0).getPurpose()%></h5>
			</div>
         
	<div class="row">
		<div class="col-sm-6">
			<div class="form-group">
				<label for="passengersNum"><b>Passengers Included:</b></label>
				<h5 id="passengersNum"><%=viewEach.get(0).getPassengers()%></h5>
			</div>
		</div>
		<div class="col-sm-6">
			<div class="form-group">
				<label for="passengers"><b>Total Passengers:</b></label>
				<h5 id="passengers"><%=viewEach.get(0).getPassengerNum()%></h5>
			</div>
		</div>
	</div>
	<div class="row">
		<div class="col-sm-6">
			<div class="form-group">
				<label for="driver"><b>Driver Assigned:</b></label>
				<h5 id="driver"><%=viewEach.get(0).getDriverName()%></h5>
			</div>
		</div>
		<div class="col-sm-6">
			<div class="col-sm-6">
				<div class="form-group">
					<label for="vehicle"><b>Vehicle Assigned:</b></label>
					<h5 id="vehicle"><%=viewEach.get(0).getCarName()%></h5>
				</div>
			</div>
		</div>
	</div>
</div>
</body>
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
</html>
<%
	}
%>
