package com.mattiasbergstrom.resrobot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

public class RouteSegmentEnd {
	private Location location;
	private Date dateTime;
	
	public Location getLocation() {
		return location;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public RouteSegmentEnd(JSONObject jsonObject) {
		try {
			location  = new Location(jsonObject.getJSONObject("location"));
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
			dateTime = df.parse(jsonObject.getString("datetime"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
