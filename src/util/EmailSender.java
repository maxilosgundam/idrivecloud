package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.PasswordAuthentication;
import javax.servlet.http.HttpServlet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
public class EmailSender extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private String reason;
	private Session session;
	private String sender;
	private String password;
	private String token;
	private String mailHost;
	private String gmail;
	private String mailPort;
	private String mailPortNumber;
	private String mailSSL;
	private String mailAuth;
	private String crest;
	private String crest2;
	private String logo;
	private String forgotAddress;
	
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getMailHost() {
		return mailHost;
	}

	public void setMailHost(String mailHost) {
		this.mailHost = mailHost;
	}

	public String getGmail() {
		return gmail;
	}

	public void setGmail(String gmail) {
		this.gmail = gmail;
	}

	public String getMailPort() {
		return mailPort;
	}

	public void setMailPort(String mailPort) {
		this.mailPort = mailPort;
	}

	public String getMailPortNumber() {
		return mailPortNumber;
	}

	public void setMailPortNumber(String mailPortNumber) {
		this.mailPortNumber = mailPortNumber;
	}

	public String getMailSSL() {
		return mailSSL;
	}

	public void setMailSSL(String mailSSL) {
		this.mailSSL = mailSSL;
	}

	public String getMailAuth() {
		return mailAuth;
	}

	public void setMailAuth(String mailAuth) {
		this.mailAuth = mailAuth;
	}

	public String getCrest() {
		return crest;
	}

	public void setCrest(String crest) {
		this.crest = crest;
	}

	public String getCrest2() {
		return crest2;
	}

	public void setCrest2(String crest2) {
		this.crest2 = crest2;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getForgotAddress() {
		return forgotAddress;
	}

	public void setForgotAddress(String forgotAddress) {
		this.forgotAddress = forgotAddress;
	}

	private void googleMailSession(){
		 Properties properties = System.getProperties();

		    properties.setProperty(mailHost, gmail);
		    properties.put(mailPort, mailPortNumber);
		    properties.put(mailSSL, "true");
		    properties.setProperty(mailAuth, "true");

		    session = Session.getDefaultInstance(properties,new javax.mail.Authenticator() {
		    	
		  			protected PasswordAuthentication getPasswordAuthentication() {
		  				
		  					return new PasswordAuthentication(sender, password);
		  					
		  				}
		  			
		  	});
	}
	//When reservation form is first sent to the managers or the administrator (if the managers are the ones
	//who sent it).
	public void reservationSent(Connection connection, int accType, int department, String employeeName, Date date, String hours, String minutes, String timeOfDay, String destination,
								String numPassengers, String purpose) throws IOException{
		googleMailSession();	
		 
		String receiver="";
		String receiverName="";
			int receiverAccType;
			if(accType == 1){
				receiverAccType = 2;
			}else{
				receiverAccType = 3;
			}
			
		/*
		 * StringBuilder contentBuilder = new StringBuilder();
			try {
			    BufferedReader in = new BufferedReader(new FileReader("mypage.html"));
			    String str;
			    while ((str = in.readLine()) != null) {
			        contentBuilder.append(str);
			    }
			    in.close();
			} catch (IOException e) {
			}
			String content = contentBuilder.toString();	
		 */
		MimeMessage message = new MimeMessage(session);
		MimeMultipart multipart = new MimeMultipart("related");
		
        String query = "SELECT * FROM employee WHERE departmentName = ? AND accountTypeID =  ?";
		try{
			if(receiverAccType==2){
				PreparedStatement pstmt = connection.prepareStatement(query);
				pstmt.setInt( 1, department);
				pstmt.setInt( 2, receiverAccType); 
				ResultSet rs =pstmt.executeQuery();
				if(rs.next()){
					receiver = rs.getString("email");
					receiverName = rs.getString("firstName") + " " + rs.getString("lastName");
				}else{
					
				}
			}else if(receiverAccType==3){
				PreparedStatement pstmt = connection.prepareStatement(query);
				pstmt.setInt(1, receiverAccType);
				pstmt.setInt(2, 1);
				ResultSet rs =pstmt.executeQuery();
				if(rs.next()){
					receiver = rs.getString("email");
					receiverName = rs.getString("firstName") + " " + rs.getString("lastName");
				}else{
					
				}
			}
			
			
			
			message.setFrom(new InternetAddress(sender));
			System.out.println(receiver);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
			message.setSubject("iDrive - New Reservation Request");
			/*BufferedReader in = new BufferedReader(new FileReader("C:/iDriveEmail/email.html/"));
			  String str;
			  while ((str = in.readLine()) != null) {
			        contentBuilder.append(str);
			    }
			    in.close();*/
			    System.out.println("Working Directory = " +
			              System.getProperty("user.dir"));    
			//String content = contentBuilder.toString();	
			//message.setText(content, "utf-8", "html");
			String html = ("<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>" +
			"<html xmlns='http://www.w3.org/1999/xhtml'>" +
					"<head>" +
			"<meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />" + 
					"<title>Email Content</title>" +
			"<meta name='viewport' content='width=device-width, initial-scale=1.0' />" +
					"</head>" +
			"<body style='background: url('http://iacademy.edu.ph/assets/themes/version2/images/portal-images/home-bg.jpg') no-repeat center center; background-size: cover; background-position: fixed; height: 100%;'>" +
			"<div align='center'>"	+	 
			"<tr>" +
					"<td>" + 
			 
					"<tr>" +
			"<td align='center' bgcolor='#1A7BC3' style='padding: 40px 0 30px 0;'>" + 
					"<img src=\"cid:crest2\" alt='Icon' width='100' height='100' style='display: block;' />" +
			"<img src=\"cid:logo-white\" alt='logo' style='display: block;' />" +
					"</td>" +
			"</tr>" +
					"<tr>" +
			"<td bgcolor='#ffffff' style='padding: 40px 30px 40px 30px;'>" +
					 
			"<h2 p style='font-family:arial;'>Good day, " + receiverName.toUpperCase() + "</h2>" + 
					"<h3 p style='font-family:arial;'>" + employeeName + " has requested for a trip reservation with the following details:</h3>" +
			"<tr>" +
					"<td>" + 
			"<p style='font-family:helvetica;''><b>&nbsp;Trip Date:</b> " + date + "</p>" +
					"<p style='font-family:helvetica;'><b>&nbsp;Departure Time:</b> " + hours + ":" + minutes + " " + timeOfDay + "</p>" +
			"<p style='font-family:helvetica;'><b>&nbsp;Destination:</b> " + destination + "</p>" +
					"<p style='font-family:helvetica;'><b>&nbsp;Number of Passengers:</b> " + numPassengers + "</p>" +
					"<p style='font-family:helvetica;'>&nbsp;The purpose of this trip is to as stated by " + employeeName + ": " + purpose + ".</p>" +
					"<br/>" +
					"<br/>" +
					"<br/>" +
					"<p style='color:red;text-align:center;'><b>PLEASE DO NOT REPLY, THIS IS AN AUTO-GENERATED EMAIL</b></p></td>" +
					"</tr>" +
					"</table>" +
					"</td>" +
					"</tr>" +
					"<tr>" +
					"<td bgcolor='#1A7BC3' style='padding: 30px 30px 30px 30px;'>" +
					 
					"<td width='75%'><footer>" +
					"<div class='row'>" +
					" <div style='width:150px;margin:0 auto;position: relative;margin-bottom: -13rem;' class='hidden-xs'>" +
					"<a href='http://iacademy.edu.ph/'><img class='img-responsive' src=\"cid:crest\" width='50' height='50' ></a>" +
					"</div>" +
					"<hr style='border-color: #00000;''>" +
					"<div class='row'>" +
					"<div class='col-md-4' style='text-align:left;'>" +
					"<span class='copyright'>Copyright © iACADEMY 2014</span>" +
					"</div>" +
					"</div>" +
					"</footer>" +
					"</table>" +
					"</td>" +
					"</tr>" +
					"</table>" +
					"</td>" +
					"</tr>" +
					"</table>" +
					"</div>" +
					"</body>" +
					"</html>");
			BodyPart messageBodyPart = new MimeBodyPart();
			
			messageBodyPart.setContent(html, "text/html");
			multipart.addBodyPart(messageBodyPart);
			
			messageBodyPart = new MimeBodyPart();
			DataSource fds = new FileDataSource(
					 crest);
			messageBodyPart.setDataHandler(new DataHandler(fds));	
			messageBodyPart.setHeader("Content-ID", "<crest>");
			multipart.addBodyPart(messageBodyPart);
			
			messageBodyPart = new MimeBodyPart();
			DataSource fds2 = new FileDataSource(
					logo);
			messageBodyPart.setDataHandler(new DataHandler(fds2));	
			messageBodyPart.setHeader("Content-ID", "<logo-white>");
			multipart.addBodyPart(messageBodyPart);
			
			messageBodyPart = new MimeBodyPart();
			DataSource fds3 = new FileDataSource(
					 crest2);
			messageBodyPart.setDataHandler(new DataHandler(fds3));	
			messageBodyPart.setHeader("Content-ID", "<crest2>");
			multipart.addBodyPart(messageBodyPart);
			
			message.setContent(multipart);
			
		/*	message.setText("Good Day, " + receiverName.toUpperCase() + " ! \n\n"
		 			+ employeeName + " has requested for a trip reservation with the following details: \n\n"
	         		+ "Trip Date: " + tripDate + "\n"
	         		+ "Departure Time: " + hours + ":" + minutes + " " + timeOfDay + "\n"
	         		+ "Destination: " + destination + "\n"
	         		+ "Number of Passengers: " + numPassengers + "\n\n"
	         		+ "The purpose of this trip is to as stated by " + employeeName + ": " + purpose + "\n" 
	         		+"\n\n<PLEASE DO NOT REPLY, THIS IS AN AUTO-GENERATED EMAIL>");
		*/

			Transport.send(message);
	
	         
	     }catch (MessagingException mex) {
	    	 mex.printStackTrace();
	     }catch(SQLException sqle){
	    	 System.out.println(sqle);
	     }
		
	}
	//When the pending reservation is approved by the manager.
	public void reservationApprovedManager(String receiver, String date){
		googleMailSession();
		try{

	         MimeMessage message = new MimeMessage(session);
	         MimeMultipart multipart = new MimeMultipart("related");
	         message.setFrom(new InternetAddress(sender));
	         message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
	         message.setSubject("Manager Approval");
	         /*message.setText("Good Day! Your reservation on " + date + " has been approved by your Department Manager."
	         		+ "It is now currently pending for approval by the Administrator."+"\n\n<PLEASE DO NOT REPLY, THIS IS AN AUTO-GENERATED EMAIL>");*/
	         BodyPart messageBodyPart = new MimeBodyPart();
			 String html = ("<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>" +
						"<html xmlns='http://www.w3.org/1999/xhtml'>" +
						"<head>" +
				"<meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />" + 
						"<title>Email Content</title>" +
				"<meta name='viewport' content='width=device-width, initial-scale=1.0' />" +
						"</head>" +
				"<body style='background: url('http://iacademy.edu.ph/assets/themes/version2/images/portal-images/home-bg.jpg') no-repeat center center; background-size: cover; background-position: fixed; height: 100%;'>" +
				"<div align='center'>"	+	 
				"<tr>" +
						"<td>" + 
				 
						"<tr>" +
				"<td align='center' bgcolor='#1A7BC3' style='padding: 40px 0 30px 0;'>" + 
						"<img src=\"cid:crest2\" alt='Icon' width='100' height='100' style='display: block;' />" +
				"<img src=\"cid:logo-white\" alt='logo' style='display: block;' />" +
						"</td>" +
				"</tr>" +
						"<tr>" +
				"<td bgcolor='#ffffff' style='padding: 40px 30px 40px 30px;'>" +
						 
				"<h2 p style='font-family:arial;'>Good day Employee,</h2>" + 
						"<h3 p style='font-family:arial;'>" + "Your reservation on " +  date + " has been approved by the manager. </h3>" +
				"<tr>" +
						"<td>" + 
						"<br/>" +
						"<br/>" +
						"<br/>" +
						"<p style='color:red;text-align:center;'><b>PLEASE DO NOT REPLY, THIS IS AN AUTO-GENERATED EMAIL</b></p></td>" +
						"</tr>" +
						"</table>" +
						"</td>" +
						"</tr>" +
						"<tr>" +
						"<td bgcolor='#1A7BC3' style='padding: 30px 30px 30px 30px;'>" +
						 
						"<td width='75%'><footer>" +
						"<div class='row'>" +
						" <div style='width:150px;margin:0 auto;position: relative;margin-bottom: -13rem;' class='hidden-xs'>" +
						"<a href='http://iacademy.edu.ph/'><img class='img-responsive' src=\"cid:crest\" width='50' height='50' ></a>" +
						"</div>" +
						"<hr style='border-color: #00000;''>" +
						"<div class='row'>" +
						"<div class='col-md-4' style='text-align:left;'>" +
						"<span class='copyright'>Copyright © iACADEMY 2014</span>" +
						"</div>" +
						"</div>" +
						"</footer>" +
						"</table>" +
						"</td>" +
						"</tr>" +
						"</table>" +
						"</td>" +
						"</tr>" +
						"</table>" +
						"</div>" +
						"</body>" +
						"</html>");	
				messageBodyPart.setContent(html, "text/html");
				multipart.addBodyPart(messageBodyPart);
				
				messageBodyPart = new MimeBodyPart();
				DataSource fds = new FileDataSource(
						 crest);
				messageBodyPart.setDataHandler(new DataHandler(fds));	
				messageBodyPart.setHeader("Content-ID", "<crest>");
				multipart.addBodyPart(messageBodyPart);
		
				messageBodyPart = new MimeBodyPart();
				DataSource fds2 = new FileDataSource(
						logo);
				messageBodyPart.setDataHandler(new DataHandler(fds2));	
				messageBodyPart.setHeader("Content-ID", "<logo-white>");
				multipart.addBodyPart(messageBodyPart);
				
				messageBodyPart = new MimeBodyPart();
				DataSource fds3 = new FileDataSource(
						 crest2);
				messageBodyPart.setDataHandler(new DataHandler(fds3));	
				messageBodyPart.setHeader("Content-ID", "<crest2>");
				multipart.addBodyPart(messageBodyPart);
				
				message.setContent(multipart);
	         Transport.send(message);
	         
	     }catch (MessagingException mex) {
	    	 mex.printStackTrace();
	     }
	}
	//When the pending reservation is approved by the administrator. This will include a QR code.
	public void reservationApprovedAdministrator(String receiver, String date){
		googleMailSession();
		try{

	         MimeMessage message = new MimeMessage(session);
	         MimeMultipart multipart = new MimeMultipart("related");
	         message.setFrom(new InternetAddress(sender));
	         message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
	         message.setSubject("Administrator Approval");
	         
	         /*message.setText("Good Day! Your reservation on " + date + " has been approved by the Administrator.");
	         message.setText("<PLEASE DO NOT REPLY, THIS IS AN AUTO-GENERATED EMAIL>");*/
	         BodyPart messageBodyPart = new MimeBodyPart();
			 String html = ("<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>" +
						"<html xmlns='http://www.w3.org/1999/xhtml'>" +
						"<head>" +
				"<meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />" + 
						"<title>Email Content</title>" +
				"<meta name='viewport' content='width=device-width, initial-scale=1.0' />" +
						"</head>" +
				"<body style='background: url('http://iacademy.edu.ph/assets/themes/version2/images/portal-images/home-bg.jpg') no-repeat center center; background-size: cover; background-position: fixed; height: 100%;'>" +
				"<div align='center'>"	+	 
				"<tr>" +
						"<td>" + 
				 
						"<tr>" +
				"<td align='center' bgcolor='#1A7BC3' style='padding: 40px 0 30px 0;'>" + 
						"<img src=\"cid:crest2\" alt='Icon' width='100' height='100' style='display: block;' />" +
				"<img src=\"cid:logo-white\" alt='logo' style='display: block;' />" +
						"</td>" +
				"</tr>" +
						"<tr>" +
				"<td bgcolor='#ffffff' style='padding: 40px 30px 40px 30px;'>" +
						 
				"<h2 p style='font-family:arial;'>Good day,</h2>" + 
						"<h3 p style='font-family:arial;'>" + "Your reservation on " +  date + " has been approved by the administrator. </h3>" +
				"<tr>" +
						"<td>" + 
						"<br/>" +
						"<br/>" +
						"<br/>" +
						"<p style='color:red;text-align:center;'><b>PLEASE DO NOT REPLY, THIS IS AN AUTO-GENERATED EMAIL</b></p></td>" +
						"</tr>" +
						"</table>" +
						"</td>" +
						"</tr>" +
						"<tr>" +
						"<td bgcolor='#1A7BC3' style='padding: 30px 30px 30px 30px;'>" +
						 
						"<td width='75%'><footer>" +
						"<div class='row'>" +
						" <div style='width:150px;margin:0 auto;position: relative;margin-bottom: -13rem;' class='hidden-xs'>" +
						"<a href='http://iacademy.edu.ph/'><img class='img-responsive' src=\"cid:crest\" width='50' height='50' ></a>" +
						"</div>" +
						"<hr style='border-color: #00000;''>" +
						"<div class='row'>" +
						"<div class='col-md-4' style='text-align:left;'>" +
						"<span class='copyright'>Copyright © iACADEMY 2014</span>" +
						"</div>" +
						"</div>" +
						"</footer>" +
						"</table>" +
						"</td>" +
						"</tr>" +
						"</table>" +
						"</td>" +
						"</tr>" +
						"</table>" +
						"</div>" +
						"</body>" +
						"</html>");	
				messageBodyPart.setContent(html, "text/html");
				multipart.addBodyPart(messageBodyPart);
				
				messageBodyPart = new MimeBodyPart();
				DataSource fds = new FileDataSource(
						 crest);
				messageBodyPart.setDataHandler(new DataHandler(fds));	
				messageBodyPart.setHeader("Content-ID", "<crest>");
				multipart.addBodyPart(messageBodyPart);
				
				messageBodyPart = new MimeBodyPart();
				DataSource fds2 = new FileDataSource(
						logo);
				messageBodyPart.setDataHandler(new DataHandler(fds2));	
				messageBodyPart.setHeader("Content-ID", "<logo-white>");
				multipart.addBodyPart(messageBodyPart);
				
				messageBodyPart = new MimeBodyPart();
				DataSource fds3 = new FileDataSource(
						 crest2);
				messageBodyPart.setDataHandler(new DataHandler(fds3));	
				messageBodyPart.setHeader("Content-ID", "<crest2>");
				multipart.addBodyPart(messageBodyPart);
				
				message.setContent(multipart);
	         
	         Transport.send(message);
	         
	     }catch (MessagingException mex) {
	    	 mex.printStackTrace();
	     }
	}
	//When the pending reservation is denied by the manager or the administrator.
	public void reservationDenied(String receiver, String date, String reason, String name){
		googleMailSession();
		System.out.println("Reservation was denied.");
		try{

	         MimeMessage message = new MimeMessage(session);
	         MimeMultipart multipart = new MimeMultipart("related");
	         message.setFrom(new InternetAddress(sender));
	         message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
	         message.setSubject("Reservation Denied");
	         /*message.setText("Good Day! Your reservation on " + date + " has been denied due to:"
	         		+ reason);
	         message.setText("<PLEASE DO NOT REPLY, THIS IS AN AUTO-GENERATED EMAIL>");*/
	         
	         BodyPart messageBodyPart = new MimeBodyPart();
			 String html = ("<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>" +
				"<html xmlns='http://www.w3.org/1999/xhtml'>" +
				"<head>" +
		"<meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />" + 
				"<title>Email Content</title>" +
		"<meta name='viewport' content='width=device-width, initial-scale=1.0' />" +
				"</head>" +
		"<body style='background: url('http://iacademy.edu.ph/assets/themes/version2/images/portal-images/home-bg.jpg') no-repeat center center; background-size: cover; background-position: fixed; height: 100%;'>" +
		"<div align='center'>"	+	 
		"<tr>" +
				"<td>" + 
		 
				"<tr>" +
		"<td align='center' bgcolor='#1A7BC3' style='padding: 40px 0 30px 0;'>" + 
				"<img src=\"cid:crest2\" alt='Icon' width='100' height='100' style='display: block;' />" +
		"<img src=\"cid:logo-white\" alt='logo' style='display: block;' />" +
				"</td>" +
		"</tr>" +
				"<tr>" +
		"<td bgcolor='#ffffff' style='padding: 40px 30px 40px 30px;'>" +
				 
		"<h2 p style='font-family:arial;'>Good day, " + name + "</h2>" + 
				"<h3 p style='font-family:arial;'>" + "Your reservation on " +  date + " has been denied due to the following reason/s: </h3>" +
		"<tr>" +
				"<td>" + 
		"<p style='font-family:helvetica;''>" + reason + "</p>" +
				"<br/>" +
				"<br/>" +
				"<br/>" +
				"<p style='color:red;text-align:center;'><b>PLEASE DO NOT REPLY, THIS IS AN AUTO-GENERATED EMAIL</b></p></td>" +
				"</tr>" +
				"</table>" +
				"</td>" +
				"</tr>" +
				"<tr>" +
				"<td bgcolor='#1A7BC3' style='padding: 30px 30px 30px 30px;'>" +
				 
				"<td width='75%'><footer>" +
				"<div class='row'>" +
				" <div style='width:150px;margin:0 auto;position: relative;margin-bottom: -13rem;' class='hidden-xs'>" +
				"<a href='http://iacademy.edu.ph/'><img class='img-responsive' src=\"cid:crest\" width='50' height='50' ></a>" +
				"</div>" +
				"<hr style='border-color: #00000;''>" +
				"<div class='row'>" +
				"<div class='col-md-4' style='text-align:left;'>" +
				"<span class='copyright'>Copyright © iACADEMY 2014</span>" +
				"</div>" +
				"</div>" +
				"</footer>" +
				"</table>" +
				"</td>" +
				"</tr>" +
				"</table>" +
				"</td>" +
				"</tr>" +
				"</table>" +
				"</div>" +
				"</body>" +
				"</html>");	
				messageBodyPart.setContent(html, "text/html");
				multipart.addBodyPart(messageBodyPart);
				
				messageBodyPart = new MimeBodyPart();
				DataSource fds = new FileDataSource(
						 crest);
				messageBodyPart.setDataHandler(new DataHandler(fds));	
				messageBodyPart.setHeader("Content-ID", "<crest>");
				multipart.addBodyPart(messageBodyPart);
				
				messageBodyPart = new MimeBodyPart();
				DataSource fds2 = new FileDataSource(
						logo);
				messageBodyPart.setDataHandler(new DataHandler(fds2));	
				messageBodyPart.setHeader("Content-ID", "<logo-white>");
				multipart.addBodyPart(messageBodyPart);
				
				messageBodyPart = new MimeBodyPart();
				DataSource fds3 = new FileDataSource(
						 crest2);
				messageBodyPart.setDataHandler(new DataHandler(fds3));	
				messageBodyPart.setHeader("Content-ID", "<crest2>");
				multipart.addBodyPart(messageBodyPart);
				
				message.setContent(multipart);
	         
	         Transport.send(message);
	         
	     }catch (MessagingException mex) {
	    	 mex.printStackTrace();
	     }
	}
	
	public void forgotSent(Connection connection, String email){
			googleMailSession();	
			
			String receiver=email;
			
			
			/*
			* StringBuilder contentBuilder = new StringBuilder();
			try {
			BufferedReader in = new BufferedReader(new FileReader("mypage.html"));
			String str;
			while ((str = in.readLine()) != null) {
			contentBuilder.append(str);
			}
			in.close();
			} catch (IOException e) {
			}
			String content = contentBuilder.toString();	
			*/
			MimeMessage message = new MimeMessage(session);
			MimeMultipart multipart = new MimeMultipart("related");
			String query = "SELECT * FROM forgot WHERE email = ?";
			try{
			
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setString(1, receiver);
			ResultSet rs =pstmt.executeQuery();
			if(rs.next()){
			setToken(rs.getString("token"));
			}else{
			
			}	
			message.setFrom(new InternetAddress(sender));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
			message.setSubject("iDrive - Forgot Password");
//			BufferedReader in = new BufferedReader(new FileReader("C:/iDriveEmail/email.html/"));
//			String str;
//			while ((str = in.readLine()) != null) {
//			contentBuilder.append(str);
//			}
//			in.close();
			/*message.setText(content, "utf-8", "html");
			message.setText("localhost:8080/iDriveTheSecond/forgot.jsp?token=" + getToken());*/
			BodyPart messageBodyPart = new MimeBodyPart();
			 String html = ("<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>" +
						"<html xmlns='http://www.w3.org/1999/xhtml'>" +
						"<head>" +
				"<meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />" + 
						"<title>Email Content</title>" +
				"<meta name='viewport' content='width=device-width, initial-scale=1.0' />" +
						"</head>" +
				"<body style='background: url('http://iacademy.edu.ph/assets/themes/version2/images/portal-images/home-bg.jpg') no-repeat center center; background-size: cover; background-position: fixed; height: 100%;'>" +
				"<div align='center'>"	+	 
				"<tr>" +
						"<td>" + 
				 
						"<tr>" +
				"<td align='center' bgcolor='#1A7BC3' style='padding: 40px 0 30px 0;'>" + 
						"<img src=\"cid:crest2\" alt='Icon' width='100' height='100' style='display: block;' />" +
				"<img src=\"cid:logo-white\" alt='logo' style='display: block;' />" +
						"</td>" +
				"</tr>" +
						"<tr>" +
				"<td bgcolor='#ffffff' style='padding: 40px 30px 40px 30px;'>" +
						 
				"<h2 p style='font-family:arial;'>Good day,</h2>" + 
						"<h3 p style='font-family:arial;'>" + "You may now change your password by clicking on this <a href='"+ getForgotAddress() + getToken() + "'>link.</a></h3>" +
				"<tr>" +
						"<td>" + 
						"<br/>" +
						"<br/>" +
						"<br/>" +
						"<p style='color:red;text-align:center;'><b>PLEASE DO NOT REPLY, THIS IS AN AUTO-GENERATED EMAIL</b></p></td>" +
						"</tr>" +
						"</table>" +
						"</td>" +
						"</tr>" +
						"<tr>" +
						"<td bgcolor='#1A7BC3' style='padding: 30px 30px 30px 30px;'>" +
						 
						"<td width='75%'><footer>" +
						"<div class='row'>" +
						" <div style='width:150px;margin:0 auto;position: relative;margin-bottom: -13rem;' class='hidden-xs'>" +
						"<a href='http://iacademy.edu.ph/'><img class='img-responsive' src=\"cid:crest\" width='50' height='50' ></a>" +
						"</div>" +
						"<hr style='border-color: #00000;''>" +
						"<div class='row'>" +
						"<div class='col-md-4' style='text-align:left;'>" +
						"<span class='copyright'>Copyright © iACADEMY 2014</span>" +
						"</div>" +
						"</div>" +
						"</footer>" +
						"</table>" +
						"</td>" +
						"</tr>" +
						"</table>" +
						"</td>" +
						"</tr>" +
						"</table>" +
						"</div>" +
						"</body>" +
						"</html>");	
				messageBodyPart.setContent(html, "text/html");
				multipart.addBodyPart(messageBodyPart);
				
				
				messageBodyPart = new MimeBodyPart();
				DataSource fds = new FileDataSource(
						 crest);
				messageBodyPart.setDataHandler(new DataHandler(fds));	
				messageBodyPart.setHeader("Content-ID", "<crest>");
				multipart.addBodyPart(messageBodyPart);
			
				
				messageBodyPart = new MimeBodyPart();
				DataSource fds2 = new FileDataSource(
						logo);
				messageBodyPart.setDataHandler(new DataHandler(fds2));	
				messageBodyPart.setHeader("Content-ID", "<logo-white>");
				multipart.addBodyPart(messageBodyPart);
				
				messageBodyPart = new MimeBodyPart();
				DataSource fds3 = new FileDataSource(
						  crest2);
				messageBodyPart.setDataHandler(new DataHandler(fds3));	
				messageBodyPart.setHeader("Content-ID", "<crest2>");
				multipart.addBodyPart(messageBodyPart);
				
				message.setContent(multipart);
			
			/*	message.setText("Good Day, " + receiverName.toUpperCase() + " ! \n\n"
				+ employeeName + " has requested for a trip reservation with the following details: \n\n"
				+ "Trip Date: " + tripDate + "\n"
				+ "Departure Time: " + hours + ":" + minutes + " " + timeOfDay + "\n"
				+ "Destination: " + destination + "\n"
				+ "Number of Passengers: " + numPassengers + "\n\n"
				+ "The purpose of this trip is to as stated by " + employeeName + ": " + purpose + "\n" 
				+"\n\n<PLEASE DO NOT REPLY, THIS IS AN AUTO-GENERATED EMAIL>");
			*/
			Transport.send(message);	
			
			}catch(AddressException ae)
			{
				ae.printStackTrace();
			}catch (MessagingException mex) {
			mex.printStackTrace();
			}catch(SQLException sqle){
			System.out.println(sqle);
			}
			
			}
}