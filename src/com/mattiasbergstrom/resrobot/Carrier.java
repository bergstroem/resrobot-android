package com.mattiasbergstrom.resrobot;

import org.json.JSONObject;

public class Carrier {
	private String name;
	private String url;
	private int id;
	private int number;
	
	public Carrier(JSONObject jsonObject) {
		id = jsonObject.optInt("id");
		name = jsonObject.optString("name");
		number = jsonObject.optInt("number");
		url = jsonObject.optString("url");
		
	}
	public String getName() {
		return name;
	}
	public String getUrl() {
		return url;
	}
	public int getId() {
		return id;
	}
	public int getNumber() {
		return number;
	}
}
