package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.User;
import org.owasp.esapi.errors.AccessControlException;
import org.owasp.esapi.errors.AuthenticationException;
import org.owasp.esapi.errors.AuthenticationHostException;
import org.owasp.esapi.errors.EncryptionException;

import model.AccountClass;
import model.DepartmentClass;
import model.DriverClass;
import model.ReservationViewer;
import model.ResultSetMethods;
import model.VehicleClass;
import model.AccountClass;
import util.ResponseMessage;
import util.SessionHandlers;

public class AccountSeparatorServlet extends HttpServlet implements org.owasp.esapi.User, org.owasp.esapi.Authenticator, ResponseMessage{
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
		int accType = (int) request.getAttribute("type");
		int acc = (int) request.getAttribute("account");
		String pass = (String) request.getAttribute("pass");
		String fName = (String) request.getAttribute("fName");
		String lName = (String) request.getAttribute("lName");
		String email = (String) request.getAttribute("email");
		int deptName = (int) request.getAttribute("deptName");
		
		String location = "";
		if(acc != 0 && accType != 0 && pass != null){
			if(accType == 1){
				location = "employee";
			}else if(accType == 2){
				location = "manager";
			}else if(accType == 3){
				location = "administrator";
			}else{
				request.setAttribute("invalid", ACCOUNT_DOES_NOT_EXIST);
				ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/login");
			}
			HttpSession session = request.getSession();
			
			AccountClass ac = new AccountClass();
			ac.setAcc(acc);
			ac.setAccType(accType);
			ac.setDeptName(deptName);
			ac.setEmail(email);
			ac.setfName(fName);
			ac.setlName(lName);
			ac.setPass(pass);
			session.setAttribute("accountSession", ac);
			session.setMaxInactiveInterval(-1);
			SessionHandlers handler = new SessionHandlers();
			switch(accType){
			case 1:
					List<ReservationViewer> reservationDetailsEmp = handler.empReservationSession(connection, acc);
					session.setAttribute("approved", reservationDetailsEmp);

				break;
			case 2:
					List<ReservationViewer> reservationDetails2 = handler.managerPendingReservationSession(connection, acc);
					session.setAttribute("approved", reservationDetails2);

				break;
			case 3:
				List<DriverClass> driverModel = handler.adminDriverSession(connection);
				
				List<VehicleClass> carsModel = handler.adminCarSession(connection);
				
				List<DepartmentClass> deptModel = handler.adminDeptSession(connection);

				session.setAttribute("drivers", driverModel);
				session.setAttribute("departments", deptModel);
				session.setAttribute("cars", carsModel);
				break;
			default:
				break;
			}
			response.sendRedirect(location);

		}else{
			request.setAttribute("invalid", ACCOUNT_DOES_NOT_EXIST);
			ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/login");
		}
		}
		catch(AccessControlException ace)
		{
			System.out.println(ace.getMessage());
			}

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void changePassword(User arg0, String arg1, String arg2, String arg3) throws AuthenticationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearCurrent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public User createUser(String arg0, String arg1, String arg2) throws AuthenticationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean exists(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String generateStrongPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String generateStrongPassword(User arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getCurrentUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getUser(long arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getUser(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set getUserNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String hashPassword(String arg0, String arg1) throws EncryptionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User login() throws AuthenticationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User login(HttpServletRequest arg0, HttpServletResponse arg1) throws AuthenticationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeUser(String arg0) throws AuthenticationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCurrentUser(User arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void verifyAccountNameStrength(String arg0) throws AuthenticationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean verifyPassword(User arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void verifyPasswordStrength(String arg0, String arg1, User arg2) throws AuthenticationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addRole(String arg0) throws AuthenticationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addRoles(Set<String> arg0) throws AuthenticationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addSession(HttpSession arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void changePassword(String arg0, String arg1, String arg2)
			throws AuthenticationException, EncryptionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long getAccountId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getAccountName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCSRFToken() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap getEventMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getExpirationTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getFailedLoginCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Date getLastFailedLoginTime() throws AuthenticationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLastHostAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getLastLoginTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getLastPasswordChangeTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Locale getLocale() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getRoles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getScreenName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set getSessions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void incrementFailedLoginCount() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isAnonymous() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isInRole(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLoggedIn() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSessionAbsoluteTimeout() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSessionTimeout() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void lock() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loginWithPassword(String arg0) throws AuthenticationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void logout() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeRole(String arg0) throws AuthenticationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeSession(HttpSession arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String resetCSRFToken() throws AuthenticationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAccountName(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setExpirationTime(Date arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLastFailedLoginTime(Date arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLastHostAddress(String arg0) throws AuthenticationHostException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLastLoginTime(Date arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLastPasswordChangeTime(Date arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLocale(Locale arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRoles(Set<String> arg0) throws AuthenticationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setScreenName(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unlock() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean verifyPassword(String arg0) throws EncryptionException {
		// TODO Auto-generated method stub
		return false;
	}

}
