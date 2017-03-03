package model;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EditClass {
	
	private InputStream image;
	private String color;
	private String driverKey;
	private String vehicleKey;
	private int departmentKey;
	private String deptName;
	private int employeeID;

	public InputStream getImage() {
		return image;
	}

	public void setImage(InputStream image) {
		this.image = image;
	}
	
	public String getDriverKey() {
		return driverKey;
	}

	public void setDriverKey(String driverKey) {
		this.driverKey = driverKey;
	}
	
	public String getVehicleKey() {
		return vehicleKey;
	}

	public void setVehicleKey(String vehicleKey) {
		this.vehicleKey = vehicleKey;
	}
	
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getDepartmentKey() {
		return departmentKey;
	}

	public void setDepartmentKey(int departmentKey) {
		this.departmentKey = departmentKey;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public int getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(int employeeID) {
		this.employeeID = employeeID;
	}

	public void updateDriver(Connection connection){
		
		try{
			
			String editQuery = "UPDATE driver SET images = ? WHERE licenseID = ?";
			PreparedStatement pstmt2 = connection.prepareStatement(editQuery);
			pstmt2.setBlob(1, image);
			pstmt2.setString(2, driverKey);
			pstmt2.executeUpdate();
			
		} catch(SQLException sqle){
			System.out.println(sqle);
			System.out.println("edit class model threw this men");
		}
	}
	
	public void updateVehicle(Connection connection){
		try{
			
			String editQuery = "UPDATE cars SET images=?, color=? WHERE plateNum = ?";
			PreparedStatement pstmt2 = connection.prepareStatement(editQuery);
			pstmt2.setBlob(1, image);
			pstmt2.setString(2, color);
			pstmt2.setString(3, vehicleKey);
			pstmt2.executeUpdate();
			
		} catch(SQLException sqle){
			System.out.println(sqle);
			System.out.println("edit class model threw this men");
		}
	}
	
	public void updateDepartments(Connection connection){
		try{
			
			String editQuery = "UPDATE departments SET images=?, departmentName=?, divisionHead=? WHERE departmentID = ?";
			PreparedStatement pstmt2 = connection.prepareStatement(editQuery);
			pstmt2.setBlob(1, image);
			pstmt2.setString(2, deptName.toUpperCase());
			pstmt2.setInt(3, employeeID);
			pstmt2.setInt(4, departmentKey);
			pstmt2.executeUpdate();
			
		} catch(SQLException sqle){
			System.out.println(sqle);
			System.out.println("edit class model threw this men");
		}
	}
}
