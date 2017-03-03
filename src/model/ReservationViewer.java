package model;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ReservationViewer implements Serializable{
	
	private int reservationID;
	private Date tripDate;
	private String departure;
	private String destination;
	private String purpose;
	private String passengers;
	private int passengerNum;
	private String department;
	private String name;
	private String plateNum;
	private String licenseID;
	private String token;
	private String deptName;
	private String driverName;
	private int carID;
	private String carName;
	
	private int departmentID;
	private int employeeID;
	
	private int reservationIDEmp;
	private Date tripDateEmp;
	private String departureEmp;
	private String destinationEmp;
	private String purposeEmp;
	private String passengersEmp;
	private int passengerNumEmp;
	private String departmentEmp;
	private String nameEmp;
	private int trackId;
	private String trackWord;
	
	public ReservationViewer(){
		
	}
	
	
	public String getCarName() {
		return carName;
	}
	public void setCarName(String carName) {
		this.carName = carName;
	}
	public int getCarID() {
		return carID;
	}
	public void setCarID(int carID) {
		this.carID = carID;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getPlateNum() {
		return plateNum;
	}
	public void setPlateNum(String plateNum) {
		this.plateNum = plateNum;
	}
	public String getLicenseID() {
		return licenseID;
	}
	public void setLicenseID(String licenseID) {
		this.licenseID = licenseID;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public int getReservationID() {
		return reservationID;
	}
	public void setReservationID(int reservationID) {
		this.reservationID = reservationID;
	}
	public Date getTripDate() {
		return tripDate;
	}
	public void setTripDate(Date tripDate) {
		this.tripDate = tripDate;
	}
	public String getDeparture() {
		return departure;
	}
	public void setDeparture(String departure) {
		this.departure = departure;
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
	public String getPassengers() {
		return passengers;
	}
	public void setPassengers(String passengers) {
		this.passengers = passengers;
	}
	public int getPassengerNum() {
		return passengerNum;
	}
	public void setPassengerNum(int passengerNum) {
		this.passengerNum = passengerNum;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getDepartmentID() {
		return departmentID;
	}
	public void setDepartmentID(int departmentID) {
		this.departmentID = departmentID;
	}
	public int getEmployeeID() {
		return employeeID;
	}
	public void setEmployeeID(int employeeID) {
		this.employeeID = employeeID;
	}
	public int getTrackId() {
		return trackId;
	}
	public void setTrackId(int trackId) {
		this.trackId = trackId;
	}
	public String getTrackWord() {
		return trackWord;
	}
	public void setTrackWord(String trackWord) {
		this.trackWord = trackWord;
	}
	public int getReservationIDEmp() {
		return reservationIDEmp;
	}


	public void setReservationIDEmp(int reservationIDEmp) {
		this.reservationIDEmp = reservationIDEmp;
	}


	public Date getTripDateEmp() {
		return tripDateEmp;
	}


	public void setTripDateEmp(Date tripDateEmp) {
		this.tripDateEmp = tripDateEmp;
	}


	public String getDepartureEmp() {
		return departureEmp;
	}


	public void setDepartureEmp(String departureEmp) {
		this.departureEmp = departureEmp;
	}


	public String getDestinationEmp() {
		return destinationEmp;
	}


	public void setDestinationEmp(String destinationEmp) {
		this.destinationEmp = destinationEmp;
	}


	public String getPurposeEmp() {
		return purposeEmp;
	}


	public void setPurposeEmp(String purposeEmp) {
		this.purposeEmp = purposeEmp;
	}


	public String getPassengersEmp() {
		return passengersEmp;
	}


	public void setPassengersEmp(String passengersEmp) {
		this.passengersEmp = passengersEmp;
	}


	public int getPassengerNumEmp() {
		return passengerNumEmp;
	}


	public void setPassengerNumEmp(int passengerNumEmp) {
		this.passengerNumEmp = passengerNumEmp;
	}


	public String getDepartmentEmp() {
		return departmentEmp;
	}


	public void setDepartmentEmp(String departmentEmp) {
		this.departmentEmp = departmentEmp;
	}


	public String getNameEmp() {
		return nameEmp;
	}


	public void setNameEmp(String nameEmp) {
		this.nameEmp = nameEmp;
	}


	public ResultSet adminReservations(Connection connection){
		try{
			String query ="SELECT * FROM reservations WHERE trackingID = 2 AND statusID = 1";
			PreparedStatement pstmt = connection.prepareStatement(query);
			ResultSet pendingReservations = pstmt.executeQuery();
			
			return pendingReservations;
		}catch(SQLException sqle){
			System.out.println(sqle);
		}
		return null;
	}

	public ResultSet managerReservations(Connection connection, int deptID){
		try{
			String query ="SELECT * FROM reservations WHERE trackingID = 1 AND departmentID = ?";
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, deptID);
			ResultSet pendingReservations = pstmt.executeQuery();
			
			return pendingReservations;
		}catch(SQLException sqle){
			System.out.println(sqle);
		}
		return null;
	}
	
	public ResultSet employeeReservations(Connection connection, int empId){
		try{
			String query ="SELECT * FROM reservations WHERE employeeID = ?";
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, empId);
			ResultSet pendingReservations = pstmt.executeQuery();
			
			return pendingReservations;
		}catch(SQLException sqle){
			System.out.println(sqle);
		}
		return null;
	}
	
	private void departmentName(Connection connection, int deptID){
		deptID=departmentID;
		try{
			String query ="SELECT * FROM departments WHERE departmentID = ?";
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, deptID);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()){
				setDepartment(rs.getString("departmentName"));
			}else{

			}
		}catch(SQLException sqle){
			System.out.println(sqle);
			System.out.println("department exception omg");
		}
	}
	
	private void fullName(Connection connection, int empID){
		empID=employeeID;
		try{
			String query ="SELECT * FROM employee WHERE employeeID = ?";
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, empID);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()){
				String fName = rs.getString("firstName");
				String lName = rs.getString("lastName");
				setName(fName+" "+lName);
			}else{

			}
		}catch(SQLException sqle){
			System.out.println(sqle);
			System.out.println("full name exception ohno");
		}
	}
	
	public void executeProcess(Connection connection){
		if(getTrackId() == 1)
		{
			setTrackWord("Division Head");
			}
		else if(getTrackId() == 2)
		{
			setTrackWord("Administrator");
			}
		else if(getTrackId() == 3)
		{
			setTrackWord("Finished");
			}
		else if(getTrackId() == 4)
		{
			setTrackWord("Upcoming");
			}
		else if(getTrackId() == 5)
		{
			setTrackWord("Ongoing");
			}
		else if(getTrackId() == 6)
		{
			setTrackWord("Emergency");
			}
		departmentName(connection, getDepartmentID());
		fullName(connection, getEmployeeID());
	}
}
