package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DriverAndVehicleUpdater {
	
	public void updateReservation(Connection connection, int resID, int carID, String licenseID, String token){
		try{
			String query ="UPDATE reservations SET carID = ? , licenseID = ? , token = ? , statusID = 3 WHERE reservationID = ?";
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, carID);
			pstmt.setString(2, licenseID);
			pstmt.setString(3, token);
			pstmt.setInt(4, resID);
			pstmt.executeUpdate();
		}catch(SQLException sqle){
			System.out.println(sqle);
		}
	}
}
