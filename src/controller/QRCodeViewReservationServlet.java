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
import javax.sql.DataSource;


@WebServlet("/view.html")
public class QRCodeViewReservationServlet extends HttpServlet {
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
		
		String token = request.getParameter("token");
		
	}

}
