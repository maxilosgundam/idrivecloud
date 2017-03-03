package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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

import model.AccountClass;
import model.ChangePasswordClass;
import model.InputValidationClass;
import util.ResponseMessage;

public class RegularChangePasswordServlet extends HttpServlet implements ResponseMessage{
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
			System.out.println("Pumasok sa servlet");
			String oldPassword = request.getParameter("oldPass");
			String newPassword = request.getParameter("newPass");
			String newPassword2 = request.getParameter("newPass2");
			
			AccountClass ac = new AccountClass();
			int employeeID = ac.getAcc();
			String email = ac.getEmail();
			
			if(oldPassword != null && newPassword != null && newPassword2 != null){
				
				InputValidationClass validate = new InputValidationClass();
				ChangePasswordClass change = new ChangePasswordClass();
				
				if(validate.changePasswordInputValidation(connection, oldPassword, newPassword, employeeID) == true){
					if(validate.passwordInputValidation(newPassword, newPassword2) == true){
						System.out.println("all good");
						change.setEmail(email);
						change.changePassword(connection, newPassword);
						response.sendRedirect("logoutservlet.html");
					} else {
						System.out.println("error 3");
						request.setAttribute("invalid", PASS_INVALID);
						ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/change");
					}
				} else {
					System.out.println("error 2");
					request.setAttribute("invalid", PASS_INVALID);
					ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/change");
				}
				
			} else {
				System.out.println("error 1");
				request.setAttribute("invalid", PASS_INVALID);
				ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/change");
			}
		}
	}
		catch(AccessControlException ace)
		{
			System.out.println(ace.getMessage());
			}
	}

}
