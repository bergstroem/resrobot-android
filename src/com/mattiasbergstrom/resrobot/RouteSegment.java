package com.mattiasbergstrom.resrobot;

import org.json.JSONException;
import org.json.JSONObject;

public class RouteSegment {
	private RouteSegmentId segmentId;
	private RouteSegmentEnd departure;
	private RouteSegmentEnd arrival;
	private String direction;
	
	public RouteSegment(JSONObject jsonObject) {
		try {
			segmentId = new RouteSegmentId(jsonObject.getJSONObject("segmentid"));
			departure = new RouteSegmentEnd(jsonObject.getJSONObject("departure"));
			arrival = new RouteSegmentEnd(jsonObject.getJSONObject("arrival"));
			direction = jsonObject.optString("direction");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	public RouteSegmentId getSegmentId() {
		return segmentId;
	}
	public RouteSegmentEnd getDeparture() {
		return departure;
	}
	public RouteSegmentEnd getArrival() {
		return arrival;
	}
	public String getDirection() {
		return direction;
	}
}
