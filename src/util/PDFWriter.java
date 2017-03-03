package util;

import java.awt.Desktop;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.sql.Date;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.io.IOException;
import com.itextpdf.io.image.ImageDataFactory;

import model.ReservationViewer;
import model.VehicleClass;

public class PDFWriter {
    
	private int empId;
	private int resId;
	private String location;
	private String requesterName;
	private String divisionheadName;
	private String driverName;
	private List<ReservationViewer> reservationDetails;
	private List<VehicleClass> carsModel;
	private ResultSet driverTable;
	private ResultSet carTable;
	private String carName;
	private String filePath;
	private String fileName;
	
	
	
	public String getCarName() {
		return carName;
	}

	public void setCarName(String carName) {
		this.carName = carName;
	}

	public ResultSet getCarTable() {
		return carTable;
	}

	public void setCarTable(ResultSet carTable) {
		this.carTable = carTable;
	}

	public ResultSet getDriverTable() {
		return driverTable;
	}

	public void setDriverTable(ResultSet driverTable) {
		this.driverTable = driverTable;
	}

	public List<ReservationViewer> getReservationDetails() {
		return reservationDetails;
	}

	public void setReservationDetails(List<ReservationViewer> reservationDetails) {
		this.reservationDetails = reservationDetails;
	}
	
	public List<VehicleClass> getCarsModel() {
		return carsModel;
	}

	public void setCarsModel(List<VehicleClass> carsModel) {
		this.carsModel = carsModel;
	}
	
	public String getLocation(){
		return location;
	}
	
	public void setLocation(String location){
		this.location = location;
	}

	public String getDivisionheadName() {
		return divisionheadName;
	}

	public void setDivisionheadName(String divisionheadName) {
		this.divisionheadName = divisionheadName;
	}

	public String getRequesterName() {
		return requesterName;
	}

