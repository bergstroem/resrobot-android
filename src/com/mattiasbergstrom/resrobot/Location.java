package com.mattiasbergstrom.resrobot;

import java.util.LinkedList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Location {
	private String name;
	private Municipality municipality;
	private int id;
	private double longitude; // x in API
	private double latitude; // y in API
	private LinkedList<TransportType> transportList;
	private LinkedList<Integer> producerList;
	private boolean isBestMatch;
	
	public Location(JSONObject jsonObject) {
		try {
			isBestMatch = jsonObject.optBoolean("bestmatch");
			longitude = jsonObject.optDouble("@x");
			latitude = jsonObject.optDouble("@y");
			id = jsonObject.optInt("@id");
			
			// Try getting display name first, otherwise name
			name = jsonObject.optString("displayname");
			
			if(name == "") 
				name = jsonObject.optString("name");
			
			municipality = jsonObject.has("municipality") ? new Municipality(jsonObject.getJSONObject("municipality")) : null;
			
			
			JSONArray transports = jsonObject.optJSONArray("transportlist");
			if(transports != null) {
					for(int i = 0; i < transports.length(); i++) {
						JSONObject obj = transports.getJSONObject(i);
						TransportType type = new TransportType(obj);
						transportList.add(type);
				}
			}
			JSONArray producers = jsonObject.optJSONArray("producerlist");
			if(producers != null) {
				for(int i = 0; i < producers.length(); i++) {
					JSONObject obj = producers.getJSONObject(i);
					
					producerList.add(obj.getInt("@id"));
				}
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public String getName() {
		return name;
	}
	
	public Municipality getMunicipality() {
		return municipality;
	}
	
	public int getId() {
		return id;
	}
	
	public double getLongitude() {
		return longitude;
	}

	public double getLatitude() {
		return latitude;
	}
	
	public LinkedList<TransportType> getTransportList() {
		return transportList;
	}
	
	public LinkedList<Integer> getProducerList() {
		return producerList;
	}
	
	public boolean isBestMatch() {
		return isBestMatch;
	}
}
