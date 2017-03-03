package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.errors.AccessControlException;

import model.AccountClass;
import model.DepartmentClass;
import model.DriverClass;
import model.InputValidationClass;
import model.ReservationSender;
import model.ReservationUpdater;
import model.ReservationViewer;
import model.VehicleClass;
import util.ResponseMessage;
import util.SessionHandlers;

public class ViewReservationServlet extends HttpServlet implements ResponseMessage{
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
		SessionHandlers handler = new SessionHandlers();
		if(request.getParameter("token")!=null){
			System.out.println("Pumasok sa token");
			String token = request.getParameter("token");
			request.setAttribute("reservationDetails", handler.viewReservationQR(connection, token));
			ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/viewreservation");
		}else{
			if(session.getAttribute("accountSession") == null){
				response.sendRedirect("login.jsp");
			} else {
				AccountClass ac = (AccountClass) session.getAttribute("accountSession");
				int accType = ac.getAccType();
				int acc = ac.getAcc();
				
				ReservationUpdater update = new ReservationUpdater();
				String location="";
				switch(accType){
					case 1:
						location = "WEB-INF/employee";
						break;
					case 2:
						location = "WEB-INF/manager";
						break;
					case 3:
						location = "WEB-INF/administrator";
						break;
					default:
						break;
				}
				
				if(accType == 1){
					if(request.getParameter("resID")!=null){
						int resID = Integer.parseInt(request.getParameter("resID"));
						System.out.println("Pumasok sa resID");
						request.setAttribute("reservationDetails", handler.regularViewReservation(connection, resID));
						ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/reservationdetails");
						return;
					}else{
						System.out.println("nag error");
						//request.setAttribute("successMsgPending", PENDING_APPROVE);  error message
						ESAPI.httpUtilities().sendForward(request, response, location);
						return;
					}
				}else{
					if(request.getParameter("approved") != null){
						ReservationSender checker = new ReservationSender();
						int approved = Integer.parseInt(request.getParameter("approved"));
						if(checker.validateAvailableDriverAndVehicleManager(connection,approved)){
							// check coding
							update.approvedReservationManager(connection, approved);
						}else{
							System.out.println("pumasok sa error");
							request.setAttribute("errorMsgPending", NO_DRIVER_VEHICLE_AVAILABLE);
							ESAPI.httpUtilities().sendForward(request, response, location);
							return;
						}
						
					}else if(request.getParameter("denied")!= null){
						
						int denied = Integer.parseInt(request.getParameter("denied"));
						String reason="";
						InputValidationClass validate = new InputValidationClass();
						ReservationSender deny = new ReservationSender();
						if(request.getParameter("denialReason").equals("") || !validate.purposeInputValidation(request.getParameter("denialReason"))){
							request.setAttribute("errorMsgPending", INVALID_REASON);
							ESAPI.httpUtilities().sendForward(request, response, location);
							return;
						}else{
							reason = request.getParameter("denialReason");
							deny.setSender(getServletContext().getInitParameter("sender"));
							deny.setPassword(getServletContext().getInitParameter("password"));
							deny.setMailHost(getServletContext().getInitParameter("mailHost"));
							deny.setGmail(getServletContext().getInitParameter("gmail"));
							deny.setMailPort(getServletContext().getInitParameter("mailPort"));
							deny.setMailPortNumber(getServletContext().getInitParameter("mailPortNumber"));
							deny.setMailSSL(getServletContext().getInitParameter("mailSSL"));
							deny.setMailAuth(getServletContext().getInitParameter("mailAuth"));
							deny.setCrest(getServletContext().getInitParameter("crest"));
							deny.setCrest2(getServletContext().getInitParameter("crest2Dir"));
							deny.setLogo(getServletContext().getInitParameter("logoDir"));
							deny.denyReservation(connection, reason, denied);
							update.deniedReservation(connection, denied, reason);
							request.setAttribute("successMsgPending", SUCCESS_DENY);
							ESAPI.httpUtilities().sendForward(request, response, location);
							return;
						}
					}else{
						ESAPI.httpUtilities().sendForward(location);
					}
		
					
					if(accType == 2){
						
						List<ReservationViewer> reservationDetails2 = handler.managerPendingReservationSession(connection, acc);
						session.removeAttribute("approved");
						session.setAttribute("approved", reservationDetails2);
						
					}else if(accType == 3){
		
						List<DriverClass> driverModel = handler.adminDriverSession(connection);
						
						List<VehicleClass> carsModel = handler.adminCarSession(connection);
						
						List<DepartmentClass> deptModel = handler.adminDeptSession(connection);
		
						session.removeAttribute("drivers");
						session.removeAttribute("departments");
						session.removeAttribute("cars");
		
						session.setAttribute("drivers", driverModel);
						session.setAttribute("departments", deptModel);
						session.setAttribute("cars", carsModel);
						
					}else{
						ESAPI.httpUtilities().sendForward(location);
					}
					request.setAttribute("successMsgPending", PENDING_APPROVE);
					ESAPI.httpUtilities().sendForward(request, response, location);
					return;
				}
			}
		}
	}
		catch(AccessControlException ace)
		{
			System.out.println(ace.getMessage());
			}
	}

}
