package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
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

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.errors.AccessControlException;

import model.DeleteClass;
import model.DepartmentClass;
import model.DriverClass;
import model.InputValidationClass;
import model.ReservationViewer;
import model.VehicleClass;
import util.ResponseMessage;
import util.SessionHandlers;

public class DeleteServlet extends HttpServlet implements ResponseMessage {
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
		try
		{
		HttpSession session = request.getSession(false);
		
		if(session.getAttribute("accountSession") == null){
			response.sendRedirect("login.jsp");
		} else {
			System.out.println("pumasok");
			String key = request.getParameter("key");
			String option = request.getParameter("option");
			
			InputValidationClass validate = new InputValidationClass();
			DeleteClass delete = new DeleteClass();
			
			if(option.equalsIgnoreCase("department")){
				if(validate.checkDepartments(connection, key)){
					
					System.out.println("success on department");
					delete.setDepartmentKey(Integer.parseInt(key));
					delete.deleteDepartment(connection);
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
					request.setAttribute("successMsgDept", DEPARTMENT_DELETE_SUCCESS);
					ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/administrator");
					
				} else {
					System.out.println("error on department");
					request.setAttribute("errorMsgDept", DEPT_NOT_FOUND);
					ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/administrator");
				}
			} else if(option.equalsIgnoreCase("driver")){
				if(validate.checkDrivers(connection, key)){
					
					System.out.println("success on driver");
					delete.setDriverKey(key);
					delete.deleteDriver(connection);
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
					request.setAttribute("successMsgDriver", DRIVER_DELETE_SUCCESS);
					ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/administrator");
				} else {
					System.out.println("error on driver");
					request.setAttribute("errorMsgDriver", DRIVER_NOT_FOUND);
					ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/administrator");
				}
			} else if(option.equalsIgnoreCase("cars")){
				if(validate.checkVehicles(connection, key)){
					
					System.out.println("success on cars");
					delete.setVehicleKey(key);
					delete.deleteVehicle(connection);
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
					request.setAttribute("successMsgCar", VEHICLE_DELETE_SUCCESS);
					ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/administrator");
					
				} else {
					System.out.println("error on cars");
					request.setAttribute("errorMsgCar", VEHICLE_NOT_FOUND);
					ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/administrator");
				}
			} else {
				System.out.println("wtf is wrong");
				request.setAttribute("errorMsg", INVALID_OPTION);
				ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/administrator");
				}
			
			}
		}
		catch(AccessControlException ace)
		{
			System.out.println(ace.getMessage());
		}
}

}
