<%@ page import="model.ReservationViewer" %>
<%@ page import="model.DriverClass" %>
<%@ page import="model.VehicleClass" %>
<%@ page import="model.DepartmentClass" %>
<%@ page import="model.AccountClass" %>
<%@ page import="model.ImageClass" %>
<%@ page import="model.EventModel" %>
<%@ page import="util.EventMaker" %>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.ResultSet" %>

<%@ page import="java.sql.Connection" %>
<%@ page import="util.SessionHandlers" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.io.*" %>
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
	    
	    Connection connection = (Connection) getServletContext().getAttribute("dbConnection");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 5.0 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head style="height:100%;">
<meta name="viewport" content="width=device-width, initial-scale=1">
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
<link rel="stylesheet" href="css/fullcalendar.css">
<script src="js/moment.min.js"></script>
<script src="js/fullcalendar.js"></script>
<link rel="stylesheet" href="jquery.dataTables.min.css">
<script type="text/javascript" src="jquery.dataTables.min.js"></script>
<script type="text/javascript" src="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
<script type="text/javascript">
var myVar;

function showPage() {
  document.getElementById("loader").style.display = "none";
  document.getElementById("page").style.display = "block";
}
function maxLimit(){
	var d = new Date();
	document.getElementById("yearMake").max = d.getFullYear();
	myVar = setTimeout(showPage, 800);
}
</script>
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
    $('#pendingTable').DataTable();
} );

$(document).ready(function() {
    $('#driverTable').DataTable();
} );

$(document).ready(function() {
    $('#carTable').DataTable();
} );

$(document).ready(function(){
	$('#deptTable').DataTable();
});

$(document).ready(function() {
	
	$('#calendar').fullCalendar({
    	header: {
			left: 'prev,next today',
			center: 'title',
			right: 'month,agendaWeek,agendaDay'
		},
		defaultView: 'month'
    });
	<%
		EventMaker em = new EventMaker();
		List<EventModel> eventList = em.getEvents(connection);
	%>
	for(ctr = 0; ctr < 8; ctr++){
		var event={id:ctr , title: 'New event'+ctr, start: '2017-02-0'+ctr};
		$('#calendar').fullCalendar( 'renderEvent', event, true);
	    
	}
	
});
</script>
<title>iDrive</title>
</head>
<body onload="maxLimit()">
<div class="wrapper">
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
<%
	
	
	SessionHandlers handler = new SessionHandlers();
	List<ReservationViewer> pending = handler.adminReservationsSession(connection);
%>

<div class="container">
<ul class="nav nav-tabs" id="tabs">
	<li class="active"><a data-toggle="tab" href="#profile">Profile</a></li>
    <li><a data-toggle="tab" href="#pending">Pending Reservations <%if(pending.size()==0){}else{%><span class="label label-danger"><%=pending.size()%></span><%} %></a></li>
    <li><a data-toggle="tab" href="#departments">Departments</a></li>
    <li><a data-toggle="tab" href="#vehicles">Vehicles</a></li>
    <li><a data-toggle="tab" href="#drivers">Drivers</a></li>
    <li><a data-toggle="tab" href="#records">Records & Statistics</a></li> 
    <li><a data-toggle="tab" href="#csv">Accounts</a></li>
    <li><a data-toggle="tab" href="#emergency">Emergency</a></li> 
