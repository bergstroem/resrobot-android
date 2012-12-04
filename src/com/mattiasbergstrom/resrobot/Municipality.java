package com.mattiasbergstrom.resrobot;

import org.json.JSONObject;

public class Municipality {
	private int id;
	private int countyId;
	private String name;
	
	
	public Municipality(JSONObject jsonObject) {
		id = jsonObject.optInt("@id");
		countyId = jsonObject.optInt("@county_id");
		name = jsonObject.optString("#text");
	}


	public int getId() {
		return id;
	}


	public int getCountyId() {
		return countyId;
	}


	public String getName() {
		return name;
	}
	
}
