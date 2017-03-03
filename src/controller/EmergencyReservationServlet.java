package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import model.EmergencyReservationClass;
import model.InputValidationClass;

public class EmergencyReservationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Connection connection = null;
	
	public void init() throws ServletException {
		try {
			connection = ((DataSource)InitialContext.doLookup("java:/comp/env/jdbc/iDriveDB")).getConnection();
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
		HttpSession session = request.getSession(false);
		
		if(session.getAttribute("accountSession") == null){ // Ask sir about the vehicle and driver
			System.out.println("no session");
		} else {
			
			String employeeId = request.getParameter("employeeID");
			String timeHours = request.getParameter("hours");
			String timeMinutes = request.getParameter("minutes");
			String timeOfDay = request.getParameter("time");
			String destination = request.getParameter("destination");
			String purpose = request.getParameter("travelPurpose");
			String numPassengers = request.getParameter("numPassengers");
			String passengers = request.getParameter("passengers");
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			try {
				date = dateFormat.parse(dateFormat.format(date));
				InputValidationClass validate = new InputValidationClass();
				EmergencyReservationClass emergency = new EmergencyReservationClass();
				if(validate.employeeIdInputValidation(connection, employeeId)){
					if(validate.timeInputValidation(timeHours, timeMinutes, timeOfDay)){
						if(validate.destinationInputValidation(destination)){
							if(validate.purposeInputValidation(purpose)){
								if(validate.numberInputValidation(numPassengers)){
									if(validate.passengerInputValidation(passengers)){
										emergency.setTimeHours(timeHours);
										emergency.setTimeMinutes(timeMinutes);
										emergency.setTimeOfDay(timeOfDay);
										emergency.setDestination(destination);
										emergency.setPurpose(purpose);
										emergency.setNumPassengers(numPassengers);
										emergency.setPassengers(passengers);
										
										if(emergency.emergencyReserve(connection, date, Integer.parseInt(employeeId))){
											System.out.println("success");
										} else {
											System.out.println("dept not found? check EmergencyReservationClass");
											//error dept of employee not valid?
										}
									} else {
										System.out.println("passenger name error. InputValidationClass");
										//passenger name error
									}
								} else {
									System.out.println("number of passenger error. InputValidationClass");
									//number of passenger error
								}
							} else {
								System.out.println("purpose of travel error. InputValidationClass");
								//purpose error
							}
						} else {
							System.out.println("destination error. InputValidationClass");
							//destination error
						}
					} else {
						System.out.println("time error. InputValidationClass");
						//time error
					}
				} else {
					System.out.println("employee error. InputValidationClass");
					//employee does not exist
				}
			} catch (ParseException e) {
				
				e.printStackTrace();
			}
			
			
						
		}
	}

}
