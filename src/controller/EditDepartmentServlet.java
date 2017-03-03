package controller;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
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
import org.owasp.esapi.errors.AccessControlException;

import model.DepartmentClass;
import model.DriverClass;
import model.EditClass;
import model.InputValidationClass;
import model.ReservationViewer;
import model.VehicleClass;
import util.ResponseMessage;
import util.SessionHandlers;

@MultipartConfig
public class EditDepartmentServlet extends HttpServlet implements ResponseMessage {
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
			String key = request.getParameter("key");
			System.out.println(key);
			String deptName = request.getParameter("editDepartment");
			String divisionHead = request.getParameter("editEmployeeId");
			Part photo = request.getPart("photo");
			
			InputStream inputStream = null;
			
			if(photo != null){
				System.out.println(photo.getName());
				System.out.println(photo.getSize());
				System.out.println(photo.getContentType());
				
				inputStream = photo.getInputStream();
			}
			
			InputValidationClass validate = new InputValidationClass();
			EditClass edit = new EditClass();
			
			
			if(validate.checkDepartments(connection, key)){
				if(validate.requiredImageInputValidation(photo.getSize(), photo.getContentType())){
					if(validate.employeeIdInputValidation(connection, divisionHead)){
						if(validate.departmentInputValidation(deptName)){
							
							System.out.println("success on departments");
							edit.setDepartmentKey(Integer.parseInt(key));
							edit.setEmployeeID(Integer.parseInt(divisionHead));
							edit.setDeptName(deptName);
							edit.setImage(inputStream);
							edit.updateDepartments(connection);
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
							request.setAttribute("successMsgDept", DEPARTMENT_EDIT_SUCCESS);
							ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/administrator");
							
						} else {
							request.setAttribute("errorMsgDept", INVALID_DEPT_NAME);
							ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/administrator");
						}
					} else {
						request.setAttribute("errorMsgDept", INVALID_DIVISION_HEAD);
						ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/administrator");
					}		
				} else {
					request.setAttribute("errorMsgDept", IMAGE_ERROR);
					ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/administrator");
				}
			} else {
				request.setAttribute("errorMsgDept", DEPT_NOT_FOUND);
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
