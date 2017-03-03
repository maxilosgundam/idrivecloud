package util;

public interface ResponseMessage {
	
	// Success messages
	String DRIVER_SUCCESS = "Driver successfully created";
	String VEHICLE_SUCCESS = "Vehicle successfully created";
	String SUCCESS_PASS_CHANGE = "Successful password change";
	String EMAIL_SUCCESS = "Email Sent";
	String DEPT_SUCCESS = "Department successfully created";
	String RESERVE_SUCCES = "Reservation has been successfully sent for approval";
	String DEPARTMENT_EDIT_SUCCESS = "Department was successfully updated";
	String DRIVER_EDIT_SUCCESS = "Driver was successfully updated";
	String VEHICLE_EDIT_SUCCESS = "Vehicle was successfully updated";
	String DEPARTMENT_DELETE_SUCCESS = "Department was successfully deleted";
	String DRIVER_DELETE_SUCCESS = "Driver was successfully deleted";
	String VEHICLE_DELETE_SUCCESS = "Vehicle was successfully deleted";
	String PENDING_APPROVE = "Reservation sent for finalization";
	String SUCCESS_DENY = "Reservation successfully denied";
	
	// Error messages
	String ENTER_USER_PASS = "Please enter a username or password";
	String INVALID_CAPTCHA = "CAPTCHA code was incorrect";
	String WRONG_USER_PASS= "Invalid username or password, try again";
	String ACCOUNT_DOES_NOT_EXIST = "Account does not exist";
	String DRIVER_EXISTS = "Driver already exists" ;
	String LICENSE_ASSIGNED = "License already assigned to a driver";
	String DOES_NOT_EXIST = "Employee does not exist";
	String IMAGE_ERROR = "Please upload an image file";
	String RESTRICTION_ERROR = "Choose the correct restriction";
	String INVALID_LICENSE = "Invalid license I.D.";
	String VEHICLE_EXISTS = "Vehicle already exists";
	String INVALID_CAP = "Capacity must be greater than 5 and not more than 25";
	String INVALID_PLATE = "Plate number must contain only 6-7 letters or numbers";
	String INVALID_COLOR = "Invalid color name";
	String INVALID_MODEL = "Enter a valid vehicle model";
	String INVALID_YEAR = "Invalid year, must contain only numbers";
	String INVALID_MANUFACTURER = "Enter a valid manufacturer name";
	String EXPIRED_REQUEST = "Your request has expired";
	String PASS_NOT_MATCH = "Invalid password combination";
	String ENTER_EMAIL = "Please enter your Email";
	String INVALID_EMAIL = "Invalid Email";
	String DEPT_EXISTS = "Department already exists";
	String EMP_DOES_NOT_EXIST = "Employee does not exist";
	String EMP_ID_INVALID = "Please enter correct details in the Employee I.D. field";
	String DEPT_INVALID = "Please enter correct details in the Department field";
	String PASS_INVALID = "Please enter a valid password";
	String DUPLICATE_RESERVATION = "There is already a similar reservation";
	String NO_DRIVER_VEHICLE_AVAILABLE = "There are no drivers and vehicles available for this date";
	String INVALID_PASSENGERS = "Please enter correct details in the Passengers field";
	String INVALID_NUM_PASSENGERS = "Invalid number of passengers";
	String INVALID_PURPOSE = "Please enter a valid purpose of travel";
	String INVALID_DESTINATION = "Invalid destination details";
	String INVALID_TIME = "Please enter a correct time";
	String INVALID_DATE = "Please enter a date 3 days before departure or within the next three months";
	String INVALID_DEPT_NAME = "Please enter a valid department name";
	String INVALID_DIVISION_HEAD = "Please enter a valid division head ID";
	String DEPT_NOT_FOUND= "Department does not exist";
	String DRIVER_NOT_FOUND = "Driver does not exist";
	String INVALID_VEHICLE_COLOR = "Invalid color for vehicle";
	String VEHICLE_NOT_FOUND = "Vehicle does not exist";
	String INVALID_OPTION = "Invalid option.";
	String INVALID_REASON = "Please input a valid reason for denial";
}
