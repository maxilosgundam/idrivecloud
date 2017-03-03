package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Validate {
	private String username;
	private String password;
	private String fName;
	private String lName;
	private String email;
	private int deptName;

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getfName() {
		return fName;
	}
	public void setfName(String fName) {
		this.fName = fName;
	}
	public String getlName() {
		return lName;
	}
	public void setlName(String lName) {
		this.lName = lName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getDeptName() {
		return deptName;
	}
	public void setDeptName(int deptName) {
		this.deptName = deptName;
	}
	public int accountChecker(Connection connection){
		try{
			String query = "SELECT * FROM employee WHERE employeeID = ? AND pass =  ?";
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setString( 1, getUsername());
//			pstmt.setString( 2, Security.encrypt(getPassword())); 
			pstmt.setString( 2, getPassword()); 
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()){
				setfName(rs.getString("firstName"));
				setlName(rs.getString("lastName"));
				setDeptName(rs.getInt("departmentName"));
				setEmail(rs.getString("email"));
				return rs.getInt("accountTypeID");
			}else{
				
				return 0;
			}
		}catch(SQLException sqle){
			return 0;
		}
	}
	
	public int emailChecker(Connection connection)
	{
		try{
			String query = "SELECT * FROM employee WHERE email = ?";
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setString( 1, getEmail());
			ResultSet rs =pstmt.executeQuery();
			if(rs.next()){
				return rs.getInt("accountTypeID");
			}else{
				
				return 0;
			}
		}catch(SQLException sqle){
			return 0;
		}
		}
	
	public boolean userValidate(){

		for (char userName:getUsername().toCharArray())
	    {
	        if (!Character.isDigit(userName)){
	        	return false;
	        }
	    }
	    return true;
	}
}