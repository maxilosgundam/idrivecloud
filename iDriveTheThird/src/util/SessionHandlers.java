package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.DepartmentClass;
import model.DriverClass;
import model.ReservationViewer;
import model.ResultSetMethods;
import model.VehicleClass;

public class SessionHandlers {
	
	public List<ReservationViewer> adminReservationsSession(Connection connection){
		try{
			ReservationViewer admin = new ReservationViewer();
			ResultSet rs1 = admin.adminReservations(connection);
			List<ReservationViewer> reservationDetails = new ArrayList<ReservationViewer>();
	
			while(rs1.next()){
				ReservationViewer details = new ReservationViewer();
				details.setReservationID(rs1.getInt("reservationID"));
				details.setTripDate(rs1.getDate("tripDate"));
				details.setDeparture(rs1.getString("departure"));
				details.setDestination(rs1.getString("destination"));
				details.setPurpose(rs1.getString("travelPurpose"));
				details.setPassengers(rs1.getString("passengers"));
				details.setPassengerNum(rs1.getInt("passengerNum"));
				details.setDepartmentID(rs1.getInt("departmentID"));
				details.setEmployeeID(rs1.getInt("employeeID"));
				details.executeProcess(connection);
				reservationDetails.add(details);
			}
			return reservationDetails;
		}catch(SQLException sqle){
			System.out.println(sqle);
			sqle.printStackTrace();
		}
		return null;
	}
	
	public List<DriverClass> adminDriverSession(Connection connection){
		try{
			ResultSetMethods drivers = new ResultSetMethods();
			ResultSet driverRs = drivers.driverSession(connection);
			List<DriverClass> driverModel = new ArrayList<DriverClass>();
	
			while(driverRs.next()){
				DriverClass details = new DriverClass();
				details.setLicenseID(driverRs.getString("licenseID"));
				details.setEmployeeID(driverRs.getInt("employeeID"));
				
				int empID = driverRs.getInt("employeeID");
				ResultSet emp = drivers.employeeSession(connection, empID);
				
				if(emp.next()){
					details.setFullName(emp.getString("firstName")+ " " +emp.getString("lastName"));
				}
				
				driverModel.add(details);
			}
			return driverModel;
		}catch(SQLException sqle){
			System.out.println(sqle);
			sqle.printStackTrace();
		}
		return null;
	}
	
	public List<VehicleClass> adminCarSession(Connection connection){
		try{
			ResultSetMethods cars = new ResultSetMethods();
			ResultSet carRs = cars.carSession(connection);
			List<VehicleClass> carsModel = new ArrayList<VehicleClass>();

			while(carRs.next()){
				VehicleClass details = new VehicleClass();
				int statID = carRs.getInt("carStatusID");
				details.carStatusSession(connection, statID);
				details.setCarStatusID(carRs.getInt("carStatusID"));
				details.setColor(carRs.getString("color"));
				details.setManufacturer(carRs.getString("manufacturer"));
				details.setMaxCap(carRs.getInt("maxCapacity"));
				details.setModel(carRs.getString("model"));
				details.setPlateNum(carRs.getString("plateNum"));
				details.setYearMake(carRs.getString("yearMake"));
				carsModel.add(details);
			}
			return carsModel;
		}catch(SQLException sqle){
			System.out.println(sqle);
			sqle.printStackTrace();
		}
		return null;
	}
	
	public List<DepartmentClass> adminDeptSession(Connection connection){
		try{
			ResultSetMethods dept = new ResultSetMethods();
			ResultSet deptRs = dept.departmentSession(connection);

			List<DepartmentClass> deptModel = new ArrayList<DepartmentClass>();

			while(deptRs.next()){
				DepartmentClass details = new DepartmentClass();
				details.setDepartmentID(deptRs.getInt("departmentID"));
				details.setDepartmentName(deptRs.getString("departmentName"));
				details.setDivisionHead(deptRs.getInt("divisionHead"));
				
				int empID = deptRs.getInt("divisionHead");
				ResultSet emp = dept.employeeSession(connection, empID);
				
				if(emp.next()){
					details.setFullName(emp.getString("firstName")+ " " +emp.getString("lastName"));
				}
				
				deptModel.add(details);
			}
			return deptModel;
		}catch(SQLException sqle){
			System.out.println(sqle);
			sqle.printStackTrace();
		}
		return null;
	}
	
