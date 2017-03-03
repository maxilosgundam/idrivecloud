package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.sql.Connection;

public class InputValidationClass {
	
	
	public boolean dateInputValidation(String date){
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		Calendar yearCalendar = Calendar.getInstance();
		Date dateThreeDaysFromNow = new Date();
		Date dateThreeMonthsFromNow = new Date();
		calendar.setTime(dateThreeDaysFromNow);
		calendar.add(Calendar.DATE, 3);
		yearCalendar.setTime(dateThreeDaysFromNow);
		yearCalendar.add(Calendar.MONTH, 3);
		dateThreeDaysFromNow = calendar.getTime();
		dateThreeMonthsFromNow = yearCalendar.getTime();
		
		if(date == null){
			return false;
		} else {
			
			String regex = "[^0-9-]";
			Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(date);
			
			if(matcher.find() == true){
				
				System.out.println("special character found.");
				return false;
				
			} else if(date.trim().isEmpty() == true){
				
				System.out.println("all whitespace");
				return false;
				
			} else {
				System.out.println("all valid characters");
				try {
					Date inputDate = dateFormat.parse(dateFormat.format(dateFormat.parse(date)));
					dateThreeDaysFromNow = dateFormat.parse(dateFormat.format(dateThreeDaysFromNow));
					dateThreeMonthsFromNow = dateFormat.parse(dateFormat.format(dateThreeMonthsFromNow));
					
					if(inputDate.equals(dateThreeMonthsFromNow) || inputDate.before(dateThreeMonthsFromNow)){
						
						System.out.println("Valid Year");
						
						if(inputDate.equals(dateThreeDaysFromNow) || inputDate.after(dateThreeDaysFromNow)){
							System.out.println("Correct Date, you entered " + inputDate.toString());
							return true;
						} else {
							System.out.println("Wrong date, you entered " +inputDate.toString() + " must be on or after this date: " + dateThreeDaysFromNow.toString());
							return false;
						}
					} else {
						System.out.println("You're not from the future.");
						return false;
					}
					
						
						
				} catch (ParseException e) {
					e.printStackTrace();
				}
				return true;
			}
		}
	}
	
	public boolean timeInputValidation(String hours, String minutes, String timeOfDay){
	
		if(hours == null || minutes == null || timeOfDay == null){
			return false;
		} else {
			
			String regex = "[^0-9]";
			Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(hours);
			Matcher matcher1 = pattern.matcher(minutes);
			
			if(matcher.find() == true || matcher1.find() == true){
				
				System.out.println("special character found.");
				return false;
				
			} else if(hours.trim().isEmpty() == true || minutes.trim().isEmpty() == true || timeOfDay.trim().isEmpty() == true){
				
				System.out.println("all whitespace");
				return false;
				
			} else {
				System.out.println("all valid characters");
				int intHours = Integer.parseInt(hours);
				int intMinutes = Integer.parseInt(minutes);
				
				if(intHours < 1 || intHours > 12){
					return false;
				}
				
				if(intMinutes < 0 || intMinutes > 60){
					return false;
				}
				
				if(timeOfDay.equals("AM") == false && timeOfDay.equals("PM") == false){
					return false;
				}
				return true;
			}

		}
	}
	
	public boolean destinationInputValidation(String text){
		if(text == null){
			System.out.println("null text");
			return false;
		} else {
			String regex = "[^A-Za-z0-9 ]";
			Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(text);
			
			if(matcher.find() == true){
				
				System.out.println("special character found.");
				return false;
				
			} else if(text.trim().isEmpty() == true){
				
				System.out.println("all whitespace");
				return false;
				
			} else if(text.length()>50){
				System.out.println("greater than destination length");
				return false;
			}else{
				System.out.println("all valid characters");
				return true;
			}
		}
	}
	
