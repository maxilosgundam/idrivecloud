package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeleteClass {
	
	private int departmentKey;
	private String driverKey;
	private String vehicleKey;
	
	public int getDepartmentKey() {
		return departmentKey;
	}

	public void setDepartmentKey(int departmentKey) {
		this.departmentKey = departmentKey;
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

	public void deleteDepartment(Connection connection){
		try{
			updateEmployeeTable(connection);
			
			String deleteQuery = "DELETE FROM departments WHERE departmentID = ?";
			PreparedStatement pstmt2 = connection.prepareStatement(deleteQuery);
			pstmt2.setInt(1, departmentKey);
			pstmt2.executeUpdate();
			
			
		} catch(SQLException sqle){
			System.out.println(sqle);
			System.out.println("delete class model threw this men");
		}
	}
	
	public void deleteDriver(Connection connection){
		try{
			
			String deleteQuery = "DELETE FROM driver WHERE licenseID = ?";
			PreparedStatement pstmt2 = connection.prepareStatement(deleteQuery);
			pstmt2.setString(1, driverKey);
			pstmt2.executeUpdate();
			
		} catch(SQLException sqle){
			System.out.println(sqle);
			System.out.println("delete class model threw this men");
		}
	}
	
	public void deleteVehicle(Connection connection){
		try{
			
			String deleteQuery = "DELETE FROM cars WHERE plateNum = ?";
			PreparedStatement pstmt2 = connection.prepareStatement(deleteQuery);
			pstmt2.setString(1, vehicleKey);
			pstmt2.executeUpdate();
			
		} catch(SQLException sqle){
			System.out.println(sqle);
			System.out.println("delete class model threw this men");
		}
	}
	
	private int searchEmployeeID(Connection connection){
		try{
			
			String query = "SELECT * FROM departments WHERE departmentID = ?";
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, departmentKey);
			ResultSet departmentsTable = pstmt.executeQuery();
			
			int empID = 0;
			while(departmentsTable.next()){
				empID = departmentsTable.getInt("divisionHead");
			}
			return empID;
			
		} catch(SQLException sqle){
			System.out.println(sqle);
			System.out.println("delete class model threw this men");
		}
		return 0;
	}
	
	private void updateEmployeeTable(Connection connection){
		try{
			int empID = searchEmployeeID(connection);
			String query = "UPDATE employee SET accountTypeID = 1 WHERE employeeID = ?";
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, empID);
			pstmt.executeUpdate();
		} catch(SQLException sqle){
			System.out.println(sqle);
			System.out.println("edit class model threw this men");
		}
	}
}