	public List<ReservationViewer> empReservationSession(Connection connection ,int acc){
		try{
			ReservationViewer emp = new ReservationViewer();
			ResultSet rs2 = emp.employeeReservations(connection, acc);
			List<ReservationViewer> reservationDetails = new ArrayList<ReservationViewer>();
			
			while(rs2.next()){
				System.out.println(rs2.getInt("reservationID"));
				ReservationViewer details = new ReservationViewer();
				details.setReservationIDEmp(rs2.getInt("reservationID"));
				details.setTripDateEmp(rs2.getDate("tripDate"));
				details.setDepartureEmp(rs2.getString("departure"));
				details.setDestinationEmp(rs2.getString("destination"));
				details.setPurposeEmp(rs2.getString("travelPurpose"));
				details.setPassengersEmp(rs2.getString("passengers"));
				details.setPassengerNumEmp(rs2.getInt("passengerNum"));
				details.setDepartmentID(rs2.getInt("departmentID"));
				details.setEmployeeID(rs2.getInt("employeeID"));
				details.setTrackId(rs2.getInt("trackingID"));
				details.executeProcess(connection);
				reservationDetails.add(details);

			}
			return reservationDetails;
		}catch(SQLException sqle){
			System.out.println(sqle);
			sqle.printStackTrace();
		}
		return null;
	}
	
	public List<VehicleClass> empCarSession(Connection connection){
		try{
			ResultSetMethods cars = new ResultSetMethods();
			ResultSet carRs = cars.carSession(connection);
			List<VehicleClass> carsModel = new ArrayList<VehicleClass>();
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date dateToday = new Date();
			String finalDateToday = dateFormat.format(dateToday).toString();
			
			while(carRs.next()){
				VehicleClass details = new VehicleClass();
				details.carStatusSession(connection, carRs.getInt("carStatusID"));
				details.setPlateNum(carRs.getString("plateNum"));
				details.checkAvailable(connection, finalDateToday);
				details.setCarStatusID(carRs.getInt("carStatusID"));
				details.setColor(carRs.getString("color"));
				details.setManufacturer(carRs.getString("manufacturer"));
				details.setMaxCap(carRs.getInt("maxCapacity"));
				details.setModel(carRs.getString("model"));
				details.setYearMake(carRs.getString("yearMake"));
				carsModel.add(details);
			}
			return carsModel;
		}catch(SQLException sqle){
			System.out.println(sqle);
			sqle.printStackTrace();
		}
		return null;
	}
	
	public List<ReservationViewer> managerReservationSession(Connection connection ,int deptName){
		try{
			ReservationViewer manager = new ReservationViewer();
			ResultSet rs1 = manager.managerReservations(connection, deptName);
			List<ReservationViewer> reservationDetails = new ArrayList<ReservationViewer>();

			while(rs1.next()){
				System.out.println(rs1.getInt("reservationID"));
				ReservationViewer details = new ReservationViewer();
				details.setReservationID(rs1.getInt("reservationID"));
				details.setTripDate(rs1.getDate("tripDate"));
				details.setDeparture(rs1.getString("departure"));
				details.setDestination(rs1.getString("destination"));
				details.setPurpose(rs1.getString("travelPurpose"));
				details.setPassengers(rs1.getString("passengers"));
				details.setPassengerNum(rs1.getInt("passengerNum"));
				details.setDepartmentID(rs1.getInt("departmentID"));
				details.setEmployeeID(rs1.getInt("employeeID"));
				details.executeProcess(connection);
				reservationDetails.add(details);

			}
			return reservationDetails;
		}catch(SQLException sqle){
			System.out.println(sqle);
			sqle.printStackTrace();
		}
		return null;
	}
	
