package com.mattiasbergstrom.resrobot;

import org.json.JSONObject;

public class TransportType {
	private String type;
	private String displayType;
    private String name;
	
	public String getType() {
		return type;
	}
	
	public String getDisplayType() {
		return displayType;
	}
	
	public String getName() {
		return name;
	}

	public TransportType(JSONObject jsonObject) {
		type = jsonObject.optString("@type");
		name = jsonObject.optString("#text");
		displayType = jsonObject.optString("@displaytype");
	}
}
