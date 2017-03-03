package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EventModel {
	private String eventName;
	private String eventDate;
	
	
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public String getEventDate() {
		return eventDate;
	}
	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}
	
	public ResultSet getEventReservations(Connection connection){
		try{
			String query = "SELECT c.plateNum, r.tripDate, r.departure, r.carID FROM reservations r INNER JOIN cars c WHERE r.statusID = 3 AND c.id = r.carID";
			PreparedStatement pstmt = connection.prepareStatement(query);
			ResultSet eventReservations = pstmt.executeQuery();
			
			return eventReservations;
		}catch(SQLException sqle){
			System.out.println(sqle);
		}
		return null;
	}
	
}
