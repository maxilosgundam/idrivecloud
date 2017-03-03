package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class VehicleClass {
	String plateNum;
	String manufacturer;
	String yearMake;
	String model;
	String color;
	int maxCap;
	int carStatusID;
	String statusName;
	String availability;
	public String getPlateNum() {
		return plateNum;
	}
	public void setPlateNum(String plateNum) {
		this.plateNum = plateNum;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getYearMake() {
		return yearMake;
	}
	public void setYearMake(String yearMake) {
		this.yearMake = yearMake;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public int getMaxCap() {
		return maxCap;
	}
	public void setMaxCap(int maxCap) {
		this.maxCap = maxCap;
	}
	public int getCarStatusID() {
		return carStatusID;
	}
	public void setCarStatusID(int carStatusID) {
		this.carStatusID = carStatusID;
	}
	public String getAvailability() {
		return availability;
	}
	public void setAvailability(String availability) {
		this.availability = availability;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	
	public void carStatusSession(Connection connection, int statID){
		try{
			String query ="SELECT * FROM carstatus WHERE carStatusID = ?";
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, statID);
			ResultSet cars = pstmt.executeQuery();

			while(cars.next()){
				setStatusName(cars.getString("carStatusType"));
			}
		}catch(SQLException sqle){
			System.out.println(sqle);
		}
	}
	
	public void checkAvailable(Connection connection, String date){
		Calendar calendar = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		try {
			DateFormat dayOfWeek = new SimpleDateFormat("EEE");
			calendar.setTime(dateFormat.parse(date));
			String day = dayOfWeek.format(calendar.getTime());

			String plate = getPlateNum();
			
			if(day.equals("Mon")){
				
				if(plate.charAt(plate.length() - 1) == '1' || plate.charAt(plate.length() - 1) == '2'){
					setAvailability("Unavailable");
				} else {
					
					setAvailability("Available");
				}
				
			} else if(day.equals("Tue")){
				
				if(plate.charAt(plate.length() - 1) == '3' || plate.charAt(plate.length() - 1) == '4'){
					setAvailability("Unavailable");
				} else {
					
					setAvailability("Available");
				}
				
			} else if(day.equals("Wed")){
				
				if(plate.charAt(plate.length() - 1) == '5' || plate.charAt(plate.length() - 1) == '6'){
					setAvailability("Unavailable");
				} else {
					
					setAvailability("Available");
				}
				
			} else if(day.equals("Thu")){
				
				if(plate.charAt(plate.length() - 1) == '7' || plate.charAt(plate.length() - 1) == '8'){
					setAvailability("Unavailable");
				} else {
					
					setAvailability("Available");
				}
				
			} else if(day.equals("Fri")){
				
				if(plate.charAt(plate.length() - 1) == '9' || plate.charAt(plate.length() - 1) == '0'){
					setAvailability("Unavailable");
				} else {
					
					setAvailability("Available");
				}
				
			} else {
				
				setAvailability("Available");
				
			}
			;
			Date resDate =  dateFormat.parse(date);
			java.sql.Date sqlDate = new java.sql.Date(resDate.getTime());
			String getCarID = "SELECT * FROM cars WHERE plateNum = ?";
			PreparedStatement pstmtCar = connection.prepareStatement(getCarID);
			pstmtCar.setString(1, plate);
			ResultSet carRs = pstmtCar.executeQuery();
			int carID = 0;
			while(carRs.next()){
				carID = carRs.getInt("id");
			}
			
			String currentVehicles = "SELECT * FROM reservations WHERE tripdate = ? AND statusID = ? AND carID = ?";
			PreparedStatement pstmt2 = connection.prepareStatement(currentVehicles);
			pstmt2.setDate(1, sqlDate);
			pstmt2.setInt(2, 3);
			pstmt2.setInt(3, carID);
			ResultSet rs2 = pstmt2.executeQuery();
			
			if(rs2.next()){
				setAvailability("Unavailable");
			}
			
			
		} catch (ParseException | SQLException e1) {
			
			e1.printStackTrace();
		}
	}
}
