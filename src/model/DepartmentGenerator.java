package model;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;
public class DepartmentGenerator {
	private String department;
	private String employeeId;
	private InputStream photo;
	
	public InputStream getPhoto() {
		return photo;
	}
	public void setPhoto(InputStream photo) {
		this.photo = photo;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public void sendDepartment(Connection connection){
		try{
				
			int intEmpId = Integer.parseInt(employeeId);
			String query ="INSERT INTO departments (departmentName, divisionHead, images) values (?,?,?)";
			PreparedStatement pstmt = connection.prepareStatement(query);
            
			pstmt.setString(1, department.toUpperCase());
			pstmt.setInt(2, intEmpId);
			pstmt.setBlob(3, photo);
			pstmt.executeUpdate();
		}catch(SQLException sqle){
			System.out.println(sqle);
		} 
	}
	private int departmentSelector(Connection connection){
		try{
			
			int intEmpId = Integer.parseInt(employeeId);
			String query ="SELECT * FROM departments WHERE divisionHead = ?";
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, intEmpId);
			ResultSet rs = pstmt.executeQuery();
			int deptID=0;
			while(rs.next()){
				deptID = rs.getInt("departmentID");
			}
			return deptID;
		}catch(SQLException sqle){
			System.out.println(sqle);
			return 0;
		}
	}
	private void employeeToManager(Connection connection, int deptId){
		try{
			int intEmpId = Integer.parseInt(employeeId);
			String query ="UPDATE employee SET departmentName = ? , accountTypeID = 2 WHERE employeeID = ?";
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, deptId);
			pstmt.setInt(2, intEmpId);
			pstmt.executeUpdate();
		}catch(SQLException sqle){
			sqle.printStackTrace();
		} 
	}
	public boolean duplicateDepartment(Connection connection){
		try{
			String query ="SELECT * FROM departments WHERE departmentName = ?";
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setString(1, department.toUpperCase());
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()){
				return true;
			}else{
				return false;
			}
		}catch(SQLException sqle){
			System.out.println(sqle);
			return false;
		}
	}
	public boolean existingEmployee(Connection connection){
		try{
			int intEmpId = Integer.parseInt(employeeId);
			String query ="SELECT * FROM employee WHERE accountTypeID = 1 AND employeeID = ?";
			System.out.println(intEmpId);
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, intEmpId);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()){
				return true;
			}else{
				return false;
			}
		}catch(SQLException sqle){
			System.out.println(sqle);
			return false;
		}
	}
	public void executeProcess(Connection connection){
		int deptID = departmentSelector(connection);
		employeeToManager(connection, deptID);
	}
//	public boolean employeeChecker(Connection connection){
//		try{
//			int intEmpId = Integer.parseInt(employeeId);
//			String query ="SELECT * FROM employee WHERE employeeID = ? AND accountTypeID = 1";
//			PreparedStatement pstmt = connection.prepareStatement(query);
//			pstmt.setInt(1, intEmpId);
//			ResultSet rs = pstmt.executeQuery();
//			if(rs.next()){
//				return true;
//			}else{
//				return false;
//			}
//		}catch(SQLException sqle){
//			System.out.println(sqle);
//			return false;
//		}
//	}
}