	public boolean purposeInputValidation(String text){
		if(text == null){
			System.out.println("null text");
			return false;
		} else {
			String regex = "[^A-Za-z0-9 .!?,$&-]";
			Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(text);
			
			if(matcher.find() == true){
				
				System.out.println("special character found.");
				return false;
				
			} else if(text.trim().isEmpty() == true){
				
				System.out.println("all whitespace");
				return false;
				
			} else if(text.length() > 500){
				
				System.out.println("max length error");
				return false;
				
			} else {
				System.out.println("all valid characters");
				return true;
			}
		}
	}
	
	public boolean numberInputValidation(String text){
		if(text == null){
			return false;
		} else {
			char check[] = text.toCharArray();
			
			for(int i = 0; i < text.length(); i++){
				if(Character.isDigit(check[i]) == true){
					
				} else {
					return false;
				}
			}
			
			if(Integer.parseInt(text) < 0 || Integer.parseInt(text) > 25){
				return false;
			}
			
			return true;
		}
	}
	
	public boolean passengerInputValidation(String text){
		if(text == null){
			return true;
		} else {
			String regex = "[^A-Za-z .,]";
            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(text);

            if(matcher.find() == true){

                System.out.println("special character found.");
                return false;

            } else if(text.trim().isEmpty() == true){

                System.out.println("all whitespace");
                return true;

            } else {
                System.out.println("all valid characters");
                return true;
            }
		}
	}
	
	public boolean employeeIdInputValidation(Connection connection, String employeeId){
		if(employeeId == null){
			return false;
		} else {
			
			String regex = "[^0-9]";
			Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(employeeId);
			if(matcher.find() == true){
				
				System.out.println("special character found.");
				return false;
				
			} else if(employeeId.trim().isEmpty() == true){
				
				System.out.println("all whitespace");
				return false;
				
			} else {
				System.out.println("all valid characters");
				int intEmpId = Integer.parseInt(employeeId);
				System.out.println(intEmpId);
				
				if(intEmpId < 100000 || intEmpId > 999999){
					return false;
				}
				
				String query = "SELECT * FROM employee WHERE employeeID = ?";
				try {
					PreparedStatement pstmt = connection.prepareStatement(query);
					pstmt.setInt(1, intEmpId);
					ResultSet rs = pstmt.executeQuery();
					
					while(rs.next()){
						System.out.println("what now?");
						return true;
					}
					
				} catch (SQLException e) {
					e.printStackTrace();
				}	
				
				return false;
			}
		}
	}
	
	public boolean departmentInputValidation(String text){
		if(text == null){
			System.out.println("null text");
			return false;
		} else {
			String regex = "[^A-Za-z0-9 ]";
			Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(text);
			
			if(matcher.find() == true){
				
				System.out.println("special character found.");
				return false;
				
			} else if(text.trim().isEmpty() == true){
				
				System.out.println("all whitespace");
				return false;
				
			} else if(text.length()>50){
				System.out.println("greater than destination length");
				return false;
			}else{
				System.out.println("all valid characters");
				return true;
			}
		}
	}
	
	public boolean imageInputValidation(long fileSize, String text){
		if(text == null){
			System.out.println("null text");
			return false;
		} else {
			
			
			if(text.trim().isEmpty() == true){
				
				System.out.println("all whitespace");
				return false;
				
			} else{
				if(fileSize > 0){
					if(text.equals("image/jpeg") || text.equals("image/png") || text.equals("image/bmp") || text.equals("image/gif"))
					{
						System.out.println("all valid characters");
						return true;
						}
					else
					{
						return false;
						}
				}else{
					return true;
				}
			}
		}
	}
	
	public boolean vehicleStringInputValidation(String text){
		if(text == null){
			System.out.println("null text");
			return false;
		} else {
			String regex = "[^A-Za-z ]";
			Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(text);
			
			if(matcher.find() == true){
				
				System.out.println("special character found.");
				return false;
				
			} else if(text.trim().isEmpty() == true){
				
				System.out.println("all whitespace");
				return false;
				
			} else if(text.length()>50){
				System.out.println("Text too long");
				return false;
			}else{
				System.out.println("all valid characters");
				return true;
			}
		}
	}
	
