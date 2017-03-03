package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.errors.AccessControlException;

import model.InputValidationClass;
import util.ResponseMessage;
import model.ChangePasswordClass;

public class ChangePasswordServlet extends HttpServlet implements ResponseMessage{
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
		InputValidationClass ivc = new InputValidationClass();
		String password = request.getParameter("password");
		String password2 = request.getParameter("password2");
		String token = request.getParameter("token");
		System.out.println("ffs" + token);
		System.out.println("ffs" + password);
		System.out.println("ffs" + password2);
		if(ivc.passwordInputValidation(password, password2) == true)
		{
			if(ivc.tokenInputValidation(connection, token) == true)
			{
				ChangePasswordClass cpc = new ChangePasswordClass();
				cpc.setiSalt(getServletContext().getInitParameter("iSalt"));
				cpc.changePassword(connection, password, token);
				request.setAttribute("change", SUCCESS_PASS_CHANGE);
				ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/login");
				}
			else
			{
				request.setAttribute("invalid", EXPIRED_REQUEST );
				request.setAttribute("token", token);
				ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/login");
				}
			}
		else
		{
			request.setAttribute("error", PASS_NOT_MATCH);
			request.setAttribute("token", token);
			ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/forgot");
			
			//request.getRequestDispatcher("forgot" + "?token=" + token).forward(request,response);
			}
		}
		catch(AccessControlException ace)
		{
			System.out.println(ace.getMessage());
			}
		}
	}

