package controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
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

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.errors.AccessControlException;

import util.PDFWriter;

public class RecordsStatisticsServlet extends HttpServlet {
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
		} else {
			try {
				PDFWriter pdf = new PDFWriter();
				String filePath = getServletContext().getInitParameter("weeklyPdfDir");
				pdf.generateStatistics(connection, filePath);
				
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "inline; filename="+pdf.getFileName());
				OutputStream out = response.getOutputStream();
				
				try (FileInputStream in = new FileInputStream(pdf.getFilePath())) {
				    int content;
				    while ((content = in.read()) != -1) {
				        out.write(content);
				    }
				} catch (IOException e) {
				    e.printStackTrace();
				}
				
				out.close();
				
				ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/administrator");
			} catch (AccessControlException e) {
				e.printStackTrace();
			}
		}
	}

}
