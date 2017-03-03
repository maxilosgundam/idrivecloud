package model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FinalizeReservation {
	
	private Date tripDate;
	private String driverName;
	private String licenseID;
	
	private String carName;
	private String carPlate;
	private int carID;
	
	private int empID;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + carID;
		result = prime * result + ((carName == null) ? 0 : carName.hashCode());
		result = prime * result + ((carPlate == null) ? 0 : carPlate.hashCode());
		result = prime * result + ((driverName == null) ? 0 : driverName.hashCode());
		result = prime * result + empID;
		result = prime * result + ((licenseID == null) ? 0 : licenseID.hashCode());
		result = prime * result + ((tripDate == null) ? 0 : tripDate.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FinalizeReservation other = (FinalizeReservation) obj;
		if (carID != other.carID)
			return false;
		if (carName == null) {
			if (other.carName != null)
				return false;
		} else if (!carName.equals(other.carName))
			return false;
		if (carPlate == null) {
			if (other.carPlate != null)
				return false;
		} else if (!carPlate.equals(other.carPlate))
			return false;
		if (driverName == null) {
			if (other.driverName != null)
				return false;
		} else if (!driverName.equals(other.driverName))
			return false;
		if (empID != other.empID)
			return false;
		if (licenseID == null) {
			if (other.licenseID != null)
				return false;
		} else if (!licenseID.equals(other.licenseID))
			return false;
		if (tripDate == null) {
			if (other.tripDate != null)
				return false;
		} else if (!tripDate.equals(other.tripDate))
			return false;
		return true;
	}
	public int getCarID() {
		return carID;
	}
	public void setCarID(int carID) {
		this.carID = carID;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getLicenseID() {
		return licenseID;
	}
	public void setLicenseID(String licenseID) {
		this.licenseID = licenseID;
	}
	public String getCarName() {
		return carName;
	}
	public void setCarName(String carName) {
		this.carName = carName;
	}
	public String getCarPlate() {
		return carPlate;
	}
	public void setCarPlate(String carPlate) {
		this.carPlate = carPlate;
	}
	public Date getTripDate() {
		return tripDate;
	}
	public void setTripDate(Date tripDate) {
		this.tripDate = tripDate;
	}
	
	public int getEmpID() {
		return empID;
	}
	public void setEmpID(int empID) {
		this.empID = empID;
	}
	public ResultSet selectReservations(Connection connection, int resID){
		try{
			String query ="SELECT * FROM reservations WHERE reservationID = ?";
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, resID);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				setTripDate(rs.getDate("tripDate"));
			}
			String query2 ="SELECT * FROM reservations WHERE tripDate = ? AND statusID = 3";
			PreparedStatement pstmt2 = connection.prepareStatement(query2);
			pstmt2.setDate(1, getTripDate());
			ResultSet rs2 = pstmt2.executeQuery();
			return rs2;
		}catch(SQLException sqle){
			System.out.println(sqle);
		}
		return null;
	}
	
	public ResultSet chooseReservation(Connection connection, int resID){
		try{
			String query ="SELECT * FROM reservations WHERE reservationID = ?";
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, resID);
			ResultSet rs = pstmt.executeQuery();
			return rs;
		}catch(SQLException sqle){
			System.out.println(sqle);
		}
		return null;
	}
	
	public ResultSet selectDriver(Connection connection){
		try{
			String query ="SELECT * FROM driver";
			PreparedStatement pstmt = connection.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			return rs;
		}catch(SQLException sqle){
			System.out.println(sqle);
		}
		return null;
	}
	
	public ResultSet selectCar(Connection connection){
		try{
			String query ="SELECT * FROM cars";
			PreparedStatement pstmt = connection.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			return rs;
		}catch(SQLException sqle){
			System.out.println(sqle);
		}
		return null;
	}
	
	public String selectEmployee(Connection connection, String licenseID){
		try{
			String query ="SELECT * FROM driver WHERE licenseID = ?";
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setString(1, licenseID);
			ResultSet rs = pstmt.executeQuery();
			int empID=0;
			while(rs.next()){
				empID = rs.getInt("employeeID");
			}
			String query2 ="SELECT * FROM employee WHERE employeeID = ?";
			PreparedStatement pstmt2 = connection.prepareStatement(query2);
			pstmt2.setInt(1, empID);
			ResultSet rs2 = pstmt2.executeQuery();
			String fullName="";
			while(rs2.next()){
				fullName = rs2.getString("firstName")+" "+rs2.getString("lastName");
			}
			return fullName;
		}catch(SQLException sqle){
			System.out.println(sqle);
		}
		return "None";
	}
	
	public String carNameSelector(Connection connection, String plateNum){
		try{
			String query ="SELECT * FROM cars WHERE plateNum = ?";
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setString(1, plateNum);
			ResultSet rs = pstmt.executeQuery();
			String fullCarName="";
			while(rs.next()){
				fullCarName = rs.getString("color")+" "+rs.getString("manufacturer")+" "+rs.getString("model");
			}
			return fullCarName;
		}catch(SQLException sqle){
			System.out.println(sqle);
		}
		return "None";
	}
	
	public String carPlateSelector(Connection connection, int carID){
		try{
			String query ="SELECT * FROM cars WHERE id = ?";
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, carID);
			ResultSet rs = pstmt.executeQuery();
			String plateNum="";
			while(rs.next()){
				plateNum = rs.getString("plateNum");
			}
			return plateNum;
		}catch(SQLException sqle){
			System.out.println(sqle);
		}
		return null;
	}

}
