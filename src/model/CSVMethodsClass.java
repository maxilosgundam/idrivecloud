package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.servlet.http.Part;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.crypto.CipherText;
import org.owasp.esapi.crypto.PlainText;
import org.owasp.esapi.errors.EncryptionException;
import org.owasp.esapi.errors.IntegrityException;
import org.owasp.esapi.reference.FileBasedAuthenticator;

public class CSVMethodsClass implements org.owasp.esapi.Encryptor {
	private String file;
	private String iSalt;
	
	
	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}
	
	public String getiSalt() {
		return iSalt;
	}

	public void setiSalt(String iSalt) {
		this.iSalt = iSalt;
	}
	
	private void readCSV(Connection connection){
		
		try {
			BufferedReader br = new BufferedReader(new FileReader("C:/iDriveCSV/"+file));
			
			//String headerLine = br.readLine(); used to skip the first row.
			String line = br.readLine();
			
			while (line != null){
				
				String columns[] = line.split(",");
				
				if(columns.length == 6){
					if(validateName(columns[0]) == true && validateName(columns[1]) == true && validateEmail(columns[2]) == true
							&& validateID(columns[4]) == true && validatePosition(columns[5])==true&&validateDepartments(columns[3])==true){
						System.out.println("\nFirst Name: " + columns[0]);
						System.out.println("Last Name: " + columns[1]);
						System.out.println("Email: " + columns[2]);
						System.out.println("Department: " + columns[3]);
						System.out.println("Employee ID: " + Integer.parseInt(columns[4]));
						System.out.println("Position: " + columns[5]);
					} 
				} else if (columns.length == 7){
					if(validateName(columns[0]) == true && validateName(columns[1]) == true && validateEmail(columns[2]) == true
							&& validateID(columns[4]) == true && validatePosition(columns[5])==true && validateAction(columns[6]) == true&&validateDepartments(columns[3])==true){
						System.out.println("\nFirst Name: " + columns[0]);
						System.out.println("Last Name: " + columns[1]);
						System.out.println("Email: " + columns[2]);
						System.out.println("Department: " + columns[3]);
						System.out.println("Employee ID: " + Integer.parseInt(columns[4]));
						System.out.println("Position: " + columns[5]);
						System.out.println("Action: " + columns[6]);
						boolean exists = duplicate(connection, Integer.parseInt(columns[4]));
						executeQuery(connection, columns, exists);
					}
				} else {
					System.out.println(columns.length);
				}
				line = br.readLine();
				
				
		}
			
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static boolean validateName(String text){
		String regex = "^[\\p{L} .'-]+$";
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(text);
		return matcher.find();
	}
	
	private static boolean validatePosition(String text){
		if(text.equals("Employee")){
			return true;
		}else if(text.equals("Manager")){
			return true;
		}else if(text.equals("Admin")){
			return true;
		}else{
			return false;
		}
	}
	
	private static boolean validateEmail(String text){
		String regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(text);
		return matcher.matches();
	}
	
	private static boolean validateID(String text){
		if(text.matches("[0-9]+") && text.length() == 6){
			return true;
		} else {
			return false;
		}
	}
	
	private static boolean validateAction(String text){
		if(text.length() == 1){
			if(text.toUpperCase().equals("A") || text.toUpperCase().equals("D") || text.toUpperCase().equals("U")){
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	private static boolean validateDepartments(String text){
		if(text.matches("[0-9]+")){
			return true;
		} else {
			return false;
		}
    }
	
	private void checkDirectory(){
		File file = new File("C:\\iDriveCSV");
		
		if(!file.exists()){
			if(file.mkdir()){
				System.out.println("Directory Created!");
			} else {
				System.out.println("Failed to create Directory!");
			}
		} else {
			System.out.println("Directory already exists!");
		}
	}
	
	private void createFile(Part filePart) throws IOException{
		
		OutputStream out = null;
		InputStream fileContent = null;
		
		try{
			out = new FileOutputStream(new File("C:/iDriveCSV/"+file));
			fileContent = filePart.getInputStream();
			
			int read = 0;
		        final byte[] bytes = new byte[1024];

		        while ((read = fileContent.read(bytes)) != -1) {
		            out.write(bytes, 0, read);
		        }
		        
		        System.out.println("New file " + file + " created at " + "C:/iDriveCSV/"+file);
		        
		} catch (FileNotFoundException fne){
			System.out.println("File was not found!");
		} catch (IOException io){
			System.out.println("IO Exception!");
		} finally {
			 if (out != null) {
		            out.close();
		        }
		        if (fileContent != null) {
		            fileContent.close();
		        }
		}
	}
	
	private boolean duplicate(Connection connection, int empID){
		try{
			String duplicateChecker = "SELECT * FROM employee WHERE employeeID = ?";
			PreparedStatement pstmt = connection.prepareStatement(duplicateChecker);
			pstmt.setInt(1, empID);
			ResultSet rs = 	pstmt.executeQuery();
			if(rs.next()){
				return true;
			}else{
				return false;
			}
		}catch(SQLException sqle){
			System.out.println(sqle);
			return false;
		}
		
	}
	
	private void executeQuery(Connection connection, String columns[], boolean result){
		try{

			int accType = 0;
			KeyGenerator kg = KeyGenerator.getInstance("AES");
			kg.init(128);
			SecretKey sk = kg.generateKey();
			if(columns[5].toUpperCase().equals("MANAGER")){
				accType =2;
			}else if(columns[5].toUpperCase().equals("ADMIN")){
				accType=3;
			}else{
				accType = 1;
			}

			String query="";
			if(columns[6].toUpperCase().equals("A") && result == false){
				query ="INSERT INTO employee (employeeID, firstName, lastName, departmentName, email, pass, accountTypeID) values (?,?,?,?,?,?,?)";
				PreparedStatement pstmt = connection.prepareStatement(query);
				pstmt.setInt(1, Integer.parseInt(columns[4])); //empId
				pstmt.setString(2, columns[0]); //firstname
				pstmt.setString(3, columns[1]); //lastname
				pstmt.setString(4, columns[3].toUpperCase()); //department
				pstmt.setString(5,columns[2]); //email
				pstmt.setString(6, hash(columns[4]+columns[1], iSalt));//pass (default is employee num+lastname)
				pstmt.setInt(7, accType); //accountTypeID
				pstmt.executeUpdate();
			}else if(columns[6].toUpperCase().equals("D") && result == true){
				query="DELETE FROM employee WHERE employeeID = ?";
				PreparedStatement pstmt = connection.prepareStatement(query);
				pstmt.setInt(1, Integer.parseInt(columns[4])); //empId
				pstmt.executeUpdate();
			}else if(columns[6].toUpperCase().equals("U") && result == true){
				query="UPDATE employee SET employeeID = ? , firstName = ? , lastName = ? , departmentName = ? , email = ? , accountTypeID = ? WHERE employeeID = ?";
				PreparedStatement pstmt = connection.prepareStatement(query);
				pstmt.setInt(1, Integer.parseInt(columns[4])); //empId
				pstmt.setString(2, columns[0]); //firstname
				pstmt.setString(3, columns[1]); //lastname
				pstmt.setString(4, columns[3].toUpperCase()); //department
				pstmt.setString(5,columns[2]); //email
				pstmt.setInt(6, accType); //accountTypeID
				pstmt.setInt(7,Integer.parseInt(columns[4]));
				pstmt.executeUpdate();
			}else{
//				query="SELECT * FROM employee WHERE employeeID = ?";
//				PreparedStatement pstmt = connection.prepareStatement(query);
//				pstmt.setInt(1, Integer.parseInt(columns[4])); //empId
//				pstmt.executeUpdate();
			}
		}catch(SQLException sqle){
			System.out.println(sqle);
		}
		catch(EncryptionException ee)
		{
			System.out.println(ee);
			}
		catch(NoSuchAlgorithmException nsae)
		{
			System.out.println(nsae);
			}
	}
	
	public void process(Part filePart, Connection connection) throws IOException{
		checkDirectory();
		createFile(filePart);
		readCSV(connection);
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
