package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DepartmentClass {
	int departmentID;
	String departmentName;
	int divisionHead;
	String fullName;
	
	public int getDepartmentID() {
		return departmentID;
	}
	public void setDepartmentID(int departmentID) {
		this.departmentID = departmentID;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public int getDivisionHead() {
		return divisionHead;
	}
	public void setDivisionHead(int divisionHead) {
		this.divisionHead = divisionHead;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public boolean checkDepartment(Connection connection, int transferID){
		String query = "SELECT * FROM employee WHERE departmentName = ?";
		
		try {
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, transferID);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()){
				//this department is not empty
				return false;
			}
			
			return true;
		} catch (SQLException e) {
			
			e.printStackTrace();
			return false;
		}
		
	}
	
	public boolean transferEmployee(Connection connection, int transferID, int delID){
		String query = "UPDATE employee SET departmentName = ? WHERE departmentName = ?";
		
		try {
			
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, transferID);
			pstmt.setInt(2, delID);
			pstmt.executeUpdate();
			
			return true;
			
		} catch (SQLException e) {
			//something failed on update.
			e.printStackTrace();
			return false;
		}
		
	}
	public boolean deleteDepartment(Connection connection, int delID){
		String query = "DELETE FROM departments where departmentID = ?";
		try {
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, delID);
			pstmt.executeUpdate();
			
			return true;
		} catch (SQLException e) {
			
			e.printStackTrace();
			return false;
		}
	}
}
