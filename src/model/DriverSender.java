package model;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DriverSender {
	private String employeeID;
	private String licenseID;
	private String restrictionID;
	private InputStream driverImage;
	
	public String getEmployeeID() {
		return employeeID;
	}
	public void setEmployeeID(String employeeID) {
		this.employeeID = employeeID;
	}
	public String getLicenseID() {
		return licenseID;
	}
	public void setLicenseID(String licenseID) {
		
		this.licenseID = licenseID;
	}
	public String getRestrictionID() {
		return restrictionID;
	}
	public void setRestrictionID(String[] restrictionID) {
		String temp = "";
		for(int ctr = 0; ctr < restrictionID.length;ctr++){
			if(restrictionID[ctr]==null){
				continue;
			}else{
				temp = temp+restrictionID[ctr]+", ";
			}
		}
		this.restrictionID = temp;
	}
	public InputStream getDriverImage() {
		return driverImage;
	}
	public void setDriverImage(InputStream driverImage) {
		this.driverImage = driverImage;
	}
	
	public boolean employeeChecker(Connection connection){
		try{
			
			String query ="SELECT * FROM employee WHERE employeeID = ? AND accountTypeID = 1";
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, Integer.parseInt(employeeID));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()){
				return true;
			}else{
				return false;
			}
			
		}catch(SQLException sqle){
			System.out.println(sqle);
			return true;
		}
	}
	
	public boolean duplicateDriver(Connection connection){
		try{
			
			String query ="SELECT * FROM driver WHERE employeeID = ?";
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, Integer.parseInt(employeeID));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()){
				return true;
			}else{
				return false;
			}
			
		}catch(SQLException sqle){
			System.out.println(sqle);
			return true;
		}
	}
	
	public boolean duplicateLicense(Connection connection){
		try{
			
			String query ="SELECT * FROM driver WHERE licenseID = ?";
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setString(1, licenseID);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()){
				return true;
			}else{
				return false;
			}
			
		}catch(SQLException sqle){
			System.out.println(sqle);
			return true;
		}
	}
	
	private void sendDriver(Connection connection){
		try{
			String query ="INSERT INTO driver (licenseID, images, restrictionID, employeeID) values (?,?,?,?)";
			PreparedStatement pstmt = connection.prepareStatement(query);
			System.out.println(licenseID.toUpperCase());
			System.out.println(restrictionID);
			System.out.println(Integer.parseInt(employeeID));
			pstmt.setString(1, licenseID.toUpperCase());
			pstmt.setBlob(2, driverImage);
			pstmt.setString(3, restrictionID);
			pstmt.setInt(4, Integer.parseInt(employeeID));
			pstmt.executeUpdate();
			
		}catch(SQLException sqle){
			System.out.println(sqle);
		}
	}
	
	public void execute(Connection connection){
		sendDriver(connection);
	}
}
