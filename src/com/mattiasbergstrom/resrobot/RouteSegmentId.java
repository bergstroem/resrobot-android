package com.mattiasbergstrom.resrobot;

import org.json.JSONException;
import org.json.JSONObject;

public class RouteSegmentId {
	private TransportType transportType;
	private Carrier carrier;
	private int distance;
	
	public TransportType getTransportType() {
		return transportType;
	}

	public Carrier getCarrier() {
		return carrier;
	}

	public int getDistance() {
		return distance;
	}

	public RouteSegmentId(JSONObject jsonObject) {
		try {
			distance = jsonObject.has("distance") ? jsonObject.getInt("distance") : 0;
			if(jsonObject.has("carrier"))
				carrier = new Carrier(jsonObject.getJSONObject("carrier"));
			transportType = new TransportType(jsonObject.getJSONObject("mot"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
}
