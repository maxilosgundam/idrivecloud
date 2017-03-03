package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

public class MoveReservationClass {
	
	private String date;
	private String hours;
	private String minutes;
	private String time;
	private String resID;
	
	public String getResID() {
		return resID;
	}
	public void setResID(String resID) {
		this.resID = resID;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getHours() {
		return hours;
	}
	public void setHours(String hours) {
		this.hours = hours;
	}
	public String getMinutes() {
		return minutes;
	}
	public void setMinutes(String minutes) {
		this.minutes = minutes;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	public boolean moveReservation(Connection connection){
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			
			Date resDate =  dateFormat.parse(date);
			java.sql.Date sqlDate = new java.sql.Date(resDate.getTime());
			
//			This code updates the reservations to the "move" date/time instantly
//			String query = "UPDATE reservations SET tripDate = ?, departure = ?, statusID = ? WHERE reservationID = ?";
//			PreparedStatement pstmt = connection.prepareStatement(query);
//			pstmt.setDate(1, sqlDate);
//			pstmt.setString(2, hours + " " + minutes + " " + time);
//			pstmt.setInt(3, 6);
//			pstmt.setInt(4, Integer.parseInt(resID));
//			pstmt.executeUpdate();
			
			String query = "INSERT INTO moveRequest (moveResID, moveDate, moveTime, moveStatus) values (?,?,?,?)";
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, Integer.parseInt(resID));
			pstmt.setDate(2, sqlDate);
			pstmt.setString(3, hours + " " + minutes + " " + time);
			pstmt.setInt(4, 1);
			pstmt.executeUpdate();
			
			return true;
			
		} catch (ParseException | SQLException e) {
			
			e.printStackTrace();
			
			//Failed to update reservation
			return false;
		}
		
	}
	
	public boolean checkAvailable(Connection connection){
		Calendar calendar = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		try {
			DateFormat dayOfWeek = new SimpleDateFormat("EEE");
			calendar.setTime(dateFormat.parse(date));
			String day = dayOfWeek.format(calendar.getTime());
			
			String totalVehicles = "SELECT * FROM cars WHERE carStatusID = ?";
			PreparedStatement pstmt = connection.prepareStatement(totalVehicles);
			pstmt.setInt(1, 1);
			ResultSet rs = pstmt.executeQuery();
			
			int total = 0;
			while(rs.next()){
				String plate = rs.getString("plateNum");
				
				if(day.equals("Mon")){
					
					if(plate.charAt(plate.length() - 1) == '1' || plate.charAt(plate.length() - 1) == '2'){
						
					} else {
						
						total++;
					}
					
				} else if(day.equals("Tue")){
					
					if(plate.charAt(plate.length() - 1) == '3' || plate.charAt(plate.length() - 1) == '4'){
						
					} else {
						
						total++;
					}
					
				} else if(day.equals("Wed")){
					
					if(plate.charAt(plate.length() - 1) == '5' || plate.charAt(plate.length() - 1) == '6'){
						
					} else {
						
						total++;
					}
					
				} else if(day.equals("Thu")){
					
					if(plate.charAt(plate.length() - 1) == '7' || plate.charAt(plate.length() - 1) == '8'){
						
					} else {
						
						total++;
					}
					
				} else if(day.equals("Fri")){
					
					if(plate.charAt(plate.length() - 1) == '9' || plate.charAt(plate.length() - 1) == '0'){
						
					} else {
						
						total++;
					}
					
				} else {
					
					total++;
					
				}
				
			}
			
			System.out.println("total " + total);
			Date resDate =  dateFormat.parse(date);
			java.sql.Date sqlDate = new java.sql.Date(resDate.getTime());
			
			String currentVehicles = "SELECT * FROM reservations WHERE tripdate = ? AND statusID = ?";
			PreparedStatement pstmt2 = connection.prepareStatement(currentVehicles);
			pstmt2.setDate(1, sqlDate);
			pstmt2.setInt(2, 3);
			ResultSet rs2 = pstmt2.executeQuery();
			
			int current = 0;
			while(rs2.next()){
				current++;
			}
			System.out.println("current " + current);
			if(total-current <= 0){
				// no available
				System.out.println("no available" + (total-current));
				return false;
			} else {
				System.out.println("available" + (total-current));
				return true;
			}
			
			
			
		} catch (ParseException | SQLException e1) {
			
			e1.printStackTrace();
		}
		return false;
	}
}