	public boolean vehicleYearManufacturedInputValidation(String text){
		if(text == null){
			System.out.println("null text");
			return false;
		} else {
			String regex = "[^0-9]";
			Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(text);
			
			if(matcher.find() == true){
				
				System.out.println("special character found.");
				return false;
				
			} else if(text.trim().isEmpty() == true){
				
				System.out.println("all whitespace");
				return false;
				
			} else if(text.length() < 4 || text.length() > 4){
				System.out.println("text too long");
				return false;
			}else{
				System.out.println("all valid characters");
				
				DateFormat dateFormat = new SimpleDateFormat("yyyy");
				Date year = new Date();
				Calendar calendar = Calendar.getInstance();
				calendar.set(2000, 1, 1);
				Date minimum = calendar.getTime();		
				
				try {
					Date input = dateFormat.parse(dateFormat.format(dateFormat.parse(text)));
					year = dateFormat.parse(dateFormat.format(year));
					System.out.println(year.toString());
					System.out.println(minimum.toString());
					System.out.println(input.toString());
					
					if(input.after(year) == true || input.before(minimum)){
						return false;
					} else {
						return true;
					}
					
				} catch (ParseException e) {
					System.out.println("error");
					e.printStackTrace();
				}
				return true;
			}
		}
	}
	
	public boolean vehiclePlateInputValidation(String text){
		text = text.toUpperCase();
		
		if(text == null){
			System.out.println("null text");
			return false;
		} else {
			String regex = "[^A-Z0-9]";
			Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(text);
			
			if(matcher.find() == true){
				
				System.out.println("special character found.");
				return false;
				
			} else if(text.trim().isEmpty() == true){
				
				System.out.println("all whitespace");
				return false;
				
			} else if(text.length() < 6 || text.length() > 7){
				System.out.println("text too long or too short");
				return false;
			}else{
				System.out.println("all valid characters");
				return true;
			}
		}
	}
	
	public boolean vehicleMaxCapacityInputValidation(String text){
		if(text == null){
			return false;
		} else {
			
			String regex = "[^0-9]";
			Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(text);
			if(matcher.find() == true){
				
				System.out.println("special character found.");
				return false;
				
			} else if(text.trim().isEmpty() == true){
				
				System.out.println("all whitespace");
				return false;
				
			} else {
				System.out.println("all valid characters");
				int capacity = Integer.parseInt(text);
				System.out.println(capacity);
				if(capacity < 5 || capacity > 25){
					return false;
				}
				System.out.println("what now?");
				return true;
			}

		}
	}
	
	public boolean vehicleImageInputValidation(long fileSize, String contentType){
		if(contentType == null){
			System.out.println("null text");
			return false;
		} else {
			
			if(contentType.trim().isEmpty() == true){
				
				System.out.println("all whitespace");
				return false;
				
			} else {
				if(fileSize > 0){
					if(contentType.equals("image/jpeg") || contentType.equals("image/png") || contentType.equals("image/bmp") || contentType.equals("image/gif")){
						System.out.println("all valid characters");
						return true;
					} else {
						return false;
					}
				} else {
					return true;
				}		
			}
		}
	}
	
	public boolean licenseInputValidation(String text){
		if(text == null){
			return false;
		} else {
			
			String regex = "[^A-Za-z0-9]";
			Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(text);
			if(matcher.find() == true){
				
				System.out.println("special character found.");
				return false;
				
			} else if(text.trim().isEmpty() == true){
				
				System.out.println("all whitespace");
				return false;
				
			} else if (text.length() < 11 || text.length() > 11){
				
				System.out.println("invalid number of characters");
				return false;
				
			} else {
				
				System.out.println("all valid characters");
				System.out.println("what now?");
				return true;
				
			}

		}
	}
	
