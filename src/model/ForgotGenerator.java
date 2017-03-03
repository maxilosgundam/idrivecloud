package model;
import java.util.UUID;
import java.util.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
public class ForgotGenerator {
	private String email;
	private String token;
	private String creationDate;
	private String expirationDate;
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}
	
	public String generateToken()
	{
		
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		System.out.println("uuid = " + uuid);
		return uuid;
		}
	
	private Date generateCreation()
	{
		Calendar cal = Calendar.getInstance();
		Date creationDate = cal.getTime();
		 return creationDate;
		}
	
	private Date generateExpiration()
	{
		Calendar expires = Calendar.getInstance();
		expires.set(Calendar.HOUR_OF_DAY, expires.get(Calendar.HOUR_OF_DAY) + 1);
		Date expireDate = expires.getTime();
		 return expireDate;
		}
	
	
	public void generateForgot(Connection connection, String email){
		try{
			String query ="INSERT INTO forgot(email, token, creation, expiration) values (?,?,?,?)";
			PreparedStatement pstmt = connection.prepareStatement(query);
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy H:mm:ss");	
			pstmt.setString(1, email);
			pstmt.setString(2, generateToken());
			pstmt.setString(3, sdf.format(generateCreation()));
			pstmt.setString(4, sdf.format(generateExpiration()));
			pstmt.executeUpdate();
		}catch(SQLException sqle){
			System.out.println(sqle);
		}
	}
	
	public void generateCleaner(Connection connection)
	{
		try{
			String query ="SELECT * FROM forgot";
			PreparedStatement pstmt = connection.prepareStatement(query);
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy H:mm:ss");	
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				 String expirationDate = (rs.getString("expiration"));
				 Date expiry = sdf.parse(expirationDate);
				 if(generateCreation().compareTo(expiry) >= 0)
				 {
					 String query3 = "DELETE From forgot WHERE expiration = ?";
				      PreparedStatement pstmt3 = connection.prepareStatement(query3);
				      pstmt3.setString(1, expirationDate);
				      pstmt3.executeUpdate();
				 	}
			}
			//pstmt.setString(1, sdf.format(creationDate));
			//pstmt.executeUpdate();
		}catch(SQLException sqle){
			System.out.println(sqle);
		}
		catch(ParseException pe)
		{
			System.out.println(pe);
			}
	
		}
	}
	