</ul>
<br>
<div id="loader"></div>
<div id="page">
<div class="tab-content">

	<div id="pending" class="tab-pane fade">
		<h5><b>PENDING RESERVATIONS</b></h5>
		<hr style="border-color: #014FB3;">
		<div class="form-group">
			<form>
			<button type="button" class="btn btn-primary" onClick="history.go(0)">Refresh Page</button>
			</form>
		</div>
		<div class="table-responsive">
		<div class="scrollable">
	<table class="table table-hover" id="pendingTable">
		<thead style="background: #014FB3;">
		<tr>
			<th><center><font color="white">#</font></center></th>
			<th><center><font color="white">REQUESTER</font></center></th>
			<th><center><font color="white">DATE</font></center></th>
			<th><center><font color="white">DEPARTURE</font></center></th>
			<th><center><font color="white">DESTINATION</font></center></th>
			<th></th>
		</tr>
		</thead>
	
	<tbody>
	
	
    <% 
		
		for(int ctr = 0; ctr < pending.size(); ctr++){
	%>
	
      <tr>
        <td align="center"><%=pending.get(ctr).getReservationID() %></td>
        <td align="center"><%=pending.get(ctr).getName() %></td>
        <td align="center"><%=pending.get(ctr).getTripDate() %></td>
        <td align="center"><%=pending.get(ctr).getDeparture() %></td>
        <td align="center"><%=pending.get(ctr).getDestination() %></td>
        <td align="center">
			<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#viewModal<%=pending.get(ctr).getReservationID() %>">View</button>
			</td>
      </tr>
     
  <div class="modal fade" id="viewModal<%=pending.get(ctr).getReservationID() %>" role="dialog">
    <div class="modal-dialog">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header" style="padding:15px 50px;">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4>Reservation # <%=pending.get(ctr).getReservationID() %></h4>
        </div>
        <div class="modal-body" style="padding:40px 50px;">
          <form role="form-vertical" action="viewreservationservlet.html" method="post">
			<div class="row">
				<div class="col-sm-6">
					<div class="form-group">
	              		<label><b>REQUESTER:</b></label>
	              		<p><%=pending.get(ctr).getName()%></p>
	            	</div>
					<div class="form-group">
						<label><b>DEPARTURE TIME:</b></label>
						<p><%=pending.get(ctr).getDeparture()%></p>
					</div>
            	</div>
				<div class="col-sm-6">
					<div class="form-group">
						<label><b>TRIP DATE:</b></label>
						<p><%=pending.get(ctr).getTripDate()%></p>
	           		</div>
					<div class="form-group">
						<label><b>DESTINATION:</b></label>
						<p><%=pending.get(ctr).getDestination()%></p>
	           		</div>
           		</div>
           		
			</div>

            <div class="form-group">
              <label><b>PURPOSE OF TRAVEL:</b></label>
              <p><%=pending.get(ctr).getPurpose()%></p>
            </div>
            
			<div class="row">
				<div class="col-sm-6">
					<div class="form-group">
	              		<label><b>PASSENGERS INCLUDED:</b></label>
	              		<p><%=pending.get(ctr).getPassengers()%></p>
	            	</div>
            	</div>
				<div class="col-sm-6">
					<div class="form-group">
						<label><b>TOTAL PASSENGERS:</b></label>
						<p><%=pending.get(ctr).getPassengerNum()%></p>
	           		</div>
           		</div>
           	<div class="form-group">
              <label><b>REASON FOR DENIAL:</b></label>
              	<textarea id="denialReason" class="form-control" name="denialReason" rows="3" placeholder="Write a short reason for denying the request" required="required" ></textarea>
            </div>
			</div>
			<button type="submit" name ="denied" id="denied" value="<%=pending.get(ctr).getReservationID() %>"class="btn btn-danger btn-block" style="border-color: #c44a4a;text-transform: uppercase;font-weight: 700;color: #fff !important;background-color: #c44a4a;border-radius: 0;">Deny</button>
          </form>
          <form role="form-vertical" action="tripfinalizationservlet.html" method="post">
				<button type="submit" name ="approved" id="approved" value="<%=pending.get(ctr).getReservationID() %>"class="btn btn-success btn-block" style="border-color: #52a55f;text-transform: uppercase;font-weight: 700;color: #fff !important;background-color: #52a55f;border-radius: 0;">Approve</button>
          		</form>
        </div>
      </div>
      
    </div>
  </div>
    <%
      }
    %>
    </tbody>
  </table>
   </div>
  </div>
	</div>
	
	<div id="profile" class="tab-pane fade in active">
	<div id='calendar'></div>
		<h5><b>PROFILE</b></h5>
		<hr style="border-color: #014FB3;">
		<div class="well">
			<%
				String first = account.getfName();
				String last = account.getlName();
				String fullName = first+" "+last;
				out.println("<h3>"+fullName+"</br>"+"<small>Administrator</small>"+"</h3>");
			%>
		</div>

	</div>
	
	<div id="departments" class="tab-pane fade">
		<%
			List<DepartmentClass> dept = (List<DepartmentClass>) session.getAttribute("departments");
			String error = (String)request.getAttribute("errorMsgDept");
			String success = (String)request.getAttribute("successMsgDept");
			if (error != null) {
				out.println("<div class='alert alert-danger fade in'>"+error+"</div>"); 
			}else if(success!=null){
				out.println("<div class='alert alert-success fade in'>"+success+"</div>");
			}
		%>
		<h5><b>MANAGE DEPARTMENTS</b></h5>
		<hr style="border-color: #014FB3;">
		<table class="table table-hover" id="deptTable">
			<thead style="background: #014FB3;">
			<tr>
				<th><center><font color="white">DEPARTMENT I.D.</font></center></th>
				<th><center><font color="white">DEPARTMENT NAME</font></center></th>
				<th><center><font color="white">DIVISION HEAD</font></center></th>
				<th><center><font color="white">NAME</font></center></th>
				<th></th>
				<th></th>
			</tr>
			</thead>
		
		<tbody>
	    <% 
			
			for(int ctr = 0; ctr < dept.size(); ctr++){
		%>
	      <tr>
	        <td align="center"><%=dept.get(ctr).getDepartmentID() %></td>
	        <td align="center"><%=dept.get(ctr).getDepartmentName() %></td>
	        <td align="center"><%=dept.get(ctr).getDivisionHead() %></td>
	        <td align="center"><%=dept.get(ctr).getFullName() %></td>
	        <td align="center">
				<button type="button" class="btn btn-warning" data-toggle="modal" data-target="#deptModalEdit<%=dept.get(ctr).getDepartmentID()%>">Edit</button>
			</td>
			<td align="center">
				<button type="button" class="btn btn-danger" data-toggle="modal" data-target="#deptModalDelete<%=dept.get(ctr).getDepartmentID()%>">Delete</button>
			</td>
	      </tr>
	  <div class="modal fade" id="deptModalDelete<%=dept.get(ctr).getDepartmentID()%>" role="dialog">
    <div class="modal-dialog">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header" style="padding:15px 50px;">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4>Department <%=dept.get(ctr).getDepartmentID() %></h4>
        </div>
        <div class="modal-body" style="padding:50px 50px;">
				<center><h5 style="padding:80px 50px;"><b>Are you sure you want to delete this department? The Division Head assigned will be changed to an Employee.</b></h5></center>
			<a href="delete.html?key=<%=dept.get(ctr).getDepartmentID() %>&option=department" role="button" class="btn btn-success btn-block" style="border-color: #52a55f;text-transform: uppercase;font-weight: 700;color: #fff !important;background-color: #52a55f;border-radius: 0;">Yes</a>
			<button type="button" data-dismiss="modal"class="btn btn-danger btn-block" style="border-color: #c44a4a;text-transform: uppercase;font-weight: 700;color: #fff !important;background-color: #c44a4a;border-radius: 0;">No</button>
        </div>
      </div>
      
    </div>
  </div>
  
  <div class="modal fade" id="deptModalEdit<%=dept.get(ctr).getDepartmentID()%>" role="dialog">
  <div class="modal-dialog">
  <!-- Modal content -->
  <div class="modal-content">
  <form class="form-vertical" action="editdepartment.html" method="post" enctype="multipart/form-data">
			<div class="modal-content" style="padding:15px 50px;">
			<div class="modal-body">
			<label for="editDepartment">Edit Department Name</label>
					<input type="text" id="editDepartment" class="form-control" name="editDepartment" 
					placeholder="<%=dept.get(ctr).getDepartmentName()%>" value="${fn:escapeXml(param.editDepartment)}" />
			</div>
			
			<div class="modal-body">
			<label for="editEmployeeId">Re-assign Division Head</label>
					<input type="text" id="editEmployeeId" min="1" max="25" class="form-control" name="editEmployeeId" 
					placeholder="<%=dept.get(ctr).getDivisionHead()%>" value="${fn:escapeXml(param.editEmployeeId)}" />
			</div>
			
			<div class="modal-body" style="padding:50px 50px;">
			<label for="photo">Portrait Photo</label>
			<label class="btn btn-primary btn-file btn-block">
					UPLOAD AN IMAGE<input type="file" id="photo" name="photo" style="display: none;"/></label>
					<input type = "hidden" name ="key" id="key" value="<%=dept.get(ctr).getDepartmentID()%>"/>
			</div>
			
			<div class="modal-body">
				<button type="submit" class="btn btn-success">Submit</button>
			</div>
			</div>	
		</form>
	</div>
  </div>
  </div>
  </div>
	    <%
	      }
	    %>
	    </tbody>
	  </table>
	  
	  
		<h5><b>ADD NEW DEPARTMENT</b></h5>
		<hr style="border-color: #014FB3;">
		
		<form class="form-vertical" action="generatedepartment.html" method="post" enctype="multipart/form-data">
		
			<div class="form-group">
			<label for="department">Department Name</label>
					<input type="text" id="department" class="form-control" name="department" 
					placeholder="Department Name" value="${fn:escapeXml(param.department)}" />
			</div>
			
			<div class="form-group">
			<label for="employeeId">Assign Division Head</label>
					<input type="text" id="employeeId" min="1" max="25" class="form-control" name="employeeId" 
					placeholder="Employee I.D." value="${fn:escapeXml(param.employeeId)}" />
			</div>
			
			<div class="form-group">
			<label for="photo">Portrait Photo</label>
			<label class="btn btn-primary btn-file btn-block">
					UPLOAD AN IMAGE<input type="file" id="photo" name="photo" style="display: none;"/></label>
			</div>
			
			<div class="form-group">
				<button type="submit" class="btn btn-success">Submit</button>
			</div>	
		</form>
	</div>
	
	<div id="vehicles" class="tab-pane fade">
		<h5><b>MANAGE VEHICLES</b></h5>
		<% 
		
		int counter = ImageClass.counter(connection);
		
			%>
		 <button type="button" class="btn btn-primary btn-lg" data-toggle="modal" data-target="#myModal">View</button>
		 <div class="modal fade" id="myModal" role="dialog">
    	<div class="modal-dialog">
    	<div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">Vehicles</h4>
        </div>
        <div class="modal-body">
		<div id="myCarousel" class="carousel slide" data-ride="carousel">
  <!-- Indicators -->
  <ol class="carousel-indicators">
  	<% 
  		int count = 0;
  		while(count < counter)
  		{
  			if(count == 0)
  			{
  				%> <li data-target="#myCarousel" data-slide-to="0" class="active"></li> <%
  				}
  			else
  			{
  				%>  <li data-target="#myCarousel" data-slide-to="${count}"></li> <%
  				}
  			count++;
  			}
  		count = 0;
  	%>
  </ol>

  <!-- Wrapper for slides -->
  <div class="carousel-inner" role="listbox">
  <%
  while(count < counter)
  	{
  		if(count == 0)
  		{
  			%> 
  			<div class="item active">
      		<img src="defaultcar.png" alt="crest" width="200" height="150">
    		</div>
    		<%
  			}
  		else
  		{
  			%>
  			<div class="item">
      		<img src="defaultcar.png" alt="crest" width="200" height="150">
    		</div>
    		<%
  			}
  		count++;
  		}
  %>
  </div>

  <!-- Left and right controls -->
  <a class="left carousel-control" href="#myCarousel" role="button" data-slide="prev">
    <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
    <span class="sr-only">Previous</span>
  </a>
  <a class="right carousel-control" href="#myCarousel" role="button" data-slide="next">
    <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
    <span class="sr-only">Next</span>
  </a>
