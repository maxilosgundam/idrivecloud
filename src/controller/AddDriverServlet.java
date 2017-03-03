package controller;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import javax.sql.DataSource;

import org.owasp.esapi.ESAPI;

import model.DepartmentClass;
import model.DriverClass;
import model.DriverSender;
import model.InputValidationClass;
import model.ReservationViewer;
import model.VehicleClass;
import util.ResponseMessage;
import util.SessionHandlers;

@MultipartConfig
public class AddDriverServlet extends HttpServlet implements ResponseMessage{
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
			System.out.println("Pumasok sa if");
			response.sendRedirect("login.jsp");
			//add error message. (possible solution to double form submission is PRG pattern. google it.)
		} else {
			System.out.println("Pumasok sa else");
			try{
				String employeeID = request.getParameter("employeeID");
				String licenseID = request.getParameter("licenseID");
				String licenseRestriction1 = request.getParameter("licenseRestriction1");
				String licenseRestriction2 = request.getParameter("licenseRestriction2");
				String licenseRestriction3 = request.getParameter("licenseRestriction3");
				String licenseRestriction4 = request.getParameter("licenseRestriction4");
				String licenseRestriction5 = request.getParameter("licenseRestriction5");
				String licenseRestriction6 = request.getParameter("licenseRestriction6");
				String licenseRestriction7 = request.getParameter("licenseRestriction7");
				String licenseRestriction8 = request.getParameter("licenseRestriction8");
				String restrictionID[] = {licenseRestriction1, licenseRestriction2, licenseRestriction3, licenseRestriction4,
										  licenseRestriction5, licenseRestriction6, licenseRestriction7, licenseRestriction8,};
				Part driverImage = request.getPart("driverImage");

				InputStream inputStream = null;

				if(driverImage != null){
					System.out.println(driverImage.getName());
					System.out.println(driverImage.getSize());
					System.out.println(driverImage.getContentType());

					inputStream = driverImage.getInputStream();
				} 

				InputValidationClass validate = new InputValidationClass();

				if(validate.employeeIdInputValidation(connection, employeeID) == true){
					if(validate.licenseInputValidation(licenseID) == true){
						if(validate.restrictionInputValidation(restrictionID) == true){
							if(validate.driverImageInputValidation(driverImage.getSize(), driverImage.getContentType()) == true){
								System.out.println("pumasok");
								DriverSender ds = new DriverSender();
								ds.setEmployeeID(employeeID);
								ds.setLicenseID(licenseID);
								ds.setRestrictionID(restrictionID);
								ds.setDriverImage(inputStream);
								if(ds.duplicateDriver(connection)){
									request.setAttribute("errorMsgDriver", DRIVER_EXISTS);
									ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/administrator");
								}else if(ds.duplicateLicense(connection)){
									request.setAttribute("errorMsgDriver", LICENSE_ASSIGNED);
									ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/administrator");
								}else if(ds.employeeChecker(connection)==false){
									request.setAttribute("errorMsgDriver", DOES_NOT_EXIST);
									ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/administrator");
								}else{
									ds.execute(connection);
									System.out.println("all good");
									SessionHandlers handler = new SessionHandlers();
									
									//List<ReservationViewer> reservationDetailsAdmin = handler.adminReservationsSession(connection);

									List<DriverClass> driverModel = handler.adminDriverSession(connection);
									
									List<VehicleClass> carsModel = handler.adminCarSession(connection);
									
									List<DepartmentClass> deptModel = handler.adminDeptSession(connection);
									
									//session.removeAttribute("pending");
									session.removeAttribute("drivers");
									session.removeAttribute("departments");
									session.removeAttribute("cars");
									//session.setAttribute("pending", reservationDetailsAdmin);
									session.setAttribute("drivers", driverModel);
									session.setAttribute("departments", deptModel);
									session.setAttribute("cars", carsModel);
									request.setAttribute("successMsgDriver", DRIVER_SUCCESS);
									ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/administrator");
								}
							} else {
								System.out.println("error 4");
								request.setAttribute("errorMsgDriver", IMAGE_ERROR);
								ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/administrator");
							}
						} else {
							System.out.println("error 3");
							request.setAttribute("errorMsgDriver", RESTRICTION_ERROR);
							ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/administrator");
						}
					} else {
						System.out.println("error 2");
						request.setAttribute("errorMsgDriver", INVALID_LICENSE);
						ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/administrator");
					}
				} else {
					System.out.println("error 1");
					request.setAttribute("errorMsgDriver", DOES_NOT_EXIST);
					ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/administrator");
				}

			} catch (Exception e){
				e.printStackTrace();
				response.sendRedirect("administrator");
			}

		}
	}

}