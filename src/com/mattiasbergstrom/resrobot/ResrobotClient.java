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
	private SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd",
			Locale.getDefault());
	private SimpleDateFormat timeFormater = new SimpleDateFormat("HH:mm",
			Locale.getDefault());

	public ResrobotClient(String key) {
		this.key = key;
	}

	/* Interfaces */
	public interface SearchCallback {
		public void searchComplete(ArrayList<Route> result);
	}

	public interface FindLocationCallback {
		public void findLocationComplete(ArrayList<Location> fromResult,
				ArrayList<Location> toResult);
	}

	public interface StationsInZoneCallback {
		public void stationsInZoneComplete(ArrayList<Location> result);
	}

	public interface ErrorCallback {
		public void errorOccurred();
	}

	/* Private methods */

	private String getUrl() {
		if (isSuper)
			return "https://api.trafiklab.se/samtrafiken/resrobotsuper";
		else
			return "https://api.trafiklab.se/samtrafiken/resrobot";
	}

	/* Public methods */

	public void findLocation(String from, String to,
			final FindLocationCallback callback) {
		findLocation(from, to, callback, null);
	}

	public void findLocation(String from, String to,
			final FindLocationCallback callback,
			final ErrorCallback errorCallback) {

		try {
			URL url = new URL(getUrl() + "/FindLocation.json?key=" + this.key
					+ "&from=" + from + "&to=" + to + "&coordSys="
					+ coordSys.toString() + "&apiVersion=" + apiVersion);

			findLocation(url, callback, errorCallback);
		} catch (MalformedURLException e) {
			if(errorCallback!=null){
				errorCallback.errorOccurred();
			}
			e.printStackTrace();
		}
	}

	public void findLocation(URL url, final FindLocationCallback callback) {
		findLocation(url, callback, null);
	}

	public void findLocation(URL url, final FindLocationCallback callback,
			final ErrorCallback errorCallback) {
		DownloadTask task = new DownloadTask();
		task.setDownloadCompleteCallback(new DownloadCompleteCallback() {

			@Override
			public void downloadComplete(String result) {
				
				if(result == null){
					if(errorCallback!=null){
						errorCallback.errorOccurred();
					}
					return;
				}
				
				ArrayList<Location> fromLocations = new ArrayList<Location>();
				ArrayList<Location> toLocations = new ArrayList<Location>();

				try {
					JSONObject jObject = (new JSONObject(result)
							.getJSONObject("findlocationresult"));
					
					if(jObject.has("from")){ 

						JSONArray fromLocationArr = jObject.optJSONObject("from")
								.optJSONArray("location");

						for (int i = 0; i < fromLocationArr.length(); i++) {
							Location loc = new Location(fromLocationArr
									.getJSONObject(i));
							fromLocations.add(loc);
						}
					}
					
					if(jObject.has("to")){ 
						
						JSONArray toLocationArr = jObject.optJSONObject("to")
								.optJSONArray("location");

						for (int i = 0; i < toLocationArr.length(); i++) {
							Location loc = new Location(toLocationArr
									.getJSONObject(i));
							toLocations.add(loc);
						}
					}

					callback.findLocationComplete(fromLocations, toLocations);
				} catch (JSONException e) {
					if(errorCallback!=null){
						errorCallback.errorOccurred();
					}
					e.printStackTrace();
				}
			}
		});

		task.execute(url);
	}

	public void search(String fromId, String toId, Date date, boolean arrival,
			final SearchCallback callback) {
		search(fromId, toId, date, arrival, callback, null);
	}

	/**
	 * Searches for routes
	 * 
	 * @param fromId
	 *            the id to travel from
	 * @param toId
	 *            the id to travel to
	 * @param date
	 *            the date/time to search on
	 * @param arrival
	 *            set to 'true' if the time specified is the wanted arrival time
	 * @param callback
	 *            gets called when the search is done
	 */
	public void search(String fromId, String toId, Date date, boolean arrival,
			final SearchCallback callback, final ErrorCallback errorCallback) {
		String urlString = getUrl() + "/Search.json?key=" + this.key
				+ "&fromId=" + fromId + "&toId=" + toId + "&arrival=" + arrival
				+ "&coordSys=" + coordSys.toString();

		urlString += "&date=" + dateFormater.format(date);
		urlString += "&time=" + timeFormater.format(date);

		urlString += "&apiVersion=" + apiVersion;
		try {
			search(new URL(urlString), callback, errorCallback);

		} catch (MalformedURLException e) {
			if(errorCallback!=null){
				errorCallback.errorOccurred();
			}
			e.printStackTrace();
		}
	}

	public void search(String from, String to, double fromX, double fromY,
			double toX, double toY, Date date, boolean arrival,
			SearchCallback callback) {
		search(from, to, fromX, fromY, toX, toY, date, arrival, callback, null);
	}

	public void search(String from, String to, double fromX, double fromY,
			double toX, double toY, Date date, boolean arrival,
			SearchCallback callback, final ErrorCallback errorCallback) {
		String urlString = getUrl() + "/Search.json?key=" + this.key + "&from="
				+ from + "&to=" + to + "&fromX=" + fromX + "&fromY=" + fromY
				+ "&toX=" + toX + "&toY=" + toY + "&arrival=" + arrival
				+ "&coordSys=" + coordSys.toString();

		if (date != null) {
			urlString += "&date=" + dateFormater.format(date);
			urlString += "&time=" + timeFormater.format(date);
		}

		urlString += "&apiVersion=" + apiVersion;

		try {
			search(new URL(urlString), callback, errorCallback);
		} catch (MalformedURLException e) {
			if(errorCallback!=null){
				errorCallback.errorOccurred();
			}
			e.printStackTrace();
		}
	}

	public void search(URL url, final SearchCallback callback) {
		search(url, callback, null);
	}

	public void search(URL url, final SearchCallback callback,
			final ErrorCallback errorCallback) {
		DownloadTask task = new DownloadTask();
		task.setDownloadCompleteCallback(new DownloadCompleteCallback() {

			@Override
			public void downloadComplete(String result) {
				
				if(result == null){
					if(errorCallback!=null){
						errorCallback.errorOccurred();
					}
					return;
				}
				
				ArrayList<Route> routes = new ArrayList<Route>();

				JSONObject jObject;
				try {
					jObject = new JSONObject(result);
					jObject = jObject.getJSONObject("timetableresult");
					
					if(!jObject.has("ttitem")){ //return empty result list instead of crashing
						callback.searchComplete(routes);
						return;
					}

					JSONArray ttitems = jObject.getJSONArray("ttitem");

					for (int i = 0; i < ttitems.length(); i++) {
						Route route = new Route();
						// The API doesn't put the segment in an array if
						// there's only one
						// segment within the ttitem. So a check must be made
						JSONArray arr = ttitems.getJSONObject(i).optJSONArray(
								"segment");
						if (arr != null) {
							for (int j = 0; j < arr.length(); j++) {
								RouteSegment segment;
								segment = new RouteSegment(arr.getJSONObject(j));
								route.addSegment(segment);
							}
						} else {
							JSONObject obj = ttitems.getJSONObject(i)
									.getJSONObject("segment");
							RouteSegment segment;
							segment = new RouteSegment(obj);
							route.addSegment(segment);
						}

						routes.add(route);
					}

					callback.searchComplete(routes);

				} catch (JSONException e) {
					if(errorCallback!=null){
						errorCallback.errorOccurred();
					}
					e.printStackTrace();
				}
			}
		});

		task.execute(url);
	}

	public void stationsInZone(double centerX, double centerY, int radius,
			final StationsInZoneCallback callback) {
		stationsInZone(centerX, centerY, radius, callback, null);
	}

	public void stationsInZone(double centerX, double centerY, int radius,
			final StationsInZoneCallback callback,
			final ErrorCallback errorCallback) {
		URL url;
		try {
			url = new URL(getUrl() + "/StationsInZone.json?key=" + this.key
					+ "&centerX=" + centerX + "&centerY=" + centerY
					+ "&radius=" + radius + "&coordSys=" + coordSys.toString());
			DownloadTask task = new DownloadTask();
			task.setDownloadCompleteCallback(new DownloadCompleteCallback() {

				@Override
				public void downloadComplete(String result) {
					
					if(result == null){
						if(errorCallback!=null){
							errorCallback.errorOccurred();
						}
						return;
					}
					
					ArrayList<Location> locations = new ArrayList<Location>();
					JSONObject jObject;
					try {
						jObject = new JSONObject(result);
						jObject = jObject.getJSONObject("stationsinzoneresult");
						
						if(!jObject.has("location")){ //return empty result list instead of crashing
							callback.stationsInZoneComplete(locations);
							return;
						}

						JSONArray arr = jObject.getJSONArray("location");
						if (arr != null) {
							for (int i = 0; i < arr.length(); i++) {
								Location loc = new Location(arr
										.getJSONObject(i));
								locations.add(loc);
							}
						} else {
							JSONObject obj = jObject.getJSONObject("location");
							Location loc = new Location(obj);
							locations.add(loc);
						}

						callback.stationsInZoneComplete(locations);
					} catch (JSONException e) {
						if(errorCallback!=null){
							errorCallback.errorOccurred();
						}
						e.printStackTrace();
					}
				}
			});

			task.execute(url);
		} catch (MalformedURLException e) {
			if(errorCallback!=null){
				errorCallback.errorOccurred();
			}
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