	public List<ReservationViewer> managerPendingReservationSession(Connection connection ,int acc){ // same as empReservationSession method, please change!
		try{
			ReservationViewer manager = new ReservationViewer();
			ResultSet rs2 = manager.employeeReservations(connection, acc);
			List<ReservationViewer> reservationDetails2 = new ArrayList<ReservationViewer>();

			while(rs2.next()){
				System.out.println(rs2.getInt("reservationID"));
				ReservationViewer details2 = new ReservationViewer();
				details2.setReservationIDEmp(rs2.getInt("reservationID"));
				details2.setTripDateEmp(rs2.getDate("tripDate"));
				details2.setDepartureEmp(rs2.getString("departure"));
				details2.setDestinationEmp(rs2.getString("destination"));
				details2.setPurposeEmp(rs2.getString("travelPurpose"));
				details2.setPassengersEmp(rs2.getString("passengers"));
				details2.setPassengerNumEmp(rs2.getInt("passengerNum"));
				details2.setDepartmentID(rs2.getInt("departmentID"));
				details2.setEmployeeID(rs2.getInt("employeeID"));
				details2.setTrackId(rs2.getInt("trackingID"));
				details2.executeProcess(connection);
				reservationDetails2.add(details2);

			}
			return reservationDetails2;
		}catch(SQLException sqle){
			System.out.println(sqle);
			sqle.printStackTrace();
		}
		return null;
	}
	
	public List<ReservationViewer> viewReservationQR(Connection connection, String token){
		try{
			System.out.println("This is the token: "+token);
			String query ="SELECT * FROM reservations WHERE token = ?";
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setString(1, token);
			ResultSet rs = pstmt.executeQuery();
			
			List<ReservationViewer> reservationDetails = new ArrayList<ReservationViewer>();
			
			int empID = 0;
			int deptID = 0;
			String license = "";
			String plateNum = "";
			if(rs.next()){
				ReservationViewer details = new ReservationViewer();
				empID = rs.getInt("employeeID");
				deptID = rs.getInt("departmentID");
				license = rs.getString("licenseID");
				plateNum = rs.getString("plateNum");
				details.setTripDate(rs.getDate("tripDate"));
				details.setDeparture(rs.getString("departure"));
				details.setDestination(rs.getString("destination"));
				details.setPurpose(rs.getString("travelPurpose"));
				details.setPassengers(rs.getString("passengers"));
				details.setPassengerNum(rs.getInt("passengerNum"));

				String getEmpNameQuery ="SELECT * FROM employee WHERE employeeID = ?";
				PreparedStatement pstmt2 = connection.prepareStatement(getEmpNameQuery);
				pstmt2.setInt(1, empID);
				ResultSet rs2 = pstmt2.executeQuery();
				
				if(rs2.next()){
					details.setName(rs2.getString("firstName")+" "+rs2.getString("lastName"));
				}else{
					return null;
				}
				
				String getDeptNameQuery ="SELECT * FROM departments WHERE departmentID = ?";
				PreparedStatement pstmt3 = connection.prepareStatement(getDeptNameQuery);
				pstmt3.setInt(1, deptID);
				ResultSet rs3 = pstmt3.executeQuery();
				
				if(rs3.next()){
					details.setDeptName(rs3.getString("departmentName"));
				}else{
					return null;
				}
				int driverEmpID = 0;
				String getDriverIDQuery ="SELECT * FROM driver WHERE licenseID = ?";
				PreparedStatement pstmt4 = connection.prepareStatement(getDriverIDQuery);
				pstmt4.setString(1, license);
				ResultSet rs4 = pstmt4.executeQuery();
				
				if(rs4.next()){
					driverEmpID = rs4.getInt("employeeID");
				}else{
					return null;
				}
				System.out.println(driverEmpID);
				String getDriverNameQuery ="SELECT * FROM employee WHERE employeeID = ?";
				PreparedStatement pstmt5 = connection.prepareStatement(getDriverNameQuery);
				pstmt5.setInt(1, driverEmpID);
				ResultSet rs5 = pstmt5.executeQuery();
				// THERE IS PROBLEM HERE
				
				if(rs5.next()){
					details.setDriverName(rs5.getString("firstName")+" "+rs5.getString("lastName"));
				}else{
					return null;
				}
				
				String getCarNameQuery ="SELECT * FROM cars WHERE plateNum = ?";
				PreparedStatement pstmt6 = connection.prepareStatement(getCarNameQuery);
				pstmt6.setString(1, plateNum);
				ResultSet rs6 = pstmt6.executeQuery();
				
				if(rs6.next()){
					details.setCarName(rs6.getString("yearMake")+" "+rs6.getString("color")+" "+rs6.getString("manufacturer")+" "+rs6.getString("model"));
				}else{
					return null;
				}
				
				reservationDetails.add(details);
			}else{
				return null;
			}
			
			return reservationDetails;
		}catch(SQLException sqle){
			System.out.println(sqle);
		}
		return null;
	}
	
