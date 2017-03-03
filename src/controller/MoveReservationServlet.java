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

import model.InputValidationClass;
import model.MoveReservationClass;

@WebServlet("/movereservation.html")
public class MoveReservationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	Connection connection = null;
	
	public void init() throws ServletException {
		try {
			connection = ((DataSource)InitialContext.doLookup("java:/comp/env/jdbc/iDriveDB")).getConnection();
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
		} else {
			InputValidationClass validate = new InputValidationClass();
			MoveReservationClass move = new MoveReservationClass();
			String date = request.getParameter("tripDate");
			String hours = request.getParameter("hours");
			String minutes = request.getParameter("minutes");
			String time = request.getParameter("time");
			String resID = request.getParameter("resID");
			
			if(validate.dateInputValidation(date)){
				if(validate.timeInputValidation(hours, minutes, time)){
					move.setDate(date);
					move.setHours(hours);
					move.setMinutes(minutes);
					move.setTime(time);
					move.setResID(resID);
					if(move.checkAvailable(connection)){
						if(move.moveReservation(connection)){
							System.out.println("Na-move!");
						} else {
							System.out.println("Hindi na-move!");
						}
					} else {
						System.out.println("No available!");
					}
				} else {
					System.out.println("URL bypassed!");
				}
			} else {
				System.out.println("Should be 3-days before!");
			}
		}
	}

}