</div>
</div>
<div class="modal-footer">
          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        </div>
      </div>
      
    </div>
  </div>
			<hr style="border-color: #014FB3;">
			<%
				List<VehicleClass> cars = (List<VehicleClass>) session.getAttribute("cars");
				String vehicleError = (String)request.getAttribute("errorMsgCar");
				String vehicleSuccess = (String)request.getAttribute("successMsgCar");
				if (vehicleError != null) {
					out.println("<div class='alert alert-danger fade in'>"+vehicleError+"</div>"); 
				}else if(vehicleSuccess!=null){
					out.println("<div class='alert alert-success fade in'>"+vehicleSuccess+"</div>");
				}
			%>
		<table class="table table-hover" id="carTable">
			<thead style="background: #014FB3;">
			<tr>
				<th><center><font color="white">PLATE NO.</font></center></th>
				<th><center><font color="white">MODEL</font></center></th>
				<th><center><font color="white">STATUS</font></center></th>
				<th></th>
				<th></th>
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
	        <td align="center">
				<button type="button" class="btn btn-warning" data-toggle="modal" data-target="#carModalEdit<%=cars.get(ctr).getPlateNum()%>">Edit</button>
			</td>
			<td align="center">
				<button type="button" class="btn btn-danger" data-toggle="modal" data-target="#carModalDelete<%=cars.get(ctr).getPlateNum()%>">Delete</button>
			</td>
	      </tr>
	      <div class="modal fade" id="carModalEdit<%=cars.get(ctr).getPlateNum()%>" role="dialog">
	      <div class="modal-dialog">
	      <!-- Modal content -->
	      <div class="modal-content">
	      <form action="editvehicle.html" method="post" enctype="multipart/form-data">
		<div class="modal-body" style="padding:15px 50px;">
			
			
				<div class="modal-body">
				<label for="carColor">Vehicle Color:</label>
					<input type="text" id="editCarColor" name="editCarColor"class="form-control" required="required" placeholder="<%=cars.get(ctr).getColor()%>" value="${fn:escapeXml(param.editCarColor)}" />
				</div>
			
		
				<div class="modal-body">
				<label for="carImage">Vehicle Image:</label>
					<label class="btn btn-primary btn-file btn-block">
					UPLOAD AN IMAGE<input type="file" id="editCarImage" name="editCarImage" class="form-control" accepts="image/*"style="display: none;"/></label>
				</div>
				
				<input type="hidden" name="key" id="key" value="<%=cars.get(ctr).getPlateNum()%>"/>
				
		<div class="modal-body">				
			<button type="submit" class="btn btn-success">Submit</button>
		</div>
		</div>
		</form>
		</div>
		</div>
	      </div>
	      
	  <div class="modal fade" id="carModalDelete<%=cars.get(ctr).getPlateNum()%>" role="dialog">
    <div class="modal-dialog">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header" style="padding:15px 50px;">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4>Plate No. <%=cars.get(ctr).getPlateNum() %></h4>
        </div>
        <div class="modal-body" style="padding:50px 50px;">
				<center><h5 style="padding:80px 50px;"><b>Are you sure you want to delete this vehicle? There might be some reservations linked to it.</b></h5></center>
			<a href="delete.html?key=<%=cars.get(ctr).getPlateNum() %>&option=cars" role="button" class="btn btn-success btn-block" style="border-color: #52a55f;text-transform: uppercase;font-weight: 700;color: #fff !important;background-color: #52a55f;border-radius: 0;">Yes</a>

			<button type="button" data-dismiss="modal"class="btn btn-danger btn-block" style="border-color: #c44a4a;text-transform: uppercase;font-weight: 700;color: #fff !important;background-color: #c44a4a;border-radius: 0;">No</button>
        </div>
      </div>
      
    </div>
  </div>
	    <%
	      }
	    %>
	    </tbody>
	  </table>
		<h5><b>ADD NEW VEHICLE</b></h5>
		<hr style="border-color: #014FB3;">

		<form action="addvehicle.html" method="post" enctype="multipart/form-data">
		<div class="row">
			<div class="col-sm-6">
				<div class="form-group">
				<label for="carManufacturer">Manufacturer:</label> 
					<input type="text" id="carManufacturer" name="carManufacturer" required="required" class="form-control" maxlength="50" placeholder="Manufacturer Name" value="${fn:escapeXml(param.carManufacturer)}"/>
				</div>
				<div class="form-group">
				<label for="yearMake">Year Manufactured:</label>
					<input type="number" id="yearMake" name="yearMake" min="2000"class="form-control" required="required" placeholder="Year Manufactured" value="${fn:escapeXml(param.yearMake)}" />
				</div>
				<div class="form-group">
				<label for="carModel">Vehicle Model:</label>
					<input type="text" id="carModel" name="carModel" required="required" class="form-control"maxlength="50" placeholder="Model" value="${fn:escapeXml(param.carModel)}" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group">
				<label for="carColor">Vehicle Color:</label>
					<input type="text" id="carColor" name="carColor"class="form-control" required="required" placeholder="Color" value="${fn:escapeXml(param.carColor)}" />
				</div>
				<div class="form-group">
				<label for="carPlate">Vehicle Plate Number:</label>
					<input type="text" id="carPlate" name="carPlate" required="required" class="form-control"maxlength="7" placeholder="Plate No. (ABC1234)" value="${fn:escapeXml(param.carPlate)}" />
				</div>
				<div class="form-group">
				<label for="carCapacity">Vehicle Max Capacity:</label>
					<input type="number" id="carCapacity" name="carCapacity" min="1" max="25"class="form-control"required="required" placeholder="Maximum capacity is 25" value="${fn:escapeXml(param.carCapacity)}" />
				</div>
			</div>
		</div>
				<div class="form-group">
				<label for="carImage">Vehicle Image:</label>
					<label class="btn btn-primary btn-file btn-block">
					UPLOAD AN IMAGE<input type="file" id="carImage" name="carImage" class="form-control" accepts="image/*"style="display: none;"/></label>
				</div>
		<div class="form-group">				
			<button type="submit" class="btn btn-success">Submit</button>
		</div>
		</form>
	</div>
	
	<div id="records" class="tab-pane fade">
		<h5><b>RECORDS & STATISTICS</b></h5>
		<hr style="border-color: #014FB3;">
		
		<div class="form-group">
			<a href="recstat.html" class="btn btn-primary" role="button">View Weekly</a>
		</div>

	</div>
	
	<div id="csv" class="tab-pane fade">
		<h5><b>IMPORT CSV ACCOUNTS</b></h5>
		<hr style="border-color: #014FB3;">
		<form action = "import.html" method="post" enctype="multipart/form-data">
		
			<div class="form-group">
				<label class="btn btn-primary btn-file btn-block">
				UPLOAD A CSV FILE<input type ="file" name="csv" accept=".csv" required="required"style="display: none;"></label>
			</div>
			
			<div class="form-group">
				<button type ="submit" class="btn btn-success">Import</button>
			</div>
			
		</form>
	</div>
	<div id="drivers" class="tab-pane fade">
		<h5><b>MANAGE DRIVERS</b></h5>
		<hr style="border-color: #014FB3;">
		<%
			List<DriverClass> drivers = (List<DriverClass>) session.getAttribute("drivers");
			String driverError = (String)request.getAttribute("errorMsgDriver");
			String driverSuccess = (String)request.getAttribute("successMsgDriver");
			if (driverError != null) {
				out.println("<div class='alert alert-danger fade in'>"+driverError+"</div>"); 
	
			}else if(driverSuccess!=null){
				out.println("<div class='alert alert-success fade in'>"+driverSuccess+"</div>");
			}
		%>
	<table class="table table-hover" id="driverTable">
		<thead style="background: #014FB3;">
		<tr>
			<th><center><font color="white">LICENSE I.D.</font></center></th>
			<th><center><font color="white">EMPLOYEE I.D.</font></center></th>
			<th><center><font color="white">NAME</font></center></th>
			<th></th>
			<th></th>
		</tr>
		</thead>
	
	<tbody>
    <% 
		
		for(int ctr = 0; ctr < drivers.size(); ctr++){
	%>
      <tr>
        <td align="center"><%=drivers.get(ctr).getLicenseID() %></td>
        <td align="center"><%=drivers.get(ctr).getEmployeeID()%></td>
        <td align="center"><%=drivers.get(ctr).getFullName()%></td>
        <td align="center">
			<button type="button" class="btn btn-warning" data-toggle="modal" data-target="#driverModalEdit<%=drivers.get(ctr).getLicenseID()%>">Edit</button>
		</td>
		<td align="center">
			<button type="button" class="btn btn-danger" data-toggle="modal" data-target="#driverModalDelete<%=drivers.get(ctr).getLicenseID() %>">Delete</button>
		</td>
      </tr>
  
     
  <div class="modal fade" id="driverModalDelete<%=drivers.get(ctr).getLicenseID() %>" role="dialog">
    <div class="modal-dialog">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header" style="padding:15px 50px;">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4>License # <%=drivers.get(ctr).getLicenseID() %></h4>
        </div>
        <div class="modal-body" style="padding:40px 50px;">

			<center><h5 style="padding:80px 50px;"><b>Are you sure you want to delete this driver? There might be some reservations assigned to him/her.</b></h5></center>
			<a href="delete.html?key=<%=drivers.get(ctr).getLicenseID()%>&option=driver" role="button" class="btn btn-success btn-block" style="border-color: #52a55f;text-transform: uppercase;font-weight: 700;color: #fff !important;background-color: #52a55f;border-radius: 0;">Yes</a>

			<button type="button" data-dismiss="modal"class="btn btn-danger btn-block" style="border-color: #c44a4a;text-transform: uppercase;font-weight: 700;color: #fff !important;background-color: #c44a4a;border-radius: 0;">No</button>
        </div>
      </div>
      
    </div>
  </div>
  
  <div class="modal fade" id="driverModalEdit<%=drivers.get(ctr).getLicenseID() %>" role="dialog">
    <div class="modal-dialog">
    <!-- Modal content-->
      <div class="modal-content">
      
    	<form action="editdriver.html" method="post" enctype="multipart/form-data">
		<div class="modal-content" style="padding:15px 50px;">
			<div class="modal-body">
				<label for="editDepartment">Portrait Photo</label>
					<label class="btn btn-primary btn-file btn-block">
					UPLOAD AN IMAGE<input type="file" id="editDriverImage" name="editDriverImage" class="form-control" accepts="image/*" style="display: none;"/></label>
					<input type="hidden" id="key" name="key" value="<%=drivers.get(ctr).getLicenseID() %>"/>
			</div>
			
			<div class="modal-body">
				<input type ="submit" value="Submit"/>
			</div>
			</div>
		</form>
        </div>
      </div>
      
    </div>
  </div>
    <%
      }
    %>
    </tbody>
  </table>
  <h5><b>ADD NEW DRIVER</b></h5>
  <hr style="border-color: #014FB3;">
		<form action="adddriver.html" method="post" enctype="multipart/form-data">
		
			<div class="form-group">
				<label for="department">Employee I.D.</label>
					<input type="text" id="employeeID" class="form-control" name="employeeID" placeholder="Employee I.D." value="${fn:escapeXml(param.employeeID)}" required="required" maxlength="6"/>
			</div>
			
			<div class="form-group">
				<label for="licenseID">Driver's License No.</label>
					<input type="text" id="licenseID" class="form-control" name="licenseID" placeholder= "License No." value="${fn:escapeXml(param.licenseID)}" required="required" maxlength="11"/>
			</div>
			
			<div class="form-group">
				<label for="department">Portrait Photo</label>
					<label class="btn btn-primary btn-file btn-block">
					UPLOAD AN IMAGE<input type="file" id="driverImage" name="driverImage" class="form-control" accepts="image/*" style="display: none;"/></label>
			</div>
			
			<div class="form-group">
				<button type="submit" class="btn btn-success">Submit</button>
			</div>
		</form>
	</div>
	
	<div id="emergency" class="tab-pane fade">
		<h5><b>EMERGENCY RESERVATION</b></h5>
		<hr style="border-color: #014FB3;">
		<%
			String emergencyError = (String)request.getAttribute("emError");
			String emergencySuccess = (String)request.getAttribute("emSuccess");
			if (emergencyError != null) {
				out.println("<div class='alert alert-danger fade in'>"+emergencyError+"</div>"); 
			}else if(emergencySuccess!=null){
				out.println("<div class='alert alert-success fade in'>"+emergencySuccess+"</div>");
			}
		%>
		<form class="form-vertical" action="emergency.html" method="post">
			<div class="form-group">
				<label for="employeeID">Employee I.D.</label>
				<input type="text" id="employeeID" class="form-control" name="employeeID" placeholder="Employee I.D." value="${fn:escapeXml(param.employeeID)}" required="required" maxlength="6"/>
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
	
</div>
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