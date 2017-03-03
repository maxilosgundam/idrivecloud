package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import util.BackupAndRestoreClass;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RestoreDBServlet
 */
public class RestoreDBServlet extends HttpServlet {
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
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		BackupAndRestoreClass restore = new BackupAndRestoreClass();
		restore.setMySQLRestorePath(getServletContext().getInitParameter("mySQLRestorePath"));
		restore.setDbUser(getServletContext().getInitParameter("dbUser"));
		restore.setSource(getServletContext().getInitParameter("source"));
		restore.setDatabase(getServletContext().getInitParameter("database"));
		restore.setDbPassword(getServletContext().getInitParameter("dbPass"));
		
		if(username.equals(getServletContext().getInitParameter("adminUser")) && password.equals(getServletContext().getInitParameter("adminPass"))){
			
			System.out.println("authenticate complete");
			String dropDB = "DROP DATABASE idrive";
			String createDB = "CREATE DATABASE idrive";
			try {
				
				PreparedStatement pstmt = connection.prepareStatement(dropDB);
				pstmt.executeUpdate();
				PreparedStatement pstmt2 = connection.prepareStatement(createDB);
				pstmt2.executeUpdate();
				
				boolean status=BackupAndRestoreClass.mysqlDatabaseRestore();
				
		        if(status==true){
		        	
		            System.out.println("restore success");
		            
		        } else {
		        	
		             System.out.println("restore failure ");
		              
		        }
				
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
			
		} else {
			System.out.println("authenticate error");
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/JSP/authenticate.jsp");
	        dispatcher.forward(request, response);
		}
		
		
		
		
	}

}
