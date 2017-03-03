package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.crypto.SecretKey;

import org.owasp.esapi.crypto.CipherText;
import org.owasp.esapi.crypto.PlainText;
import org.owasp.esapi.errors.EncryptionException;
import org.owasp.esapi.errors.IntegrityException;
import org.owasp.esapi.reference.FileBasedAuthenticator;

public class ChangePasswordClass implements org.owasp.esapi.Encryptor {
	private String password;
	private String token;
	private String email;
	private String iSalt;


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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	public String getiSalt() {
		return iSalt;
	}

	public void setiSalt(String iSalt) {
		this.iSalt = iSalt;
	}


	
	public void changePassword(Connection connection, String password, String token)
	{
		try{
			System.out.println("this is my shingaling " + token);
			String query ="SELECT * FROM forgot WHERE token = ?";
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setString(1, token);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				 setEmail(rs.getString("email"));
			}
			System.out.println("this my though " + email);
			String query2 ="UPDATE employee SET pass=? WHERE email=?";
			PreparedStatement pstmt2 = connection.prepareStatement(query2);
			pstmt2.setString(1, hash(password, iSalt));
			pstmt2.setString(2, getEmail());
			pstmt2.executeUpdate();
			
			String query3 = "DELETE From forgot WHERE token = ?";
		      PreparedStatement pstmt3 = connection.prepareStatement(query3);
		      pstmt3.setString(1, token);
		      pstmt3.executeUpdate();
		}catch(SQLException sqle){
			System.out.println(sqle);
		}
		catch(EncryptionException ee)
		{
			System.out.println(ee);
			}
		}
	
	public void changePassword(Connection connection, String password){
		
		try{
			String query = "UPDATE employee SET pass=? WHERE email=?";
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setString(1, hash(password, iSalt));
			pstmt.setString(2, getEmail());
			pstmt.executeUpdate();
			
		} catch (SQLException sqle){
			System.out.println(sqle);	
		}
		catch(EncryptionException ee)
		{
			System.out.println(ee);
			}
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
	public CipherText encrypt(SecretKey arg0, PlainText arg1) throws EncryptionException {
		// TODO Auto-generated method stub
		return null;
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
	public String hash(String password, String username) throws EncryptionException {
		return FileBasedAuthenticator.getInstance().hashPassword(password, username);
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
