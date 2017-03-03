package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReservationUpdater {
	
	public boolean approvedReservationManager(Connection connection, int resID){
		try{
			String query ="UPDATE reservations SET trackingID = 2 WHERE reservationID = ?";
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, resID);
			pstmt.executeUpdate();
			return true;
		}catch(SQLException sqle){
			System.out.println(sqle);
			return false;
		}
	}
	
	public boolean deniedReservation(Connection connection, int resID, String reason){
		try{
			String query ="UPDATE reservations SET trackingID = 3 , statusID = 2 , denialReason = ? WHERE reservationID = ?";
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setString(1, reason);
			pstmt.setInt(2, resID);
			pstmt.executeUpdate();
			return true;
		}catch(SQLException sqle){
			System.out.println(sqle);
			return false;
		}
	}
	
	public boolean approvedReservationAdmin(Connection connection, int resID){
		try{
			String query ="UPDATE reservations SET trackingID = 4 WHERE reservationID = ?";
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, resID);
			pstmt.executeUpdate();
			return true;
		}catch(SQLException sqle){
			System.out.println(sqle);
			return false;
		}
	}
}
