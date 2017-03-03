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

import model.DepartmentClass;

@WebServlet("/deldept.html")
public class DeleteDepartmentServlet extends HttpServlet {
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
		
		try{
			DepartmentClass dept = new DepartmentClass();
			int delID = Integer.parseInt(request.getParameter("deptID"));
			int transferID = Integer.parseInt(request.getParameter("transfer"));
			
			if(dept.checkDepartment(connection, transferID)){
				if(dept.transferEmployee(connection, transferID, delID)){
					if(dept.deleteDepartment(connection, delID)){
						//department deleted, all employees from it are transferred.
					} else {
						//failed to delete department error
					}
				} else {
					//failed to transfer employees error
				}
			} else {
				//department chosen has employees error
			}
			
			
		} catch (NumberFormatException nfe){
			nfe.printStackTrace();
		}
		
	}

}
