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
import model.InputValidationClass;
import model.ReservationViewer;
import model.VehicleClass;
import model.VehicleSender;
import util.ResponseMessage;
import util.SessionHandlers;

@MultipartConfig
public class AddVehicleServlet extends HttpServlet implements ResponseMessage{
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
		} else {
			System.out.println("Pumasok sa else");
			try{
				
				String manufacturer = request.getParameter("carManufacturer");
				String yearManufactured = request.getParameter("yearMake");
				String carModel = request.getParameter("carModel");
				String carColor = request.getParameter("carColor");
				String carPlate = request.getParameter("carPlate");
				String carCapacity = request.getParameter("carCapacity");
				Part carImage = request.getPart("carImage");

				InputStream inputStream = null;

				if(carImage != null){
					System.out.println(carImage.getName());
					System.out.println(carImage.getSize());
					System.out.println(carImage.getContentType());

					inputStream = carImage.getInputStream();
				} 
				InputValidationClass validate = new InputValidationClass();

				if(validate.vehicleStringInputValidation(manufacturer) == true){
					if(validate.vehicleYearManufacturedInputValidation(yearManufactured) == true){
						if(validate.vehicleStringInputValidation(carModel) == true){
							if(validate.vehicleStringInputValidation(carColor) == true){
								if(validate.vehiclePlateInputValidation(carPlate) == true){
									if(validate.vehicleMaxCapacityInputValidation(carCapacity) == true){
										if(validate.vehicleImageInputValidation(carImage.getSize(), carImage.getContentType()) == true){
											VehicleSender vs = new VehicleSender();
											vs.setManufacturer(manufacturer);
											vs.setYearManufactured(yearManufactured);
											vs.setCarModel(carModel);
											vs.setCarColor(carColor);
											vs.setCarPlate(carPlate);
											vs.setCarCapacity(carCapacity);
											vs.setPhoto(inputStream);
											if(vs.duplicateVehicle(connection)){
												request.setAttribute("errorMsgCar", VEHICLE_EXISTS);
												ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/administrator");
											}else{

												vs.executeProcess(connection);
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
												request.setAttribute("successMsgCar", VEHICLE_SUCCESS);
												ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/administrator");
		//										response.sendRedirect("administrator.jsp");
											}
										} else {
											request.setAttribute("errorMsgCar", IMAGE_ERROR);
											ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/administrator");
										}
									} else {
										request.setAttribute("errorMsgCar", INVALID_CAP);
										ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/administrator");
									}
								} else {
									request.setAttribute("errorMsgCar", INVALID_PLATE);
									ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/administrator");
								}
							} else {
								request.setAttribute("errorMsgCar", INVALID_COLOR);
								ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/administrator");
							}
						} else {
							request.setAttribute("errorMsgCar", INVALID_MODEL);
							ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/administrator");
						}
					} else {
						request.setAttribute("errorMsgCar", INVALID_YEAR);
						ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/administrator");
					}
				} else {
					request.setAttribute("errorMsgCar", INVALID_MANUFACTURER);
					ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/administrator");
				}	
			} catch (Exception e){
				e.printStackTrace();
				response.sendRedirect("administrator.jsp");
			}
			
		}
	
	}

}