	public boolean restrictionInputValidation(String text[]){
		if(text == null){
			return false;
		} else {
			for(int i = 0; i < text.length; i++){
				if(text[i] == null){
					System.out.println("restriction no." + (i + 1) + " was not picked");
				} else {
					String regex = "[^1-8]";
					Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
					Matcher matcher = pattern.matcher(text[i]);
					if(matcher.find() == true){
						
						System.out.println("special character found.");
						return false;
						
					} else if(text[i].trim().isEmpty() == true){
						
						System.out.println("all whitespace");
						return false;
						
					} else if (text[i].length() < 1 || text[i].length() > 1){
						
						System.out.println("invalid number of characters");
						return false;
						
					} else {
						System.out.println("all valid characters");
						System.out.println("moving to next input");
						
					}
				}
			}
			System.out.println("all good :)");
			return true;
		}
	}
	
	public boolean driverImageInputValidation(long fileSize, String contentType){
		if(contentType == null){
			System.out.println("null text");
			return false;
		} else {
			
			if(contentType.trim().isEmpty() == true){
				
				System.out.println("all whitespace");
				return false;
				
			} else {
				if(fileSize > 0){
					if(contentType.equals("image/jpeg") || contentType.equals("image/png") || contentType.equals("image/bmp") || contentType.equals("image/gif")){
						System.out.println("all valid characters");
						return true;
					} else {
						return false;
					}
				} else {
					return true;
				}		
			}
		}
	}
	
	public boolean passwordInputValidation(String password, String password2)
	{
		System.out.println("passValidation");
		String regex = "[^a-zA-Z0-9]";
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(password);
		if(password != null)
		{
			if(password.equals(password2))
			{
				if(password.length() < 7 || password.length() > 27)
				{
					return false;
					}
				else
				{
					if(matcher.find() == true)
					{
						
						System.out.println("special character found.");
						return false;
						} 
					else if(password.trim().isEmpty() == true)
					{
						
						System.out.println("all whitespace");
						return false;
						} 
					else
					{
						System.out.println("all valid characters");
						return true;
						}
					}
				}
			else
			{
				return false;
				}
			}
		else
		{
			return false;
			}
		}
	
