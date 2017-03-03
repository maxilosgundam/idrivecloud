package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EmergencyReservationClass {
	
	private String date;
	private String timeHours;
	private String timeMinutes;
	private String timeOfDay;
	private String destination;
	private String purpose;
	private String numPassengers;
	private String passengers;
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTimeHours() {
		return timeHours;
	}

	public void setTimeHours(String timeHours) {
		this.timeHours = timeHours;
	}

	public String getTimeMinutes() {
		return timeMinutes;
	}

	public void setTimeMinutes(String timeMinutes) {
		this.timeMinutes = timeMinutes;
	}

	public String getTimeOfDay() {
		return timeOfDay;
	}

	public void setTimeOfDay(String timeOfDay) {
		this.timeOfDay = timeOfDay;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getNumPassengers() {
		return numPassengers;
	}

	public void setNumPassengers(String numPassengers) {
		this.numPassengers = numPassengers;
	}

	public String getPassengers() {
		return passengers;
	}

	public void setPassengers(String passengers) {
		this.passengers = passengers;
	}

	public boolean emergencyReserve(Connection connection, Date date, int employeeId){
		String deptQuery = "SELECT departmentName FROM employee WHERE employeeID = ?";
		try {
			PreparedStatement pstmt = connection.prepareStatement(deptQuery);
			pstmt.setInt(1, employeeId);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()){
				int deptId = rs.getInt("departmentName");
	            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
	            String departureFull = getTimeHours()+":"+getTimeMinutes()+" "+getTimeOfDay();
				String query = "INSERT INTO reservations (tripDate, departure, destination, travelPurpose, passengers, passengerNum, employeeID, statusID, trackingID, departmentID) values (?,?,?,?,?,?,?,?,?,?)";
				PreparedStatement pstmt2 = connection.prepareStatement(query);
				
				pstmt2.setDate(1, sqlDate);
				pstmt2.setString(2, departureFull);
				pstmt2.setString(3, getDestination());
				pstmt2.setString(4, getPurpose());
				pstmt2.setString(5,getPassengers());
				pstmt2.setInt(6, Integer.parseInt(getNumPassengers()));
				pstmt2.setInt(7, employeeId);
				pstmt2.setInt(8, 4);
				pstmt2.setInt(9, 6);
				pstmt2.setInt(10, deptId);
				pstmt2.executeUpdate();
				
				return true;
			}
			
			
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return false;
	}
	
	
}
