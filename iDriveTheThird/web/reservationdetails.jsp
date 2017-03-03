<%@ page import="model.ReservationViewer" %>
<%@ page import="model.AccountClass" %>
<%@ page import="util.SessionHandlers" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%
	if (request.getAttribute("reservationDetails")==null) {
		request.setAttribute("invalid", "Cannot be accessed");
		request.getRequestDispatcher("login").forward(request,response);
	
	}else{
		AccountClass account = (AccountClass)session.getAttribute("accountSession");
		if(account.getAccType()==3){
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
	<div class="row">
		<div class="col-sm-6">
			<button type="button" class="btn btn-warning" data-toggle="modal" data-target="#moveModal">Move</button>
		</div>
		<div class="col-sm-6">
			<button type="button" class="btn btn-danger" data-toggle="modal" data-target="#cancelModal">Cancel</button>
		</div>
	</div>
	<div class="modal fade" id="cancelModal" role="dialog">
		<div class="modal-dialog">
			<div class="modal-content">
	  			<div class="modal-header" style="padding:15px 50px;">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4>Reservation #<%=viewEach.get(0).getReservationID() %></h4>
				</div>
				<div class="modal-body" style="padding:50px 50px;">
					<center><h5 style="padding:80px 50px;"><b>Are you sure you want to cancel this reservation?</b></h5></center>
					<div class="row">
						<div class="col-sm-6">
							<a href="cancel.html?resID=<%=viewEach.get(0).getReservationID() %>" role="button" class="btn btn-success btn-block" style="border-color: #52a55f;text-transform: uppercase;font-weight: 700;color: #fff !important;background-color: #52a55f;border-radius: 0;">Yes</a>
						</div>
						<div class="col-sm-6">
							<button type="button" data-dismiss="modal"class="btn btn-danger btn-block" style="border-color: #c44a4a;text-transform: uppercase;font-weight: 700;color: #fff !important;background-color: #c44a4a;border-radius: 0;">No</button>
						</div>
					</div>
				</div>
	    	</div>
		</div>
	</div>
	<div class="modal fade" id="moveModal" role="dialog">
		<div class="modal-dialog">
			<div class="modal-content">
	  			<div class="modal-header" style="padding:15px 50px;">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4>Reservation #<%=viewEach.get(0).getReservationID() %></h4>
				</div>
				<div class="modal-body" style="padding:50px 50px;">
					<form class="form-vertical" action="movereservation.html" method="post">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group">
								<label for="tripDate">Trip Date:</label>
									<input type="date" id="tripDate" class="form-control" name="tripDate" required="required"/>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label for="hours">Departure:</label>
								</div>
								<div class="form-group">
									<div class="input-group">
										<select name="hours" id="hours" class="form-control"required="required">
											<option value="01" ${param.hours == '01' ? 'selected' : ''}>01</option>
											<option value="02" ${param.hours == '02' ? 'selected' : ''}>02</option>
											<option value="03" ${param.hours == '03' ? 'selected' : ''}>03</option>
											<option value="04" ${param.hours == '04' ? 'selected' : ''}>04</option>
											<option value="05" ${param.hours == '05' ? 'selected' : ''}>05</option>
											<option value="06" ${param.hours == '06' ? 'selected' : ''}>06</option>
											<option value="07" ${param.hours == '07' ? 'selected' : ''}>07</option>
											<option value="08" ${param.hours == '08' ? 'selected' : ''}>08</option>
											<option value="09" ${param.hours == '09' ? 'selected' : ''}>09</option>
											<option value="10" ${param.hours == '10' ? 'selected' : ''}>10</option>
											<option value="11" ${param.hours == '11' ? 'selected' : ''}>11</option>
											<option value="12" ${param.hours == '12' ? 'selected' : ''}>12</option>
										</select>
										<span class="input-group-addon">:</span>
										
										<select name="minutes" id="minutes" class="form-control" required="required">
											<option value="00" ${param.minutes == '00' ? 'selected' : ''}>00</option>
											<option value="05" ${param.minutes == '05' ? 'selected' : ''}>05</option>
											<option value="10" ${param.minutes == '10' ? 'selected' : ''}>10</option>
											<option value="15" ${param.minutes == '15' ? 'selected' : ''}>15</option>
											<option value="20" ${param.minutes == '20' ? 'selected' : ''}>20</option>
											<option value="25" ${param.minutes == '25' ? 'selected' : ''}>25</option>
											<option value="30" ${param.minutes == '30' ? 'selected' : ''}>30</option>
											<option value="35" ${param.minutes == '35' ? 'selected' : ''}>35</option>
											<option value="40" ${param.minutes == '40' ? 'selected' : ''}>40</option>
											<option value="45" ${param.minutes == '45' ? 'selected' : ''}>45</option>
											<option value="50" ${param.minutes == '50' ? 'selected' : ''}>50</option>
											<option value="55" ${param.minutes == '55' ? 'selected' : ''}>55</option>
										</select>
										<span class="input-group-addon"> </span>
										<select name="time" id="time" class="form-control" required="required">
											<option value="AM" ${param.time == 'AM' ? 'selected' : ''}>AM</option>
											<option value="PM" ${param.time == 'PM' ? 'selected' : ''}>PM</option>
										</select>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<button value="<%=viewEach.get(0).getReservationID() %>" id="resID" name="resID" type="submit" class="btn btn-success btn-block" style="border-color: #52a55f;text-transform: uppercase;font-weight: 700;color: #fff !important;background-color: #52a55f;border-radius: 0;">Submit</button>
							</div>
							<div class="col-sm-6">
								<button type="button" data-dismiss="modal"class="btn btn-danger btn-block" style="border-color: #c44a4a;text-transform: uppercase;font-weight: 700;color: #fff !important;background-color: #c44a4a;border-radius: 0;">Cancel</button>
							</div>
						</div>
					</form>
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