	public boolean tokenInputValidation(Connection connection, String token)
	{
		try
		{
			String query ="SELECT * FROM forgot WHERE token = ?";
			String tokenChecker = null;
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setString(1, token);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				 tokenChecker = rs.getString("token");
				}
			
			if(tokenChecker.equals(token))
			{
				return true;
				}
			else
			{
				return false;
				}
			}
		catch(SQLException sqle)
		{
			System.out.println(sqle);
			}
		return false;
		}
	
	public boolean changePasswordInputValidation(Connection connection, String oldPassword, String newPassword, int employeeID){
		try{
			if(oldPassword.equals(newPassword)){
				System.out.println("not changing anything at all");
				return false;
			} else {
				String query = "SELECT * FROM employee WHERE employeeID = ?";
				PreparedStatement pstmt = connection.prepareStatement(query);
				pstmt.setInt(1, employeeID);
				ResultSet rs = pstmt.executeQuery();

				while(rs.next()){
					if(oldPassword.equals(rs.getString("pass"))){
						return true;
					} else {
						System.out.println("old password does not match");
						return false;
					}
				}
				
				System.out.println("no employee found with this ID: " + employeeID);
				return false;
					   
			}
		} catch (SQLException sqle){
			System.out.println(sqle);
			return false;
		}
	}
	
	public boolean checkDepartments(Connection connection, String key){
		if(key == null){
			return false;
		} else {
			
			String regex = "[^0-9]";
			Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(key);
			if(matcher.find() == true){
				
				System.out.println("special character found.");
				return false;
				
			} else if(key.trim().isEmpty() == true){
				
				System.out.println("all whitespace");
				return false;
				
			} else {
				try{
					
					int finalKey = Integer.parseInt(key);
					
					String query = "SELECT * FROM departments WHERE departmentID = ?";
					PreparedStatement pstmt = connection.prepareStatement(query);
					pstmt.setInt(1, finalKey);
					ResultSet departments = pstmt.executeQuery();
					
					while(departments.next()){
						System.out.println("all valid characters");
						System.out.println("what now?");	
						return true;
					}
					
					System.out.println("No departments found with this ID: " + key);
					return false;
					
				} catch (SQLException sqle){
					System.out.println(sqle);
					return false;
				}
	
			}

		}
	}
	
	public boolean checkDrivers(Connection connection, String key){
		if(key == null){
			return false;
		} else {
			
			String regex = "[^A-Za-z0-9]";
			Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(key);
			if(matcher.find() == true){
				
				System.out.println("special character found.");
				return false;
				
			} else if(key.trim().isEmpty() == true){
				
				System.out.println("all whitespace");
				return false;
				
			} else if(key.length() < 11 || key.length() > 11){
				
				System.out.println("too much characters");
				return false;
			} else {
			
				try{	
					String query = "SELECT * FROM driver WHERE licenseID = ?";
					PreparedStatement pstmt = connection.prepareStatement(query);
					pstmt.setString(1, key);
					ResultSet departments = pstmt.executeQuery();
					
					while(departments.next()){
						System.out.println("all valid characters");
						System.out.println("what now?");	
						return true;
					}
					
					System.out.println("No drivers found with this ID: " + key);
					return false;
					
				} catch (SQLException sqle){
					System.out.println(sqle);
					return false;
				}
	
			}

		}
	}
	
	public boolean checkVehicles(Connection connection, String key){
		if(key == null){
			return false;
		} else {
			
			String regex = "[^A-Za-z0-9]";
			Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(key);
			if(matcher.find() == true){
				
				System.out.println("special character found.");
				return false;
				
			} else if(key.trim().isEmpty() == true){
				
				System.out.println("all whitespace");
				return false;
				
			} else if(key.length() < 6 || key.length() > 7){
				
				System.out.println("too much characters");
				return false;
			} else {
			
				try{	
					String query = "SELECT * FROM cars WHERE plateNum = ?";
					PreparedStatement pstmt = connection.prepareStatement(query);
					pstmt.setString(1, key);
					ResultSet departments = pstmt.executeQuery();
					
					while(departments.next()){
						System.out.println("all valid characters");
						System.out.println("what now?");	
						return true;
					}
					
					System.out.println("No vehicles found with this plate number: " + key);
					return false;
					
				} catch (SQLException sqle){
					System.out.println(sqle);
					return false;
				}
	
			}

		}
	}
	
	public boolean requiredImageInputValidation(long fileSize, String contentType){
		if(contentType == null){
			System.out.println("null text");
			return false;
		} else {
			
			if(contentType.trim().isEmpty() == true){
				
				System.out.println("all whitespace");
				return false;
				
			} else {
				if(fileSize > 0){
					if(contentType.equals("image/jpeg") || contentType.equals("image/png") || contentType.equals("image/bmp") || contentType.equals("image/gif")){
						System.out.println("all valid characters");
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}		
			}
		}
	}

	public boolean checkReservations(Connection connection, String key){
		if(key == null){
			return false;
		} else {
			
			String regex = "[^0-9]";
			Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(key);
			if(matcher.find() == true){
				
				System.out.println("special character found.");
				return false;
				
			} else if(key.trim().isEmpty() == true){
				
				System.out.println("all whitespace");
				return false;
				
			} else {
				try{
					
					int finalKey = Integer.parseInt(key);
					
					String query = "SELECT * FROM reservations WHERE reservationID = ?";
					PreparedStatement pstmt = connection.prepareStatement(query);
					pstmt.setInt(1, finalKey);
					ResultSet reservations = pstmt.executeQuery();
					
					while(reservations.next()){
						System.out.println("all valid characters");
						System.out.println("what now?");	
						return true;
					}
					
					System.out.println("No reservations found with this ID: " + key);
					return false;
					
				} catch (SQLException sqle){
					System.out.println(sqle);
					return false;
				}
	
			}

		}
	}
}
	

