package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import model.AccountClass;
import model.InputValidationClass;
import model.ReservationSender;
import model.ReservationViewer;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.errors.AccessControlException;

import model.ReservationSender;
import util.ResponseMessage;
import util.SessionHandlers;

public class SendReservationServlet extends HttpServlet implements ResponseMessage{
	private static final long serialVersionUID = 1L;
	Connection connection = null;
	public void init() throws ServletException {
		
		try {
			connection = ((DataSource)InitialContext.doLookup("java:/comp/env/jdbc/iDriveDB"))
					.getConnection();
			getServletContext().setAttribute("dbConnection", connection);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			System.err.println("NamingException Exception - " + e.getMessage());
			e.printStackTrace();
		} catch (SQLException sqle) {
			System.err.println("SQLE Exception - " + sqle.getMessage());
			sqle.printStackTrace();
		}
	
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
		HttpSession session = request.getSession(false);
		
		if(session.getAttribute("accountSession") == null){
			response.sendRedirect("login.jsp");
		} else {
			String date = request.getParameter("tripDate");
			String timeHours = request.getParameter("hours");
			String timeMinutes = request.getParameter("minutes");
			String timeOfDay = request.getParameter("time");
			String destination = request.getParameter("destination");
			String purpose = request.getParameter("travelPurpose");
			String numPassengers = request.getParameter("numPassengers");
			String passengers = request.getParameter("passengers");
			String location = "";
			
			AccountClass ac = (AccountClass) session.getAttribute("accountSession");
			int accType = ac.getAccType();
			int empId = ac.getAcc();
			int department = ac.getDeptName();
			String fName = ac.getfName();
			String lName = ac.getlName();
			String fullName = fName+" "+lName;
			
			switch(accType){
				case 1: 
					location = "WEB-INF/employee";
					break;
				case 2:
					location = "WEB-INF/manager";
					break;
				case 3:
					location = "WEB-INF/administrator";
					break;
				default:
					location = "WEB-INF/login";
					break;
			}
			
			InputValidationClass validate = new InputValidationClass();
			
			if(validate.dateInputValidation(date) == true){
				if(validate.timeInputValidation(timeHours, timeMinutes, timeOfDay) == true){
					if(validate.destinationInputValidation(destination) == true){
						if(validate.purposeInputValidation(purpose) == true){
							if(validate.numberInputValidation(numPassengers) == true){
								if(validate.passengerInputValidation(passengers) == true){
									ReservationSender sender = new ReservationSender();
									sender.setDate(date);
									sender.setTimeHours(timeHours);
									sender.setTimeMinutes(timeMinutes);
									sender.setTimeOfDay(timeOfDay);
									sender.setDestination(destination);
									sender.setPurpose(purpose);
									sender.setNumPassengers(numPassengers);
									sender.setPassengers(passengers);
									sender.setEmpId(empId);
									sender.setSender(getServletContext().getInitParameter("sender"));
									sender.setPassword(getServletContext().getInitParameter("password"));
									sender.setMailHost(getServletContext().getInitParameter("mailHost"));
									sender.setGmail(getServletContext().getInitParameter("gmail"));
									sender.setMailPort(getServletContext().getInitParameter("mailPort"));
									sender.setMailPortNumber(getServletContext().getInitParameter("mailPortNumber"));
									sender.setMailSSL(getServletContext().getInitParameter("mailSSL"));
									sender.setMailAuth(getServletContext().getInitParameter("mailAuth"));
									sender.setCrest(getServletContext().getInitParameter("crest"));
									sender.setCrest2(getServletContext().getInitParameter("crest2Dir"));
									sender.setLogo(getServletContext().getInitParameter("logoDir"));
									
									if(sender.validateReservation(connection)){
										if(sender.validateAvailableDriverAndVehicle(connection)){
											if(sender.carCodingChecker(connection)){
												sender.sendReservation(connection, accType, department, fullName);
												
												SessionHandlers handler = new SessionHandlers();
												List<ReservationViewer> reservationDetailsEmp = handler.empReservationSession(connection, empId);
												session.removeAttribute("approved");
												session.setAttribute("approved", reservationDetailsEmp);
												
												request.setAttribute("successMsg", RESERVE_SUCCES);
												ESAPI.httpUtilities().sendForward(request, response, location);
											}else{
												request.setAttribute("errorMsg", NO_DRIVER_VEHICLE_AVAILABLE);
												ESAPI.httpUtilities().sendForward(request, response, location);
											}
										}else{
											request.setAttribute("errorMsg", NO_DRIVER_VEHICLE_AVAILABLE);
											ESAPI.httpUtilities().sendForward(request, response, location);
										}
									}else{
										//error for similar reservation
										request.setAttribute("errorMsg", DUPLICATE_RESERVATION);
										ESAPI.httpUtilities().sendForward(request, response, location);
									}
								} else {
									//error for passengers input message
									System.out.println(validate.passengerInputValidation(passengers));
									request.setAttribute("errorMsg", INVALID_PASSENGERS);
									ESAPI.httpUtilities().sendForward(request, response, location);
								}
							} else {
								//error for numPassengers input message
								System.out.println(validate.numberInputValidation(numPassengers));
								request.setAttribute("errorMsg", INVALID_NUM_PASSENGERS);
								ESAPI.httpUtilities().sendForward(request, response, location);
							}
						} else {
							//error for purpose input message
							System.out.println(validate.purposeInputValidation(purpose));
							request.setAttribute("errorMsg", INVALID_PURPOSE);
							ESAPI.httpUtilities().sendForward(request, response, location);
						}
					} else {
						//error for destination input message
						System.out.println(validate.destinationInputValidation(destination));
						request.setAttribute("errorMsg", INVALID_DESTINATION);
						ESAPI.httpUtilities().sendForward(request, response, location);
					}
				} else {
					//error for time input message
					System.out.println(validate.timeInputValidation(timeHours, timeMinutes, timeOfDay));
					request.setAttribute("errorMsg", INVALID_TIME);
					ESAPI.httpUtilities().sendForward(request, response, location);
				}
			} else {
				//error for trip date message
				System.out.println(validate.dateInputValidation(date));
				request.setAttribute("errorMsg", INVALID_DATE);
				ESAPI.httpUtilities().sendForward(request, response, location);
			}
		}
	}
		catch(AccessControlException ace)
		{
			System.out.println(ace.getMessage());
			}
	}

}