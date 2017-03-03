package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetMethods {
	public ResultSet driverSession(Connection connection){
		try{
			String query ="SELECT * FROM driver";
			PreparedStatement pstmt = connection.prepareStatement(query);
			ResultSet drivers = pstmt.executeQuery();
			
			return drivers;
		}catch(SQLException sqle){
			System.out.println(sqle);
		}
		return null;
	}
	
	public ResultSet driverSessionGetEmpID(Connection connection, String licenseID){
		try{
			String query ="SELECT * FROM driver WHERE licenseID = ?";
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setString(1, licenseID);
			ResultSet drivers = pstmt.executeQuery();
			
			return drivers;
		}catch(SQLException sqle){
			System.out.println(sqle);
		}
		return null;
	}
	
	public ResultSet carSession(Connection connection){
		try{
			String query ="SELECT * FROM cars";
			PreparedStatement pstmt = connection.prepareStatement(query);
			ResultSet cars = pstmt.executeQuery();
			
			return cars;
		}catch(SQLException sqle){
			System.out.println(sqle);
		}
		return null;
	}
	
	public ResultSet carsSessionGetDetails(Connection connection, int carID){
		try{
			String query ="SELECT * FROM cars WHERE id = ?";
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, carID);
			ResultSet cars = pstmt.executeQuery();
			
			return cars;
		}catch(SQLException sqle){
			System.out.println(sqle);
		}
		return null;
	}
	
	public ResultSet departmentSession(Connection connection){
		try{
			String query ="SELECT * FROM departments";
			PreparedStatement pstmt = connection.prepareStatement(query);
			ResultSet dept = pstmt.executeQuery();
			
			return dept;
		}catch(SQLException sqle){
			System.out.println(sqle);
		}
		return null;
	}
	
	public ResultSet employeeSession(Connection connection, int empID){
		try{
			String query ="SELECT * FROM employee WHERE employeeID = ?";
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, empID);
			ResultSet emp = pstmt.executeQuery();
			
			return emp;
		}catch(SQLException sqle){
			System.out.println(sqle);
		}
		return null;
	}
}
