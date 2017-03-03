<%@ page import="model.FinalizeReservation" %>
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
<%
	List<FinalizeReservation> cars = (List<FinalizeReservation>) session.getAttribute("availCars");
	List<FinalizeReservation> driver = (List<FinalizeReservation>) session.getAttribute("availDrivers");
	List<ReservationViewer> viewEach = (List<ReservationViewer>) session.getAttribute("viewEach");
%>
<div class="container">
<ul class="nav nav-tabs">
	<li class="active"><a data-toggle="tab" href="#view">View Reservation</a></li>
</ul>
<br>
		<h5><b>ASSIGN VEHICLE AND CAR</b></h5>
		<hr style="border-color: #014FB3;">
		<form role="form-vertical" action="assigndriverandvehicleservlet.html" method="post">
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
							<label for="driver"><b>Assign Driver:</b></label>
							<select id="driver" class="form-control" name="driver"required="required">
								<%
									for(int ctr = 0; ctr < driver.size(); ctr++){
								%>
										<option value=<%=driver.get(ctr).getLicenseID() %>><%=driver.get(ctr).getDriverName()%></option>
								<%
									};
								%>
							</select>
						</div>
				</div>
				<div class="col-sm-6">
						<div class="form-group">
							<label for="cars"><b>Assign Car:</b></label>
							<select id="cars" class="form-control" name="cars" rows="3"required="required" >
								<%
									for(int ctr = 0; ctr < cars.size(); ctr++){
								%>
										<option value=<%=cars.get(ctr).getCarID() %>><%=cars.get(ctr).getCarName()%></option>
								<%
									};
								%>
							</select>
						</div>
				</div>
			</div>
			<div class="form-group">
			<button type="submit" class="btn btn-success">
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
