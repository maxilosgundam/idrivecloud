package model;

import java.sql.SQLException;
import java.sql.*;
import java.io.*;
import java.util.*;
public class ImageClass {
	public static byte[] getPhoto (Connection conn, int count)
		       throws Exception, SQLException
		  {
		    String req = "" ;
		    Blob img ;
		    byte[] imgData = null ;
		    Statement stmt = conn.createStatement ();
		    
		    // Query
		    req = "Select images From cars Where id =" + count;
		    
		    ResultSet rset  = stmt.executeQuery ( req ); 
		    
		    while (rset.next ())
		    {    
		      img = rset.getBlob(1);
		      imgData = img.getBytes(1,(int)img.length());
		    }
		    
		    
		    
		    rset.close();
		    stmt.close();
		    
		    return imgData ;
		  }
	
	public static int counter (Connection conn)
			throws Exception, SQLException
	{
		String req = "" ;
		int counter = 0;
		Statement stmt = conn.createStatement ();
	    
	    // Query
	    req = "Select images From cars";
	    
	    ResultSet rset  = stmt.executeQuery ( req ); 
	    
	    while (rset.next ())
	    {    
	      counter++;
	    }    
	    
	    rset.close();
	    stmt.close();
	    return counter;
		}
}
