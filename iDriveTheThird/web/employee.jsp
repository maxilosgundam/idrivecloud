<%@ page import="model.ReservationViewer" %>
<%@ page import="model.AccountClass" %>
<%@ page import="model.VehicleClass" %>

<%@ page import="java.util.List" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="util.SessionHandlers" %>
<%@ page import="java.sql.Connection" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%
	if (session.getAttribute("accountSession")==null) {
		
		request.setAttribute("invalid", "Please login your account");
		request.getRequestDispatcher("login").forward(request,response);
	}else{
		AccountClass account = (AccountClass)session.getAttribute("accountSession");
		if(account.getAccType()!=1){
			request.setAttribute("invalid", "Unauthorized access");
			request.getRequestDispatcher("login").forward(request,response);
		}
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	    response.setDateHeader("Expires", 0);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 5.0 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="css/base.css" rel="stylesheet" type="text/css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="jquery.dataTables.min.css">
<script type="text/javascript" src="jquery.dataTables.min.js"></script>
<script type="text/javascript" src="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
<script type="text/javascript">
$(document).ready(function(){
    $('a[data-toggle="tab"]').on('show.bs.tab', function(e) {
        localStorage.setItem('activeTab', $(e.target).attr('href'));
    });
    var activeTab = localStorage.getItem('activeTab');
    if(activeTab){
        $('#tabs a[href="' + activeTab + '"]').tab('show');
    }
});
$(document).ready(function() {
    $('#reservationsTable').DataTable();
} );
$(document).ready(function() {
    $('#carTable').DataTable();
} );
</script>
<title>iDrive</title>
</head>
<body>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
<!-- <img src="logo.png" alt="iDrive" style="width:250px;margin-bottom: 15px;margin-top: -30px;"> -->
<ul class="nav nav-tabs" id="tabs">
	<li class="active"><a data-toggle="tab" href="#profile">Profile</a></li>
    <li><a data-toggle="tab" href="#trip">Reserve a Trip</a></li>
    <li><a data-toggle="tab" href="#reservations">Reservations</a></li>
    <li><a data-toggle="tab" href="#vehicles">Vehicles</a></li>
  </ul>
  <br>
<div class="tab-content">
	<div id="trip" class="tab-pane fade">
	<h5><b>TRIP TICKET FORM</b></h5>
	<hr style="border-color: #014FB3;">
	<%
		String error = (String)request.getAttribute("errorMsg");
		String success = (String)request.getAttribute("successMsg");
		if (error != null) {
			out.println("<div class='alert alert-danger fade in'>"+error+"</div>"); 
		}else if(success!=null){
			out.println("<div class='alert alert-success fade in'>"+success+"</div>");
		}
	%>
	<form class="form-vertical" action="sendreservation.html" method="post">
		<div class="form-group">
			<label for="tripDate">Trip Date:</label>
				<input type="date" id="tripDate" class="form-control" name="tripDate" required="required" value="${fn:escapeXml(param.tripDate)}"/>
		</div>
		<div class="form-group">
		<label for="hours">Departure:</label></div>
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
			</select></div></div>
		<div class="form-group">
		<label for="destination">Destination:</label>
				<input type="text" id="destination" pattern="[A-Za-z0-9 ]+{50}"maxlength="50" class="form-control" name="destination" required="required"
				placeholder="Destination of travel" value="${fn:escapeXml(param.destination)}" />
		</div>
		<div class="form-group">
		<label for="travelPurpose">Purpose of Travel:</label>
				<textarea id="travelPurpose" class="form-control" pattern="[A-Za-z0-9 .!?,$&-]+{500}" name="travelPurpose" required = "required" rows="3" placeholder="Write a short reason for your reservation" maxlength="500">${fn:escapeXml(param.travelPurpose)}</textarea>
		</div>
		<div class="form-group">
		<label for="passengers">Passengers (Optional):</label>
				<input type="text" id="passengers" class="form-control" pattern="[A-Za-z .,]+" name="passengers" 
				placeholder="(e.g., John Doe, Jane Doe, etc.)" value="${fn:escapeXml(param.passengers)}" />
		</div>
		<div class="form-group">
		<label for="numPassengers">Number of Passengers:</label>
				<input type="number" id="numPassengers" min="1" max="25" pattern="[0-9]+{2}"class="form-control" name="numPassengers" 
				placeholder="Total number of included passengers" value="${fn:escapeXml(param.numPassengers)}" />
		</div>
		<div class="form-group">
			<button type="submit" class="btn btn-success" style="border-color: #52a55f;text-transform: uppercase;font-weight: 700;color: #fff !important;background-color: #52a55f;border-radius: 0;">
				Submit
			</button>
		</div>
	</form>
	</div>
	<div id="profile" class="tab-pane fade in active">
	<h5><b>PROFILE</b></h5>
	<hr style="border-color: #014FB3;">
	<div class="well">
		<%
			String first = account.getfName();
			String last = account.getlName();
			String fullName = first+" "+last;
			out.println("<h3>"+fullName+"</br>"+"<small>Employee</small>"+"</h3>");
		%>
		
	</div>
	</div>
	<div id="reservations" class="tab-pane fade">
	<h5><b>RESERVATIONS</b></h5>
	<hr style="border-color: #014FB3;">
	
	<table class="table table-hover" id="reservationsTable">
		<thead style="background: #014FB3;">
      	<tr>
      	
        <th><center><font color="white">#</font></center></th>
        <th><center><font color="white">Date</font></center></th>
        <th><center><font color="white">Departure</font></center></th>
        <th><center><font color="white">Destination</font></center></th>
        <th><center><font color="white">Tracker</font></center></th>
        <th></th>
      </tr>
    </thead>
    <tbody>
    <% 
    List<ReservationViewer> approving = (List<ReservationViewer>) session.getAttribute("approved");
	for(int ctr = 0; ctr < approving.size(); ctr++){
    %>
    <tr>
        <td align="center"><%=approving.get(ctr).getReservationIDEmp() %></td>
        <td align="center"><%=approving.get(ctr).getTripDateEmp() %></td>
        <td align="center"><%=approving.get(ctr).getDepartureEmp() %></td>
        <td align="center"><%=approving.get(ctr).getDestinationEmp() %></td>
        <td align="center"><%=approving.get(ctr).getTrackWord() %></td>
        <td align="center">
        	<form role="form-vertical" action="viewreservationservlet.html" method="post">
        		<button type="submit" name ="resID" id="resID" class="btn btn-primary" value="<%=approving.get(ctr).getReservationIDEmp() %>">View</button>
        	</form>
        </td>
      </tr>

    <%
	}
    %>
    </tbody>
		</table>
	
	</div>
	<div id="vehicles" class="tab-pane fade">
	<h5><b>VEHICLES</b></h5>
	<hr style="border-color: #014FB3;">
	<%
		Connection connection = (Connection) getServletContext().getAttribute("dbConnection");

		SessionHandlers handler = new SessionHandlers();
		List<VehicleClass> cars = handler.empCarSession(connection);
	%>
		<table class="table table-hover" id="carTable">
			<thead style="background: #014FB3;">
			<tr>
				<th><center><font color="white">PLATE NO.</font></center></th>
				<th><center><font color="white">MODEL</font></center></th>
				<th><center><font color="white">STATUS</font></center></th>
				<th><center><font color="white">AVAILABILITY FOR TODAY</font></center></th>
			</tr>
			</thead>
		
		<tbody>
	    <% 
			
			for(int ctr = 0; ctr < cars.size(); ctr++){
		%>
	      <tr>
	        <td align="center"><%=cars.get(ctr).getPlateNum() %></td>
	        <td align="center"><%=cars.get(ctr).getYearMake() %> <%=cars.get(ctr).getColor()%> <%=cars.get(ctr).getModel() %></td>
	        <td align="center"><%=cars.get(ctr).getStatusName() %></td>
	        <td align="center"><%=cars.get(ctr).getAvailability() %></td>
	      </tr>
	    <%
	      }
	    %>
	    </tbody>
	  </table>
	</div>
	</div>
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