package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CancelClass {
	private int resID;

	public int getResID() {
		return resID;
	}

	public void setResID(int resID) {
		this.resID = resID;
	}
	
	public boolean cancelReservation(Connection connection){
		
		try{
			
			String cancelReservation = "UPDATE reservations SET statusID = ? WHERE reservationID = ?";
			PreparedStatement pstmt = connection.prepareStatement(cancelReservation);
			pstmt.setInt(1, 5);
			pstmt.setInt(2, resID);
			pstmt.executeUpdate();
			
			String checkLocation = "SELECT trackingID FROM reservations WHERE reservationID = ?";
			PreparedStatement pstmt2 = connection.prepareStatement(checkLocation);
			pstmt2.setInt(1, resID);
			ResultSet rs = pstmt2.executeQuery();
			
			int location=0;
			
			while(rs.next()){
				location = rs.getInt("trackingID");
				
				if(location == 1){
					String getDivisionHeadEmail = "SELECT e.email FROM employee e INNER JOIN departments d, reservations r WHERE r.departmentID = d.departmentID AND d.divisionHead = e.employeeID AND r.reservationID = ?";
					PreparedStatement pstmt3 = connection.prepareStatement(getDivisionHeadEmail);
					pstmt3.setInt(1, resID);
					ResultSet rs1 = pstmt2.executeQuery();
					
					while(rs1.next()){
						String sendEmailTo = rs1.getString("email");
						//This is the email of the division head the employee is under to.
						//Send an email to division head that reservation was cancelled.
						
						return true;
					}
					
					//The email was not located. Check database if email exists.
					System.out.println("Email not located");
					return false;
					
				} else if(location == 2){
					//This is the email of the administrator.
					//Send an email to the administrator that the reservation was cancelled.
					
					return true;
				}
			}
			
			//The location is invalid. Check database if trackingID assigned exists.
			System.out.println(location);
			System.out.println("location is invalid");
			return false;
			
			
			
		} catch (SQLException sqle){
			sqle.printStackTrace();
		}
		System.out.println("false sa dulo");
		return false;
	}
}
