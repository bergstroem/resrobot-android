package com.mattiasbergstrom.resrobot;


import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mattiasbergstrom.resrobot.DownloadTask.DownloadCompleteCallback;

public class ResrobotClient {
	
	
	public enum CoordSys {
		WGS84, RT90
	}
	
	private String key = "";
	private CoordSys coordSys = CoordSys.WGS84;
	private boolean isSuper = false;
	private String apiVersion = "2.1";
	
	public ResrobotClient(String key) {
		this.key = key;
	}
	
	/* Interfaces */
	public interface SearchCallback {
		public void searchComplete(ArrayList<Route> result);
	}
	
	public interface FindLocationCallback {
		public void findLocationComplete(ArrayList<Location> fromResult, ArrayList<Location> toResult);
	}
	
	public interface StationsInZoneCallback {
		public void stationsInZoneComplete(ArrayList<Location> result);
	}

	/* Private methods */
	
	private String getUrl(){
		if(isSuper)
			return "https://api.trafiklab.se/samtrafiken/resrobotsuper";
		else return "https://api.trafiklab.se/samtrafiken/resrobot";
	}
	
	/* Public methods */
	
	public void findLocation(String from, String to, final FindLocationCallback callback) {
		
		try {
			URL url = new URL(getUrl() +"/FindLocation.json?key=" + this.key + 
						"&from=" + from + "&to=" + to + 
						"&coordSys=" + coordSys.toString() + "&apiVersion=" + apiVersion);
			
			findLocation(url, callback);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void findLocation(URL url, final FindLocationCallback callback) {
		DownloadTask task = new DownloadTask();
		task.setDownloadCompleteCallback(new DownloadCompleteCallback() {
			
			@Override
			public void downloadComplete(String result) {
				ArrayList<Location> fromLocations = new ArrayList<Location>();
				ArrayList<Location> toLocations = new ArrayList<Location>();
				
				try {
					JSONObject jObject = (new JSONObject(result).getJSONObject("findlocationresult"));
					
					JSONArray fromLocationArr = jObject.optJSONObject("from").optJSONArray("location");
					JSONArray toLocationArr = jObject.optJSONObject("to").optJSONArray("location");
					
					for(int i = 0; i < fromLocationArr.length(); i++) {
						Location loc = new Location(fromLocationArr.getJSONObject(i));
						fromLocations.add(loc);
					}
					
					for(int i = 0; i < toLocationArr.length(); i++) {
						Location loc = new Location(toLocationArr.getJSONObject(i));
						toLocations.add(loc);
					}
					
					callback.findLocationComplete(fromLocations, toLocations);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		task.execute(url);
	}
	
	/**
	 * Searches for routes
	 * @param fromId the id to travel from
	 * @param toId the id to travel to
	 * @param date the date/time to search on
	 * @param arrival set to 'true' if the time specified is the wanted arrival time
	 * @param callback gets called when the search is done
	 */
	public void search(String fromId, String toId, Date date, boolean arrival, 
			final SearchCallback callback) {
		String urlString = getUrl() + "/Search.json?key=" + this.key + 
				"&fromId=" + fromId + "&toId=" + toId + "&arrival=" + arrival +
				"&coordSys=" + coordSys.toString();
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		urlString += "&date=" + df.format(date);
		df.applyPattern("HH:mm");
		urlString += "&time=" + df.format(date);
		
		urlString += "&apiVersion=" + apiVersion;
		try {
			search(new URL(urlString), callback);
						
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void search(String from, String to, double fromX, double fromY, 
			double toX, double toY, Date date, boolean arrival, 
			SearchCallback callback) {
		String urlString = getUrl() + "/Search.json?key=" + this.key + 
				"&from=" + from + "&to=" + to + "&fromX=" + fromX + "&fromY=" + fromY + 
				"&toX=" + toX + "&toY=" + toY + 
				"&arrival=" + arrival + "&coordSys=" + coordSys.toString();
		
		if(date != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
			urlString += "&date=" + df.format(date);
			df.applyPattern("HH:mm");
			urlString += "&time=" + df.format(date);
		}
		
		urlString += "&apiVersion=" + apiVersion;
		
		try {
			search(new URL(urlString), callback);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void search(URL url, final SearchCallback callback) {
		DownloadTask task = new DownloadTask();
		task.setDownloadCompleteCallback(new DownloadCompleteCallback() {
			
			@Override
			public void downloadComplete(String result) {
				ArrayList<Route> routes = new ArrayList<Route>();
				
				JSONObject jObject;
				try {
					jObject = new JSONObject(result);
					jObject = jObject.getJSONObject("timetableresult");
					
					JSONArray ttitems = jObject.getJSONArray("ttitem");
					
					for(int i = 0; i < ttitems.length(); i++) {
						Route route = new Route();
						// The API doesn't put the segment in an array if there's only one
						// segment within the ttitem. So a check must be made
						JSONArray arr = ttitems.getJSONObject(i).optJSONArray("segment");
						if(arr != null) {
							for(int j = 0 ; j < arr.length(); j++) {
								RouteSegment segment;
								segment = new RouteSegment(arr.getJSONObject(j));
								route.addSegment(segment);
							}
						}
						else {
							JSONObject obj = ttitems.getJSONObject(i).getJSONObject("segment");
							RouteSegment segment;
							segment = new RouteSegment(obj);
							route.addSegment(segment);
						}

						routes.add(route);
					}
					
				callback.searchComplete(routes);
				
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		task.execute(url);
	}
	
	public void stationsInZone(double centerX, double centerY, int radius, 
			final StationsInZoneCallback callback) {
		URL url;
		try {
			url = new URL(getUrl() + "/StationsInZone.json?key=" + this.key + 
					"&centerX=" + centerX + "&centerY=" + centerY + "&radius=" + radius +
					"&coordSys=" + coordSys.toString());
			DownloadTask task = new DownloadTask();
			task.setDownloadCompleteCallback(new DownloadCompleteCallback() {
				
				@Override
				public void downloadComplete(String result) {
					ArrayList<Location> locations = new ArrayList<Location>();
					JSONObject jObject;
					try {
						jObject = new JSONObject(result);
						jObject = jObject.getJSONObject("stationsinzoneresult");
						
						JSONArray arr = jObject.getJSONArray("location");
						if(arr != null) {
							for(int i = 0 ; i < arr.length(); i++) {
								Location loc = new Location(arr.getJSONObject(i));
								locations.add(loc);
							}
						}
						else {
							JSONObject obj = jObject.getJSONObject("location");
							Location loc = new Location(obj);
							locations.add(loc);
						}
						
						callback.stationsInZoneComplete(locations);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			
			task.execute(url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/* Getters and setters */
	
	public boolean isSuper() {
		return isSuper;
	}
	
	public void setSuper(boolean isSuper) {
		this.isSuper = isSuper;
	}

	public CoordSys getCoordSys() {
		return coordSys;
	}

	public void setCoordSys(CoordSys coordSys) {
		this.coordSys = coordSys;
	}
	
	public String getApiVersion() {
		return apiVersion;
	}

	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
