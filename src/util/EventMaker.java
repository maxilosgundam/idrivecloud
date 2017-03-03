package util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import model.EventModel;

public class EventMaker {
	
	public List<EventModel> getEvents(Connection connection){

		try{
			EventModel em = new EventModel();
			ResultSet rs = em.getEventReservations(connection);
			List<EventModel> events = new ArrayList<EventModel>();
	
			while(rs.next()){
				EventModel details = new EventModel();
				String date = rs.getDate("departure").toString();
				details.setEventDate(rs.getString("tripDate")+" "+date);
				details.setEventName(rs.getString("plateNum"));
				events.add(details);
			}
			return events;
		}catch(SQLException sqle){
			System.out.println(sqle);
			sqle.printStackTrace();
		}
		return null;
	}
	
	public String execute(Connection connection){
		String json = new Gson().toJson(getEvents(connection));
		return json;
	}
}
