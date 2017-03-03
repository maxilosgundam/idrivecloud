package model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.sql.Date;

import util.EmailSender;

public class ReservationSender {
	private String date;
	private String timeHours;
	private String timeMinutes;
	private String timeOfDay;
	private String destination;
	private String purpose;
	private String numPassengers;
	private String passengers;
	private int empId;
	private String mailHost;
	private String gmail;
	private String mailPort;
	private String mailPortNumber;
	private String mailSSL;
	private String mailAuth;
	private String crest;
	private String crest2;
	private String logo;
	private String sender;
	private String password;
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTimeHours() {
		return timeHours;
	}
	public void setTimeHours(String timeHours) {
		this.timeHours = timeHours;
	}
	public String getTimeMinutes() {
		return timeMinutes;
	}
	public void setTimeMinutes(String timeMinutes) {
		this.timeMinutes = timeMinutes;
	}
	public String getTimeOfDay() {
		return timeOfDay;
	}
	public void setTimeOfDay(String timeOfDay) {
		this.timeOfDay = timeOfDay;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getNumPassengers() {
		return numPassengers;
	}
	public void setNumPassengers(String numPassengers) {
		this.numPassengers = numPassengers;
	}
	public String getPassengers() {
		return passengers;
	}
	public void setPassengers(String passengers) {
		if(passengers.isEmpty()){
			this.passengers = "None";
		}else{
			this.passengers = passengers;
		}
	}
	public int getEmpId() {
		return empId;
	}
	public void setEmpId(int empId) {
		this.empId = empId;
	}
	public String getMailHost() {
		return mailHost;
	}
	public void setMailHost(String mailHost) {
		this.mailHost = mailHost;
	}
	public String getGmail() {
		return gmail;
	}
	public void setGmail(String gmail) {
		this.gmail = gmail;
	}
	public String getMailPort() {
		return mailPort;
	}
	public void setMailPort(String mailPort) {
		this.mailPort = mailPort;
	}
	public String getMailPortNumber() {
		return mailPortNumber;
	}
	public void setMailPortNumber(String mailPortNumber) {
		this.mailPortNumber = mailPortNumber;
	}
	public String getMailSSL() {
		return mailSSL;
	}
	public void setMailSSL(String mailSSL) {
		this.mailSSL = mailSSL;
	}
	public String getMailAuth() {
		return mailAuth;
	}
	public void setMailAuth(String mailAuth) {
		this.mailAuth = mailAuth;
	}
	public String getCrest() {
		return crest;
	}
	public void setCrest(String crest) {
		this.crest = crest;
	}
	public String getCrest2() {
		return crest2;
	}
	public void setCrest2(String crest2) {
		this.crest2 = crest2;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean validateReservation(Connection connection){
		try{
			System.out.println("");
			SimpleDateFormat forDate = new SimpleDateFormat("yyyy-MM-dd");
		    	java.util.Date date = forDate.parse(getDate());
		    	java.sql.Date sqlDate = new Date(date.getTime());
			String departureFull = getTimeHours()+":"+getTimeMinutes()+" "+getTimeOfDay();
			
			String userSimilarReservationQuery = "SELECT * FROM reservations WHERE tripDate = ? AND departure = ? AND employeeID = ?";
			PreparedStatement pstmt = connection.prepareStatement(userSimilarReservationQuery);
			
			pstmt.setDate(1, sqlDate);
			pstmt.setString(2, departureFull);
			pstmt.setInt(3, empId);
			ResultSet rs = pstmt.executeQuery();
			int total = 0;
			while(rs.next()){
				total++;
			}
			
			if(total == 0){
				return true;
			}else{
				return false;
			}
		} catch (SQLException sqle){
			System.out.println(sqle);
		} catch (ParseException e){
			System.out.println(e);
		}
		System.out.println("Nag return false");
		return false;
	}
	
	public boolean validateAvailableDriverAndVehicleManager(Connection connection, int resID){
		try{
			
			String selectReservation = "SELECT * FROM reservations WHERE reservationID = ?";
			PreparedStatement selectTripDate = connection.prepareStatement(selectReservation);
			selectTripDate.setInt(1, resID);
			
			java.sql.Date sqlDate = null;
			
			ResultSet tripDateRS = selectTripDate.executeQuery();
	        while(tripDateRS.next()){
	        	sqlDate = tripDateRS.getDate("tripDate");
	        }
	        
	        if(sqlDate == null){
	        	return false;
	        }

	        String selectSimilarTripDate = "SELECT * FROM reservations WHERE tripDate = ? AND statusID = 3";
	        PreparedStatement pstmt1 = connection.prepareStatement(selectSimilarTripDate);
	        pstmt1.setDate(1, sqlDate);
	        
	        ResultSet similarTripDates = pstmt1.executeQuery();
	        int numSimilarTripDates = 0;
	        while(similarTripDates.next()){
	        	numSimilarTripDates++;
	        }
	        
	        String selectAllCars = "SELECT * FROM cars";
	        PreparedStatement pstmt2 = connection.prepareStatement(selectAllCars);
	        
	        ResultSet allCars = pstmt2.executeQuery();
	        int numAllCars = 0;
	        while(allCars.next()){
	        	numAllCars++;
	        }
	        
	        if(numSimilarTripDates == numAllCars){
	        	return false;
	        }else{
	        	return true;
	        }
		}catch(SQLException sqle){
			System.out.println(sqle);
		}
		return false;
	}
	
	public boolean validateAvailableDriverAndVehicle(Connection connection){
		try{
			SimpleDateFormat forDate = new SimpleDateFormat("yyyy-MM-dd");
	        java.util.Date date = forDate.parse(getDate());
	        java.sql.Date sqlDate = new Date(date.getTime());
	        
	        String selectSimilarTripDate = "SELECT * FROM reservations WHERE tripDate = ? AND statusID = 3";
	        PreparedStatement pstmt1 = connection.prepareStatement(selectSimilarTripDate);
	        pstmt1.setDate(1, sqlDate);
	        
	        ResultSet similarTripDates = pstmt1.executeQuery();
	        int numSimilarTripDates = 0;
	        while(similarTripDates.next()){
	        	numSimilarTripDates++;
	        }
	        
	        String selectAllCars = "SELECT * FROM cars";
	        PreparedStatement pstmt2 = connection.prepareStatement(selectAllCars);
	        
	        ResultSet allCars = pstmt2.executeQuery();
	        int numAllCars = 0;
	        while(allCars.next()){
	        	numAllCars++;
	        }
	        
	        if(numSimilarTripDates == numAllCars){
	        	return false;
	        }else{
	        	return true;
	        }
		}catch(SQLException sqle){
			System.out.println(sqle);
		} catch (ParseException pe) {
			System.out.println(pe);
		}
		return false;
	}
	
	public boolean carCodingChecker(Connection connection){
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
			java.util.Date resDate =  dateFormat.parse(date);
			Date sqlDate = new Date(resDate.getTime());
			
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
	
	public void sendReservation(Connection connection, int accType, int dept, String fullName){
		System.out.println("Pumasok dito");
		try{
			String query ="INSERT INTO reservations (tripDate, departure, destination, travelPurpose, passengers, passengerNum, employeeID, statusID, trackingID, departmentID) values (?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement pstmt = connection.prepareStatement(query);
			
			SimpleDateFormat forDate = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = forDate.parse(getDate());
            java.sql.Date sqlDate = new Date(date.getTime());
            
            String departureFull = getTimeHours()+":"+getTimeMinutes()+" "+getTimeOfDay();
            
            int tracking = 1;
            switch(accType){
	            case 1:
	            	break;
	            case 2:
	            	tracking = 2;
	            	break;
	            case 3:
	            	tracking = 4;
	            	break;
	            default:
	            	tracking = 1;
	            	break;
            }
            
			pstmt.setDate(1, sqlDate);
			pstmt.setString(2, departureFull);
			pstmt.setString(3, getDestination());
			pstmt.setString(4, getPurpose());
			pstmt.setString(5,getPassengers());
			pstmt.setInt(6, Integer.parseInt(getNumPassengers()));
			pstmt.setInt(7, getEmpId());
			pstmt.setInt(8, 1);
			pstmt.setInt(9, tracking);
			pstmt.setInt(10, dept);
			pstmt.executeUpdate();
			
			EmailSender emSend = new EmailSender();
			emSend.setSender(sender);
			emSend.setPassword(password);
			emSend.setMailHost(mailHost);
			emSend.setGmail(gmail);
			emSend.setMailPort(mailPort);
			emSend.setMailPortNumber(mailPortNumber);
			emSend.setMailSSL(mailSSL);
			emSend.setMailAuth(mailAuth);
			emSend.setCrest(crest);
			emSend.setCrest2(crest2);
			emSend.setLogo(logo);
			emSend.reservationSent(connection, accType, dept, fullName, date, timeHours, timeMinutes, timeOfDay, destination, numPassengers, purpose);
			
		}catch(SQLException sqle){
			System.out.println(sqle);
		} catch (ParseException pe) {
			System.out.println(pe);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void denyReservation(Connection connection, String reason, int resId){
		System.out.println("denyReservation activated");
		int employeeId = 0;
		String email = "";
		String fullName = "";
		String date = "";
		try{
			String query = "SELECT * from reservations WHERE reservationID  = ?";
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setInt( 1, resId);
			ResultSet rs =pstmt.executeQuery();
			if(rs.next()){
				employeeId = rs.getInt("employeeID");
				date = rs.getDate("tripDate").toString();
			}else{
				
			}
		}catch(SQLException sqle){
			System.err.println("SQLE error for 1st PreparedStatement");
			sqle.printStackTrace();
			
		}
		try{
			String query2 = "SELECT * from employee WHERE employeeID = ?";
			PreparedStatement pstmt2 = connection.prepareStatement(query2);
			pstmt2.setInt( 1, employeeId);
			ResultSet rs2 =pstmt2.executeQuery();
			if(rs2.next()){
				email = rs2.getString("email");
				String fName = rs2.getString("firstName");
				String lName = rs2.getString("lastName");
				fullName = fName + " " + lName;
			}else{
				
			}
		}catch(SQLException sqle){
			System.err.println("SQLE error for 2nd PreparedStatement");
			sqle.printStackTrace();
			
		}
		
			EmailSender emSend = new EmailSender();
			emSend.setSender(getSender());
			emSend.setPassword(getPassword());
			emSend.setMailHost(getMailHost());
			emSend.setGmail(getGmail());
			emSend.setMailPort(getMailPort());
			emSend.setMailPortNumber(getMailPortNumber());
			emSend.setMailSSL(getMailSSL());
			emSend.setMailAuth(getMailAuth());
			emSend.setCrest(getCrest());
			emSend.setCrest2(getCrest2());
			emSend.setLogo(getLogo());
			
			emSend.reservationDenied(email, date, reason, fullName);
	}
	
}