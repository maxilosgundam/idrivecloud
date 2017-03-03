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

import model.Validate;
import model.ForgotGenerator;
import util.EmailSender;
//import net.tanesha.recaptcha.ReCaptchaImpl;
//import net.tanesha.recaptcha.ReCaptchaResponse;
import util.ResponseMessage;

public class EmailCheckerServlet extends HttpServlet implements ResponseMessage{
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
		String email = request.getParameter("email");
		System.out.println(email);
		if(email.isEmpty()){
			System.out.println("pumasok");
			request.setAttribute("invalid", ENTER_EMAIL);
			ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/forgot");
		}else{
			
			Validate val = new Validate();
			val.setEmail(email);
			EmailSender sender = new EmailSender();
				int accountType = val.emailChecker(connection);
				
				if(accountType != 0){

						ForgotGenerator fGen = new ForgotGenerator();
						fGen.generateCleaner(connection);
						fGen.generateForgot(connection, email);
						sender.setSender(getServletContext().getInitParameter("sender"));
						sender.setPassword(getServletContext().getInitParameter("password"));
						sender.setMailHost(getServletContext().getInitParameter("mailHost"));
						sender.setGmail(getServletContext().getInitParameter("gmail"));
						sender.setMailPort(getServletContext().getInitParameter("mailPort"));
						sender.setMailPortNumber(getServletContext().getInitParameter("mailPortNumber"));
						sender.setMailSSL(getServletContext().getInitParameter("mailSSL"));
						sender.setMailAuth(getServletContext().getInitParameter("mailAuth"));
						sender.setCrest(getServletContext().getInitParameter("crest"));
						sender.setCrest2(getServletContext().getInitParameter("crest2Dir"));
						sender.setLogo(getServletContext().getInitParameter("logoDir"));
						sender.setForgotAddress(getServletContext().getInitParameter("forgotAddress"));
						sender.forgotSent(connection, email);
						request.setAttribute("successMsg", EMAIL_SUCCESS);
						ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/forgot");
					
				}else{
					request.setAttribute("invalid", INVALID_EMAIL);
					ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/forgot");
				}
			}
		}
		catch(AccessControlException ace)
		{
			System.out.println(ace.getMessage());
			}
	}
	}