	public List<ReservationViewer> regularViewReservation(Connection connection, int id){
		try{
			System.out.println("This is the id: "+id);
			String query ="SELECT * FROM reservations WHERE reservationID = ?";
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
			
			List<ReservationViewer> reservationDetails = new ArrayList<ReservationViewer>();
			
			int empID = 0;
			int deptID = 0;
			String license = "";
			int carID = 0;
			if(rs.next()){
				ReservationViewer details = new ReservationViewer();
				empID = rs.getInt("employeeID");
				deptID = rs.getInt("departmentID");
				license = rs.getString("licenseID");
				carID = rs.getInt("carID");
				details.setTripDate(rs.getDate("tripDate"));
				details.setDeparture(rs.getString("departure"));
				details.setDestination(rs.getString("destination"));
				details.setPurpose(rs.getString("travelPurpose"));
				details.setPassengers(rs.getString("passengers"));
				details.setPassengerNum(rs.getInt("passengerNum"));
				details.setReservationID(id);

				String getEmpNameQuery ="SELECT * FROM employee WHERE employeeID = ?";
				PreparedStatement pstmt2 = connection.prepareStatement(getEmpNameQuery);
				pstmt2.setInt(1, empID);
				ResultSet rs2 = pstmt2.executeQuery();
				
				if(rs2.next()){
					details.setName(rs2.getString("firstName")+" "+rs2.getString("lastName"));
				}else{
					return null;
				}
				
				String getDeptNameQuery ="SELECT * FROM departments WHERE departmentID = ?";
				PreparedStatement pstmt3 = connection.prepareStatement(getDeptNameQuery);
				pstmt3.setInt(1, deptID);
				ResultSet rs3 = pstmt3.executeQuery();
				
				if(rs3.next()){
					details.setDeptName(rs3.getString("departmentName"));
				}else{
					return null;
				}
				int driverEmpID = 0;
				String getDriverIDQuery ="SELECT * FROM driver WHERE licenseID = ?";
				PreparedStatement pstmt4 = connection.prepareStatement(getDriverIDQuery);
				pstmt4.setString(1, license);
				ResultSet rs4 = pstmt4.executeQuery();
				
				if(rs4.next()){
					driverEmpID = rs4.getInt("employeeID");
				}else{
					return null;
				}
				System.out.println(driverEmpID);
				String getDriverNameQuery ="SELECT * FROM employee WHERE employeeID = ?";
				PreparedStatement pstmt5 = connection.prepareStatement(getDriverNameQuery);
				pstmt5.setInt(1, driverEmpID);
				ResultSet rs5 = pstmt5.executeQuery();
				// THERE IS PROBLEM HERE
				
				if(rs5.next()){
					details.setDriverName(rs5.getString("firstName")+" "+rs5.getString("lastName"));
				}else{
					return null;
				}
				
				String getCarNameQuery ="SELECT * FROM cars WHERE id = ?";
				PreparedStatement pstmt6 = connection.prepareStatement(getCarNameQuery);
				pstmt6.setInt(1, carID);
				ResultSet rs6 = pstmt6.executeQuery();
				
				if(rs6.next()){
					details.setCarName(rs6.getString("yearMake")+" "+rs6.getString("color")+" "+rs6.getString("manufacturer")+" "+rs6.getString("model"));
				}else{
					return null;
				}
				
				reservationDetails.add(details);
			}else{
				return null;
			}
			
			return reservationDetails;
		}catch(SQLException sqle){
			System.out.println(sqle);
		}
		return null;
	}
}
