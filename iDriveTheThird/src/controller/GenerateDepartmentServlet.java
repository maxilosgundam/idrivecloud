package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import model.InputValidationClass;
import model.ReservationSender;
import model.ReservationViewer;
import model.VehicleClass;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.Part;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.errors.AccessControlException;

import java.io.InputStream;

import model.DepartmentClass;
import model.DepartmentGenerator;
import model.DriverClass;
import util.EmailSender;
import util.ResponseMessage;
import util.SessionHandlers;

@MultipartConfig
public class GenerateDepartmentServlet extends HttpServlet implements ResponseMessage {
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
		doPost(request,response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try
		{
		HttpSession session = request.getSession(false);
		
		if(session.getAttribute("accountSession") == null){
			response.sendRedirect("login.jsp");
		} else {
			String newDept = request.getParameter("department");
			String employeeId = request.getParameter("employeeId");
			Part filePart = request.getPart("photo");
			InputStream inputStream = null;
			
			if (filePart != null) {
	            // prints out some information for debugging
	            System.out.println(filePart.getName());
	            System.out.println(filePart.getSize());
	            System.out.println(filePart.getContentType());
	             
	            // obtains input stream of the upload file
	            inputStream = filePart.getInputStream();
	        }
			
			InputValidationClass validate = new InputValidationClass();
			
			if(validate.departmentInputValidation(newDept) == true)
			{
				if(validate.imageInputValidation(filePart.getSize(),filePart.getContentType()) == true)
				{	
				if(validate.employeeIdInputValidation(connection, employeeId) == true)
				{
					DepartmentGenerator sender = new DepartmentGenerator();
					sender.setDepartment(newDept);
					sender.setEmployeeId(employeeId);
					sender.setPhoto(inputStream);
					if(sender.duplicateDepartment(connection)){
						request.setAttribute("errorMsgDept", DEPT_EXISTS);
						ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/administrator");
					}else{
						if(sender.existingEmployee(connection)){
							sender.sendDepartment(connection);
							sender.executeProcess(connection);
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
							request.setAttribute("successMsgDept", DEPT_SUCCESS );
							ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/administrator");
						}else{
							request.setAttribute("errorMsgDept", EMP_DOES_NOT_EXIST);
							ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/administrator");
						}
					}
				}
				else
				{
					request.setAttribute("errorMsgDept", EMP_ID_INVALID);
					ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/administrator");
					}
				}
				else
				{
					System.err.println(validate.imageInputValidation(filePart.getSize(),filePart.getContentType()));
					request.setAttribute("errorMsgDept", IMAGE_ERROR);
					ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/administrator");
					}
				}
			else
			{
				System.out.println(validate.departmentInputValidation(newDept));
				request.setAttribute("errorMsgDept", DEPT_INVALID);
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