	public void setRequesterName(String requesterName) {
		this.requesterName = requesterName;
	}
	
	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}
	
	public int getResId() {
		return resId;
	}

	public void setResId(int resId) {
		this.resId = resId;
	}
	
	public String getFilePath() {
		return filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public void resultSetGetEmpID(Connection connection){
		try {
			if(getDriverTable().next()){
				setEmpId(getDriverTable().getInt("employeeID"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void resultSetGetCarName(Connection connection){
		try {
			if(getCarTable().next()){
				setCarName(getCarTable().getString("yearMake")+" "+getCarTable().getString("color")+" "+getCarTable().getString("manufacturer")+" "+getCarTable().getString("model"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void driverName(Connection connection){
		
		try{
			String query ="SELECT emp.firstName, emp.lastName FROM employee emp WHERE emp.employeeID = ?";
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, getEmpId());
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()){
				//setDepartment(rs.getString("departmentName"));
				String fName = rs.getString("firstName");
				String lName = rs.getString("lastName");
				String fullName = fName + " " +lName;
				setDriverName(fullName);
			}else{

			}
		}catch(SQLException sqle){
			System.out.println(sqle);
			System.out.println("requester name exception omg");
		}
	}
	
	
	public void requesterName(Connection connection){
						
		try{
			String query ="SELECT emp.firstName, emp.lastName FROM employee emp INNER JOIN reservations r "
					+ "WHERE emp.employeeID = r.employeeID AND r.reservationID = ?";
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, getResId());
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()){
				//setDepartment(rs.getString("departmentName"));
				String fName = rs.getString("firstName");
				String lName = rs.getString("lastName");
				String fullName = fName + " " +lName;
				setRequesterName(fullName);
			}else{

			}
		}catch(SQLException sqle){
			System.out.println(sqle);
			System.out.println("requester name exception omg");
		}
	}
	
	public void divisionHeadName(Connection connection){
		
		try{
			String query ="SELECT * FROM reservations WHERE reservationID = ?";
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, getResId());
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()){
				//setDepartment(rs.getString("departmentName"));
				int deptID = rs.getInt("departmentID");
				
				String query2 = "SELECT * FROM employee WHERE departmentName = ? AND accountTypeID = 2";
				PreparedStatement pstmt2 = connection.prepareStatement(query2);
				pstmt2.setInt(1, deptID);
				ResultSet rs2 = pstmt2.executeQuery();
				
				if(rs2.next()){
					setDivisionheadName(rs2.getString("firstName")+" "+rs2.getString("lastName"));
				}else{
					
				}
			}else{

			}
		}catch(SQLException sqle){
			System.out.println(sqle);
			System.out.println("division head name exception omg");
		}
	}

	public void generatePDF(){
		
    	String departure = reservationDetails.get(0).getDeparture();
		String destination = reservationDetails.get(0).getDestination();
		String purpose = reservationDetails.get(0).getPurpose();
		String passengers = reservationDetails.get(0).getPassengers();
		Date tripDate = reservationDetails.get(0).getTripDate();
//		String carModel = carsModel.get(0).getModel();
//		String carColor = carsModel.get(0).getColor();
//		String carMake = carsModel.get(0).getYearMake();
		
		try {
    		String fileLocation = location+resId +".pdf";
    		PdfDocument pdf = new PdfDocument(new PdfWriter(fileLocation));
            Document document = new Document(pdf);
            File file = new File(location+resId +".pdf");
            Table columnOne = new Table(1).setWidth(400).setTextAlignment(TextAlignment.CENTER).setHorizontalAlignment(HorizontalAlignment.CENTER);
            Table columnTwo = new Table(2).setWidth(400).setTextAlignment(TextAlignment.CENTER).setHorizontalAlignment(HorizontalAlignment.CENTER);;
            Table columnThree = new Table(3).setWidth(400).setTextAlignment(TextAlignment.CENTER).setHorizontalAlignment(HorizontalAlignment.CENTER);
            Table columnFour = new Table(4).setWidth(400).setTextAlignment(TextAlignment.CENTER).setHorizontalAlignment(HorizontalAlignment.CENTER);
            Table secondColumnOne = new Table(1).setWidth(400).setTextAlignment(TextAlignment.CENTER).setHorizontalAlignment(HorizontalAlignment.CENTER);
            Table secondColumnTwo = new Table(2).setWidth(400).setTextAlignment(TextAlignment.CENTER).setHorizontalAlignment(HorizontalAlignment.CENTER);;
            Table secondColumnThree = new Table(3).setWidth(400).setTextAlignment(TextAlignment.CENTER).setHorizontalAlignment(HorizontalAlignment.CENTER);
            Table secondColumnFour = new Table(4).setWidth(400).setTextAlignment(TextAlignment.CENTER).setHorizontalAlignment(HorizontalAlignment.CENTER);
            Table table1 = new Table(65); // 3 columns.
          
            Image image = new Image(ImageDataFactory.create("qrcode.png"));
            
            columnOne.addCell(new Cell().add("Trip Ticket Form").setFontSize(13));
            columnOne.addCell(new Cell().add("To be filled out by Facilities Dept").setFontSize(10).setItalic());
            columnThree.addCell("Date Of Trip: " + tripDate).addCell("Assigned Vehicle: " + getCarName()).addCell("Assigned Driver: " + getDriverName()).setFontSize((float) 7.5);
            columnThree.addCell(new Cell(1,70).add("To be filled out by the guard on duty before the vehicle leaves the premises.").setFontSize(5).setItalic());
            columnThree.addCell(new Cell().add("Time of Departure").setBold()).addCell(new Cell().add("Guard on Duty").setBold()).addCell(new Cell().add("Signature").setBold());
            columnThree.addCell(departure).addCell(new Cell().setHeight(20).add("")).addCell("");
            columnThree.addCell(new Cell().add("To be filled out by the requesting department.").setItalic().setFontSize(5)).addCell(new Cell().add("To be filled out by the requesting department.").setItalic().setFontSize(5)).addCell(new Cell().add("To be filled out by the requesting department.").setItalic().setFontSize(5));
            columnThree.addCell(new Cell().add("Purpose of Travel").setBold()).addCell(new Cell().add("Destination").setBold()).addCell(new Cell().add("Passenger/s").setBold());
            columnThree.addCell(new Cell().add(purpose)).addCell(new Cell().add(destination)).addCell(new Cell().add(passengers));
            columnThree.addCell(new Cell(1,70).add("(To be accomplished by the requesting department)").setFontSize(5).setItalic());
            columnThree.addCell(new Cell().add("Requested by:").setBold()).addCell(new Cell().add("Approved by:").setBold()).addCell(new Cell().add("Received by:").setBold());
            columnThree.addCell(new Cell().add(requesterName)).addCell(new Cell().add(getDivisionheadName())).addCell(new Cell().add("").setBold());
            columnThree.addCell(new Cell().add("Name and Signature").setBold()).addCell(new Cell().add("Department/Division Head").setBold()).addCell(new Cell().add("Facilities Dept").setBold());
            columnThree.addCell(new Cell(1,70).add(image));
            
            table1.addCell(new Cell(1, 100).add("Trip Ticket Form").setTextAlignment(TextAlignment.CENTER).setFontSize(20)).setTextAlignment(TextAlignment.CENTER);
            table1.addCell(new Cell(1, 100).add("To be filled out by Facilities Dept").setTextAlignment(TextAlignment.CENTER).setFontSize(10).setItalic());
            table1.addCell(new Cell(1, 100).setFontSize(10).setWordSpacing(3).add("Date Of Trip: " + tripDate + " Assigned Vehicle: " + getCarName() + " Assigned Driver: " + getDriverName()).setTextAlignment(TextAlignment.CENTER));
            table1.addCell(new Cell(1,100).add("To be filled out by the guard on duty before the vehicle leaves the premises.").setTextAlignment(TextAlignment.CENTER).setItalic().setFontSize(10));
            table1.addCell(new Cell(1,16).add("Time").setBold().setTextAlignment(TextAlignment.CENTER)).addCell(new Cell(1,20).add("Guard on Duty").setBold().setTextAlignment(TextAlignment.CENTER)).addCell(new Cell(1,25).add("Signature").setBold());
            table1.addCell(new Cell(1,8).add("Departure").setBold()).addCell(new Cell(1,20).add("")).addCell(new Cell(1,15).add(""));
            table1.addCell(new Cell(1,8).add(departure)).addCell(new Cell(1,20).add("Printed Name")).addCell(new Cell(1,15).add(""));
            table1.addCell(new Cell(1,24).add("To be filled out by the requesting department.").setItalic().setFontSize(10)).addCell(new Cell(1,26).add("To be filled out by the requesting department.").setItalic().setFontSize(10));
            table1.addCell(new Cell(1,24).add("Destination").setBold()).addCell(new Cell(1,26).add("Purpose of travel").setBold());
            table1.addCell(new Cell(1,24).add("(Please enumerate all places)").setItalic().setFontSize(10)).addCell(new Cell(1,15).add("Arrival").setBold()).addCell(new Cell(1,26).add("(Please indicate purpose of travel)").setItalic().setFontSize(10));
            table1.addCell(new Cell(1,24).add(destination).setHeight(100)).addCell(new Cell(1,15).add("")).addCell(new Cell(1,26).add(purpose));
            table1.addCell(new Cell(1, 100).add("Passenger/s(to be be filled out by the requesting department)").setTextAlignment(TextAlignment.CENTER).setFontSize(10).setItalic());
            table1.addCell(new Cell(1,66).add(passengers).setHeight(70));
            table1.addCell(new Cell(1, 100).add("(To be accomplished by the requesting department)").setTextAlignment(TextAlignment.CENTER).setFontSize(10).setItalic());
            table1.addCell(new Cell(1,32).add("Requested by:").setBold()).addCell(new Cell(1,16).add("Approved by:").setBold()).addCell(new Cell(1,18).add("Received by:").setBold());
            table1.addCell(new Cell(1,32).add(requesterName).setBold().setHeight(30)).addCell(new Cell(1,16).add(getDivisionheadName()).setBold()).addCell(new Cell(1,18).add("").setBold());
            table1.addCell(new Cell(1,32).add("Name and Signature").setBold()).addCell(new Cell(1,16).add("Department/Division Head").setBold()).addCell(new Cell(1,18).add("Facilities Dept").setBold());
            table1.addCell(new Cell(1,66).add(image).setHeight(70));
            document.add(columnOne);
            document.add(columnThree);
            document.add(columnFour);
            document.add(columnTwo);
            document.add(new Paragraph().add("\n\n"));
            secondColumnOne.addCell(new Cell().add("Trip Ticket Form").setFontSize(13));
            secondColumnOne.addCell(new Cell().add("To be filled out by Facilities Dept").setFontSize(10).setItalic());
            secondColumnThree.addCell("Date Of Trip: " + tripDate).addCell("Assigned Vehicle: " +getCarName()).addCell("Assigned Driver: " + getDriverName()).setFontSize((float) 7.5);
            secondColumnThree.addCell(new Cell(1,70).add("To be filled out by the guard on duty before the vehicle leaves the premises.").setFontSize(5).setItalic());
            secondColumnThree.addCell(new Cell().add("Time of Departure").setBold()).addCell(new Cell().add("Guard on Duty").setBold()).addCell(new Cell().add("Signature").setBold());
            secondColumnThree.addCell(departure).addCell(new Cell().setHeight(20).add("")).addCell("");
            secondColumnThree.addCell(new Cell().add("To be filled out by the requesting department.").setItalic().setFontSize(5)).addCell(new Cell().add("To be filled out by the requesting department.").setItalic().setFontSize(5)).addCell(new Cell().add("To be filled out by the requesting department.").setItalic().setFontSize(5));
            secondColumnThree.addCell(new Cell().add("Purpose of Travel").setBold()).addCell(new Cell().add("Destination").setBold()).addCell(new Cell().add("Passenger/s").setBold());
            secondColumnThree.addCell(new Cell().add(purpose)).addCell(new Cell().add(destination)).addCell(new Cell().add(passengers));
            secondColumnThree.addCell(new Cell(1,70).add("(To be accomplished by the requesting department)").setFontSize(5).setItalic());
            secondColumnThree.addCell(new Cell().add("Requested by:").setBold()).addCell(new Cell().add("Approved by:").setBold()).addCell(new Cell().add("Received by:").setBold());
            secondColumnThree.addCell(new Cell().add(requesterName)).addCell(new Cell().add(getDivisionheadName())).addCell(new Cell().add("").setBold());
            secondColumnThree.addCell(new Cell().add("Name and Signature").setBold()).addCell(new Cell().add("Department/Division Head").setBold()).addCell(new Cell().add("Facilities Dept").setBold());
            secondColumnThree.addCell(new Cell(1,70).add(image));
            document.add(secondColumnOne);
            document.add(secondColumnThree);
            document.add(secondColumnFour);
            document.add(secondColumnTwo);
            
            document.close();
            
            
            if (Desktop.isDesktopSupported()) {
                try {
                	setFilePath(fileLocation);
                	setFileName("Trip-TicketReservation"+resId +".pdf");
                    //Desktop.getDesktop().open(file);
                } catch (IOException ex) {
                    System.out.println("No registered application for opening the file.");
                	ex.printStackTrace();
                }
            }
        } catch(Exception e){
        	e.printStackTrace();
        }
    }
	
public void generateStatistics(Connection connection, String fileLocation){
    	
    	try {
    		PdfDocument pdf = new PdfDocument(new PdfWriter(fileLocation));
            Document document = new Document(pdf);
           
            File file = new File(fileLocation);
            String query = "SELECT emp.firstName, emp.lastName, r.destination, de.departmentName, c.manufacturer, c.yearMake, c.model, c.color FROM employee emp INNER JOIN reservations r INNER JOIN driver d INNER JOIN departments de INNER JOIN cars c WHERE emp.employeeID = d.employeeID AND r.licenseID = d.licenseID AND r.departmentID = de.departmentID AND r.carID = c.id AND r.statusID = 3";
            PreparedStatement prstmt = connection.prepareStatement(query);
            ResultSet rs = prstmt.executeQuery();
            Table columnOne = new Table(1).setWidth(400).setTextAlignment(TextAlignment.CENTER).setHorizontalAlignment(HorizontalAlignment.CENTER);
            Table columnTwo = new Table(2).setWidth(400).setTextAlignment(TextAlignment.CENTER).setHorizontalAlignment(HorizontalAlignment.CENTER);;
            Table columnThree = new Table(3).setWidth(400).setTextAlignment(TextAlignment.CENTER).setHorizontalAlignment(HorizontalAlignment.CENTER);
            Table columnFour = new Table(4).setWidth(400).setTextAlignment(TextAlignment.CENTER).setHorizontalAlignment(HorizontalAlignment.CENTER);
            Table secondColumnOne = new Table(1).setWidth(400).setTextAlignment(TextAlignment.CENTER).setHorizontalAlignment(HorizontalAlignment.CENTER);
            Table secondColumnTwo = new Table(2).setWidth(400).setTextAlignment(TextAlignment.CENTER).setHorizontalAlignment(HorizontalAlignment.CENTER);;
            Table secondColumnThree = new Table(3).setWidth(400).setTextAlignment(TextAlignment.CENTER).setHorizontalAlignment(HorizontalAlignment.CENTER);
            Table secondColumnFour = new Table(4).setWidth(400).setTextAlignment(TextAlignment.CENTER).setHorizontalAlignment(HorizontalAlignment.CENTER);
            //Table columnsFive = new Table(5).setWidth(50);
            //Table table1 = new Table(5); // 3 columns.
            //Table table2 = new Table(65);
            
            //Image image = new Image(ImageDataFactory.create("qrcode.png"));
            columnOne.addCell(new Cell().add("Activity Report").setFontSize(13));
            columnFour.addCell(new Cell().add("Destination:").setBold()).addCell(new Cell().add("Driver Name:").setBold()).addCell(new Cell().add("Department:").setBold()).addCell(new Cell().add("Vehicle Used:").setBold());
            while(rs.next()){
            	String destination = rs.getString("destination");
				String driverName = rs.getString("firstName") + " " + rs.getString("lastName");
				String department = rs.getString("departmentName");
				String vehicleUsed = rs.getString("color") + " " + rs.getString("yearMake") + " " + rs.getString("manufacturer") + " " + rs.getString("model");
				columnFour.addCell(destination).addCell(driverName).addCell(department).addCell(vehicleUsed).setFontSize(10);
            }
            //table1.addCell(new Cell(1,66).add("qrcode").setHeight(70));*/
            document.add(columnOne);
            document.add(columnFour);

            

            document.close();
            if (Desktop.isDesktopSupported()) {
                try {
                	setFilePath(fileLocation);
                	setFileName("Statistics.pdf"); // add date for filename
                } catch (IOException ex) {
                    System.out.println("No registered application for opening the file.");
                	ex.printStackTrace();
                }
            }
        } catch(Exception e){
        	e.printStackTrace();
        }
    }
    	
    
	
	public void execute(Connection connection){
		resultSetGetEmpID(connection);
		resultSetGetCarName(connection);
		driverName(connection);
		divisionHeadName(connection);
		requesterName(connection);
		generatePDF();
	}
}