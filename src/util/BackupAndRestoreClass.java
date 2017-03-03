package util;

import util.BackupAndRestoreClass;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BackupAndRestoreClass {
	
	private static String backUpPath;
	private static String host;
	private static String mysqlPort;
	private static String dbUser;
	private static String dbPassword;
	private static String database;
	private static String mySQLPath;
	private static String mySQLRestorePath;
	private static String source;
	private static ResultSet rs;
    private static Connection con;
    private Statement st;
    private int BUFFER = 99999;
	
    
    public static String getHost() {
		return host;
	}

	public void setHost(String host) {
		BackupAndRestoreClass.host = host;
	}

	public static String getSource() {
		return source;
	}

	public void setSource(String source) {
		BackupAndRestoreClass.source = source;
	}
    
	public static String getMySQLPath() {
		return mySQLPath;
	}

	public void setMySQLPath(String mySQLPath) {
		BackupAndRestoreClass.mySQLPath = mySQLPath;
	}

	public static String getMySQLRestorePath() {
		return mySQLRestorePath;
	}

	public void setMySQLRestorePath(String mySQLRestorePath) {
		BackupAndRestoreClass.mySQLRestorePath = mySQLRestorePath;
	}

	public String getBackUpPath() {
		return backUpPath;
	}

	public void setBackUpPath(String backUpPath) {
		BackupAndRestoreClass.backUpPath = backUpPath;
	}

	public String getMysqlPort() {
		return mysqlPort;
	}

	public void setMysqlPort(String mysqlPort) {
		BackupAndRestoreClass.mysqlPort = mysqlPort;
	}

	public String getDbUser() {
		return dbUser;
	}

	public void setDbUser(String dbUser) {
		BackupAndRestoreClass.dbUser = dbUser;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public void setDbPassword(String dbPassword) {
		BackupAndRestoreClass.dbPassword = dbPassword;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		BackupAndRestoreClass.database = database;
	}
	

	public static boolean mysqlDatabaseRestore() { 
			
			boolean status=false;
			
			System.out.println(source);
			System.out.println(database);
			System.out.println(dbUser);
			System.out.println(dbPassword);
			String[] restoreCmd = new String[]{mySQLRestorePath,database, "--user=" + dbUser, "--password=" + dbPassword, "-e", " source " + source};
	        Process runtimeProcess;
	        
	        try {
	 
	            runtimeProcess = Runtime.getRuntime().exec(restoreCmd);
	            int processComplete = runtimeProcess.waitFor();
	 
	            if (processComplete == 0) {
	                System.out.println("Restored successfully!");
	                status = true;
	            } else {
	                System.out.println("Could not restore the backup!");
	            }
	            
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	        
			return status;
		}
	   
	 
	 
		 public static String mysqlDatabaseBackUp() {
			 String status="";
		       
		     File file = new File(backUpPath);
		     if (!file.exists()) {
		    	 if (file.mkdir()) {
				   System.out.println("Directory is created!");
				   BackupAndRestoreClass dbBackup = new BackupAndRestoreClass();
				   
		           try {
				       
				       byte[] data = dbBackup.getData(host, mysqlPort, dbUser, dbPassword, database).getBytes();   
				      
				       File fileDestination = new File(backUpPath+"\\"+database+".sql");
				       FileOutputStream destination = new FileOutputStream(fileDestination);       
				       destination.write(data);
				       
				       destination.close();
				       status="y";
				       System.out.println("Back Up Success");
				       
				       return status;
		           }catch (Exception ex){
		        	   
				       System.out.println("Back Up Failed");
				       status="n";
				       return status;
		    
		           }   
		                       
		    	 } else {
		    		 System.out.println("Failed to create directory!");
		    	 }
		    	 
		     } else {
		    	 
	            BackupAndRestoreClass dbBackup = new BackupAndRestoreClass();
	            
	            try {
			      
			       byte[] data = dbBackup.getData(host, mysqlPort, dbUser, dbPassword, database).getBytes();     
			      
			       File fileDestination = new File(backUpPath+"\\"+database+".sql");
			       FileOutputStream destination = new FileOutputStream(fileDestination);       
			       destination.write(data);
			       
			       destination.close();
		    
				  System.out.println("Back Up Success");
				  status ="y";
				  
				  return status;
			   }catch (Exception ex){
				  
			       System.out.println("Back Up Failed");
			       status="n";
			       return status;
			   
			   }   
		                      
		       }
		   
		 return status;
		 
		}
	    
	    
	    
	    
	    public String getData(String host, String port, String user, String password, String db) {
	        String mySQLPath = "C:\\xampp\\mysql\\bin\\";
	        try {
	            Class.forName("com.mysql.jdbc.Driver");
	        } catch (Exception e) {
	            e.printStackTrace();
	            System.out.print("cant access mysql driver");
	        }
	        try {
	            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + db, user, password);
	            st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
	        } catch (Exception e) {
	            System.out.print("connection error");
	            e.printStackTrace();
	        }
	            System.out.println(mySQLPath);
	        Process run = null;
	        try {
	            System.out.println(mySQLPath + "mysqldump --host=" + host + " --port=" + port + " --user=" + user + " --password=" + password + " --compact --complete-insert --extended-insert " + "--skip-comments --skip-triggers " + db);
	            run = Runtime.getRuntime().exec(mySQLPath + "mysqldump --host=" + host + " --port=" + port + " --user=" + user + " --password=" + password + "  " + "--skip-comments --skip-triggers " + db);
	        } catch (IOException ex) {
	           
	        }
	
	        InputStream in = run.getInputStream();
	        BufferedReader br = new BufferedReader(new InputStreamReader(in));
	        StringBuffer temp = new StringBuffer();
	   
	        int count;
	        char[] cbuf = new char[BUFFER];
	        try {
	            while ((count = br.read(cbuf, 0, BUFFER)) != -1) {
	                temp.append(cbuf, 0, count);
	            }
	        } catch (IOException ex) {
	          
	        }
	        try {
	            br.close();
	            in.close();
	        } catch (IOException ex) {
	           
	        }
	        return temp.toString();
	    }
}
