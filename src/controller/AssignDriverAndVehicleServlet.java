package controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import model.DepartmentClass;
import model.DriverAndVehicleUpdater;
import model.DriverClass;
import model.ForgotGenerator;
import model.ReservationViewer;
import model.ResultSetMethods;
import model.VehicleClass;
import util.SessionHandlers;
import util.PDFWriter;
import util.QRCodeGenerator;

public class AssignDriverAndVehicleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	Connection connection = null;
	public void init() throws ServletException {
		try {
			connection = ((DataSource)InitialContext.doLookup("java:/comp/env/jdbc/iDriveDB"))
					.getConnection();
			getServletContext().setAttribute("dbConnection", connection);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			System.err.println("NamingException Exception - " + e.getMessage());
			e.printStackTrace();
		} catch (SQLException sqle) {
			System.err.println("SQLE Exception - " + sqle.getMessage());
			sqle.printStackTrace();
		}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		
		if(session.getAttribute("accountSession") == null){
			response.sendRedirect("login.jsp");
			System.out.println("no session found");
		} else {
			String licenseID = request.getParameter("driver");
			int carID = Integer.parseInt(request.getParameter("cars"));

			List<ReservationViewer> viewEach = (List<ReservationViewer>) session.getAttribute("viewEach");
			int resID = viewEach.get(0).getReservationID();
			
			
			if(licenseID == null || carID == 0){
				response.sendRedirect("administrator");
			}else{
				String location = getServletContext().getInitParameter("pdfDir");
				
				DriverAndVehicleUpdater update = new DriverAndVehicleUpdater();
				ForgotGenerator fg = new ForgotGenerator();
				String token = fg.generateToken();
				update.updateReservation(connection, resID, carID, licenseID, token);
				
				QRCodeGenerator qrGen = new QRCodeGenerator();
				qrGen.setToken(getServletContext().getInitParameter("qrCodeAdd")+token);
				qrGen.execute();
				
				SessionHandlers handler = new SessionHandlers();
				
				List<ReservationViewer> reservationDetailsAdmin = handler.adminReservationsSession(connection);

				List<DriverClass> driverModel = handler.adminDriverSession(connection);
				List<VehicleClass> carsModel = handler.adminCarSession(connection);
				List<DepartmentClass> deptModel = handler.adminDeptSession(connection);
				
				session.removeAttribute("pending");
				session.removeAttribute("drivers");
				session.removeAttribute("departments");
				session.removeAttribute("cars");
				session.setAttribute("pending", reservationDetailsAdmin);
				session.setAttribute("drivers", driverModel);
				session.setAttribute("departments", deptModel);
				session.setAttribute("cars", carsModel);
				
				ResultSetMethods rs = new ResultSetMethods();
				PDFWriter pdf = new PDFWriter();
				pdf.setResId(resID);
//				pdf.setEmpId(driverEmpID);
				pdf.setDriverTable(rs.driverSessionGetEmpID(connection,licenseID));
				pdf.setCarTable(rs.carsSessionGetDetails(connection, carID));
				pdf.setLocation(location);
				pdf.setReservationDetails(viewEach);
//				pdf.setCarsModel(carsModel);
				pdf.execute(connection);
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "inline; filename="+pdf.getFileName());
				OutputStream out = response.getOutputStream(); 
				try (FileInputStream in = new FileInputStream(pdf.getFilePath())) {
				    int content;
				    while ((content = in.read()) != -1) {
				        out.write(content);
				    }
				} catch (IOException e) {
				    e.printStackTrace();
				}
				out.close();
				System.out.println("File path is " + pdf.getFilePath());
				session.removeAttribute("availCars");
				session.removeAttribute("availDrivers");
				session.removeAttribute("viewEach");
				//response.sendRedirect("administrator");
				
				
			}
		}
	}

}
