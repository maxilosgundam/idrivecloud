package model;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VehicleSender {
	private String manufacturer;
	private String yearManufactured;
	private String carModel;
	private String carColor;
	private String carPlate;
	private String carCapacity;
	private InputStream photo;
	
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getYearManufactured() {
		return yearManufactured;
	}
	public void setYearManufactured(String yearManufactured) {
		this.yearManufactured = yearManufactured;
	}
	public String getCarModel() {
		return carModel;
	}
	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}
	public String getCarColor() {
		return carColor;
	}
	public void setCarColor(String carColor) {
		this.carColor = carColor;
	}
	public String getCarPlate() {
		return carPlate;
	}
	public void setCarPlate(String carPlate) {
		this.carPlate = carPlate;
	}
	public String getCarCapacity() {
		return carCapacity;
	}
	public void setCarCapacity(String carCapacity) {
		this.carCapacity = carCapacity;
	}
	public InputStream getPhoto() {
		return photo;
	}
	public void setPhoto(InputStream photo) {
		this.photo = photo;
	}
	
	public boolean duplicateVehicle(Connection connection){
		try{
			
			String query ="SELECT * FROM cars WHERE plateNum = ?";
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setString(1, carPlate.toUpperCase());
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()){
				return true;
			}else{
				return false;
			}
			
		}catch(SQLException sqle){
			System.out.println(sqle);
			return true;
		}
	}
	
	private void sendVehicle(Connection connection){
		try{
			
			String query ="INSERT INTO cars (manufacturer, yearMake, model, color, plateNum, maxCapacity, images, carStatusId) values (?,?,?,?,?,?,?,?)";
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setString(1, manufacturer);
			pstmt.setString(2, yearManufactured);
			pstmt.setString(3, carModel);
			pstmt.setString(4, carColor);
			pstmt.setString(5, carPlate.toUpperCase());
			pstmt.setInt(6, Integer.parseInt(carCapacity));
			pstmt.setBlob(7, photo);
			pstmt.setInt(8, 1);
			pstmt.executeUpdate();
			
		}catch(SQLException sqle){
			System.out.println(sqle);
		}
	}
	
	public void executeProcess(Connection connection){
		sendVehicle(connection);
	}
	
	
}
