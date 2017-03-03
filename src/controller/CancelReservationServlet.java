package controller;

import java.io.IOException;
import java.sql.Connection;
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

import model.CancelClass;
import model.InputValidationClass;

@WebServlet("/cancel.html")
public class CancelReservationServlet extends HttpServlet {
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
			System.out.println("no session found.");
		} else {
			String reservationID = request.getParameter("resID");
			InputValidationClass validate = new InputValidationClass();
			CancelClass cancel = new CancelClass();
			cancel.setResID(Integer.parseInt(reservationID));
			if(validate.checkReservations(connection, reservationID)){
				if(cancel.cancelReservation(connection)){
					//Reservation successfully cancelled
					System.out.println("Reservation has been successfully cancelled");
				} else {
					//Reservation not cancelled. check logs for what returned false.
					System.out.println("Reservation wasn't cancelled omg");
				}
			} else {
				//no reservation found with the ID selected.
				System.out.println("No reservation ID?!?!");
			}
		}
		
		
		
	}

}
