package controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.owasp.esapi.User;
import org.owasp.esapi.crypto.CipherText;
import org.owasp.esapi.crypto.PlainText;
import org.owasp.esapi.errors.AccessControlException;
import org.owasp.esapi.errors.AuthenticationException;
import org.owasp.esapi.errors.AuthenticationHostException;
import org.owasp.esapi.errors.EncryptionException;
import org.owasp.esapi.errors.IntegrityException;
import org.owasp.esapi.reference.FileBasedAuthenticator;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Encryptor;
import org.owasp.esapi.waf.ESAPIWebApplicationFirewallFilter;
import model.Validate;
import util.ResponseMessage;
import util.VerifyRecaptcha;
public class AccountCheckerServlet extends HttpServlet implements org.owasp.esapi.User, org.owasp.esapi.Encryptor ,org.owasp.esapi.Authenticator, ResponseMessage{
	private static final long serialVersionUID = 1L;
	//private static byte[] key = {
	//		'h', 'a', 'r', 'a', 'm', 'b', 'e', 'l', 'i', 'v', 'e', 's', '4', 'e', 'v', 'r'
	//	};
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
		
//		String remoteAddr = request.getRemoteAddr();
//	    ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
//	    reCaptcha.setPrivateKey("6LfQNwwUAAAAAERiI2YtwOyJeWt_iyaTJzAiEb9F");
//	
//	    String challenge = request.getParameter("recaptcha_challenge_field");
//	    String uresponse = request.getParameter("recaptcha_response_field");
//	    ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, challenge, uresponse);
		try
		{
		String gRecaptchaResponse = request
				.getParameter("g-recaptcha-response");
		boolean verify = VerifyRecaptcha.verify(gRecaptchaResponse);

		String user = request.getParameter("username");
		String password =  request.getParameter("password");
		
		
		//SecretKey sk = new SecretKeySpec(key, "AES");
		
		System.out.println(hashPassword(password, getServletContext().getInitParameter("iSalt")));
		System.out.println("does it go here");
		if(user.isEmpty() || password.isEmpty()){
			request.setAttribute("invalid", ENTER_USER_PASS);
			ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/login");
			System.out.println("probably here!");
		}else{
		
			Validate val = new Validate();
			val.setUsername(user);
		//	val.setPassword(password);
			val.setPassword(hashPassword(password, getServletContext().getInitParameter("iSalt")));
			System.out.println(val.getPassword());
		//	val.setPassword(encrypt(sk, new PlainText(hashPassword(password, user + getServletContext().getInitParameter("iSalt")))).getBase64EncodedRawCipherText());
			System.out.println("check the label!");
			boolean isValid = val.userValidate();
			
			if(isValid){

				int username =  Integer.parseInt(user);
				
				val.setUsername(user);
				val.setPassword(hashPassword(password, getServletContext().getInitParameter("iSalt")));
			//	val.setPassword(password);
			/*	val.setPassword(encrypt(sk, new PlainText(hashPassword(password, user + getServletContext().getInitParameter("iSalt")))).getBase64EncodedRawCipherText());
				System.out.println("check the label" + val.getPassword());
				System.out.println("check the label" + val.getPassword());
				System.out.println("check the label" + val.getPassword());
				System.out.println("check the label" + val.getPassword());
				System.out.println("check the label" + val.getPassword());
				System.out.println("check the label" + val.getPassword());
				System.out.println("check the label" + val.getPassword());
				System.out.println("check the label" + val.getPassword());
				System.out.println("check the label" + val.getPassword());
				System.out.println("check the label" + val.getPassword());
				System.out.println("check the label" + val.getPassword());
			*/
				int accountType = val.accountChecker(connection);
				
				if(accountType != 0){
					if(verify){
						request.setAttribute("account", username);
						request.setAttribute("type", accountType);
						request.setAttribute("pass", val.getPassword()); //val.getPassword());
						request.setAttribute("fName", val.getfName());
						request.setAttribute("lName", val.getlName());
						request.setAttribute("deptName", val.getDeptName());
						request.setAttribute("email", val.getEmail());
						
						ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/accountseparator.html");
					}else{
						request.setAttribute("invalid", INVALID_CAPTCHA);
						ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/login");
					}
				}else{
					request.setAttribute("invalid", WRONG_USER_PASS );
					ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/login");
				}
			}else{
				request.setAttribute("invalid", WRONG_USER_PASS);
				ESAPI.httpUtilities().sendForward(request, response, "WEB-INF/login");
			}
		}
		}
		catch(EncryptionException ee)
		{
			System.out.println(ee.getMessage());
		}
		catch(AccessControlException ae)
		{
			System.out.println(ae.getMessage());
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
		return FileBasedAuthenticator.getInstance().generateStrongPassword();
	}
	@Override
	public String generateStrongPassword(User user, String password) {
		return FileBasedAuthenticator.getInstance().generateStrongPassword(user, password);
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
	public String hashPassword(String password, String accountName) throws EncryptionException {
		return FileBasedAuthenticator.getInstance().hashPassword(password, accountName);
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
	@Override
	public PlainText decrypt(CipherText arg0) throws EncryptionException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public PlainText decrypt(SecretKey arg0, CipherText arg1) throws EncryptionException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public CipherText encrypt(PlainText arg0) throws EncryptionException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public CipherText encrypt(SecretKey key, PlainText password) throws EncryptionException {
		return ESAPI.encryptor().encrypt(key,password);
	}
	@Override
	public long getRelativeTimeStamp(long arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public long getTimeStamp() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public String hash(String arg0, String arg1) throws EncryptionException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String hash(String arg0, String arg1, int arg2) throws EncryptionException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String seal(String arg0, long arg1) throws IntegrityException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String sign(String arg0) throws EncryptionException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String unseal(String arg0) throws EncryptionException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean verifySeal(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean verifySignature(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

}
