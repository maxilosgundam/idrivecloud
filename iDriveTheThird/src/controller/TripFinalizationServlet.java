package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import java.sql.PreparedStatement;

import model.FinalizeReservation;
import model.ReservationViewer;

public class TripFinalizationServlet extends HttpServlet {
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
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		
		if(session.getAttribute("accountSession") == null){
			response.sendRedirect("login.jsp");
		} else {
			int reservationID = Integer.parseInt(request.getParameter("approved"));
			System.out.println("Reservation ID: "+reservationID);
			try {
				FinalizeReservation fr = new FinalizeReservation();
				ResultSet reservation = fr.selectReservations(connection,reservationID);
				ResultSet reservation2 = fr.selectReservations(connection,reservationID);
				ResultSet driver = fr.selectDriver(connection);
				ResultSet car = fr.selectCar(connection);
				ResultSet rv  = fr.chooseReservation(connection, reservationID);
				List<ReservationViewer> selectedReservation = new ArrayList<ReservationViewer>();
				
				List<FinalizeReservation> driverList = new ArrayList<FinalizeReservation>();
	            while(driver.next()){
	            	FinalizeReservation frDriver = new FinalizeReservation();
	            	frDriver.setLicenseID(driver.getString("licenseID"));
	            	String name = frDriver.selectEmployee(connection, driver.getString("licenseID"));
	            	frDriver.setDriverName(name);
	            	driverList.add(frDriver);
	            }
	
	            List<FinalizeReservation> notAvailableDriverList = new ArrayList<FinalizeReservation>();
	            while(reservation.next()){
	            	FinalizeReservation frReservation = new FinalizeReservation();
	            	frReservation.setLicenseID(reservation.getString("licenseID"));
	            	String name = frReservation.selectEmployee(connection, reservation.getString("licenseID"));
	            	frReservation.setDriverName(name);
	                notAvailableDriverList.add(frReservation);
	            }
	
	            List<FinalizeReservation> union = new ArrayList<FinalizeReservation>(driverList);
	            union.addAll(notAvailableDriverList);
	            
	            List<FinalizeReservation> intersection = new ArrayList<FinalizeReservation>(driverList);
	            intersection.retainAll(notAvailableDriverList);
	            
	            union.removeAll(intersection);
	            for (int ctr = 0; ctr < union.size();ctr++) {
	                System.out.println(union.get(ctr).getLicenseID());
	                System.out.println(union.get(ctr).getDriverName());
	            }
	            
	            List<FinalizeReservation> carList = new ArrayList<FinalizeReservation>();
	            while(car.next()){
	            	FinalizeReservation frCar = new FinalizeReservation();
	            	frCar.setCarID(car.getInt("id"));
	            	frCar.setCarPlate(car.getString("plateNum"));
	            	String carName = frCar.carNameSelector(connection, car.getString("plateNum"));
	            	frCar.setCarName(carName);
	            	carList.add(frCar);
	            }
	
	            List<FinalizeReservation> notAvailableCarList = new ArrayList<FinalizeReservation>();
	            while(reservation2.next()){
	            	FinalizeReservation frReservationCar = new FinalizeReservation();
	            	frReservationCar.setCarID(reservation2.getInt("carID"));
	            	String carPlate = frReservationCar.carPlateSelector(connection, reservation2.getInt("carID"));
	            	frReservationCar.setCarPlate(carPlate);
	            	String carName = frReservationCar.carNameSelector(connection, carPlate);
	            	frReservationCar.setCarName(carName);
	            	notAvailableCarList.add(frReservationCar);
	            }
	
	            List<FinalizeReservation> union2 = new ArrayList<FinalizeReservation>(carList);
	            union2.addAll(notAvailableCarList);
	            
	            List<FinalizeReservation> intersection2 = new ArrayList<FinalizeReservation>(carList);
	            intersection2.retainAll(notAvailableCarList);
	            
	            union2.removeAll(intersection2);
	            for (int ctr = 0; ctr < union2.size();ctr++) {
	                System.out.println(union2.get(ctr).getCarName());
	                System.out.println(union2.get(ctr).getCarPlate());
	            }
	
				while(rv.next()){
					ReservationViewer details = new ReservationViewer();
					details.setReservationID(reservationID);
					details.setTripDate(rv.getDate("tripDate"));
					details.setDeparture(rv.getString("departure"));
					details.setDestination(rv.getString("destination"));
					details.setPurpose(rv.getString("travelPurpose"));
					details.setPassengers(rv.getString("passengers"));
					details.setPassengerNum(rv.getInt("passengerNum"));
					details.setDepartmentID(rv.getInt("departmentID"));
					details.setEmployeeID(rv.getInt("employeeID"));
					details.setTrackId(rv.getInt("trackingID"));
					details.executeProcess(connection);
					System.out.println(rv.getString("departure"));
					selectedReservation.add(details);
				}
				System.out.println("Size: "+selectedReservation.size());
				session.removeAttribute("availCars");
				session.removeAttribute("availDrivers");
				session.removeAttribute("viewEach");
				session.setAttribute("availCars", union2);
				session.setAttribute("availDrivers", union);
				session.setAttribute("viewEach", selectedReservation);
				response.sendRedirect("approve.jsp");
			} catch (SQLException sqle) {
				System.out.println(sqle);
				sqle.printStackTrace();
			}
		}
	}

}
