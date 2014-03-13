package com.mattiasbergstrom.resrobot;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.util.Log;

import com.mattiasbergstrom.resrobot.DownloadTask.DownloadCompleteCallback;

public class ResrobotClient {

	public enum CoordSys {
		WGS84, RT90
	}

	private String key = "";
	private String departuresKey = "";
	private CoordSys coordSys = CoordSys.WGS84;
	private boolean isSuper = false;
	private String apiVersion = "2.1";
	private SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd",
			Locale.getDefault());
	private SimpleDateFormat timeFormater = new SimpleDateFormat("HH:mm",
			Locale.getDefault());

	public ResrobotClient(String key) {
		this(key, "");
	}
	
	public ResrobotClient(String key, String departuresKey) {
		this.key = key;
		this.departuresKey = departuresKey;
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
	
	public interface DeparturesCallback {
		public void departuresComplete(ArrayList<RouteSegment> result);
	}

	/* Private methods */

	private String getUrl() {
		if (isSuper)
			return "https://api.trafiklab.se/samtrafiken/resrobotsuper";
		else
			return "https://api.trafiklab.se/samtrafiken/resrobot";
	}
	
	private String getDeparturesUrl() {
		if(isSuper) {
			return "https://api.trafiklab.se/samtrafiken/resrobotstops";
		} else {
			return "https://api.trafiklab.se/samtrafiken/resrobotstopssuper";
		}
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
			URL url;
			url = new URL(getUrl() + "/FindLocation.json?key=" + this.key
						+ "&from=" + URLEncoder.encode(from, "UTF-8") + "&to=" + URLEncoder.encode(to, "UTF-8") + "&coordSys="
						+ coordSys.toString() + "&apiVersion=" + apiVersion);

			findLocation(url, callback, errorCallback);
		} catch (MalformedURLException e) {
			if (errorCallback != null) {
				errorCallback.errorOccurred();
			}
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			if (errorCallback != null) {
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
		final Handler handler = new Handler();
		task.setDownloadCompleteCallback(new DownloadCompleteCallback() {

			@Override
			public void downloadComplete(String result) {

				if (result == null) {
					if (errorCallback != null) {
						handler.post(new Runnable() {

							@Override
							public void run() {
								errorCallback.errorOccurred();
							}
						});
					}
					return;
				}

				final ArrayList<Location> fromLocations = new ArrayList<Location>();
				final ArrayList<Location> toLocations = new ArrayList<Location>();

				try {
					JSONObject jObject = (new JSONObject(result)
							.getJSONObject("findlocationresult"));

					if (jObject.has("from")) {

						JSONArray fromLocationArr = jObject.optJSONObject(
								"from").optJSONArray("location");

						if(fromLocationArr != null) {
							for (int i = 0; i < fromLocationArr.length(); i++) {
								Location loc = new Location(fromLocationArr
										.getJSONObject(i));
								fromLocations.add(loc);
							}
						}
					}

					if (jObject.has("to")) {

						JSONArray toLocationArr = jObject.optJSONObject("to")
								.optJSONArray("location");

						if(toLocationArr != null) {
							for (int i = 0; i < toLocationArr.length(); i++) {
								Location loc = new Location(toLocationArr
										.getJSONObject(i));
								toLocations.add(loc);
							}
						}
					}

					handler.post(new Runnable() {

						@Override
						public void run() {
							callback.findLocationComplete(fromLocations,
									toLocations);
						}
					});
				} catch (JSONException e) {
					if (errorCallback != null) {
						handler.post(new Runnable() {

							@Override
							public void run() {
								errorCallback.errorOccurred();
							}
						});
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
			if (errorCallback != null) {
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
			if (errorCallback != null) {
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
		final Handler handler = new Handler();
		task.setDownloadCompleteCallback(new DownloadCompleteCallback() {

			@Override
			public void downloadComplete(String result) {

				if (result == null) {
					if (errorCallback != null) {
						handler.post(new Runnable() {
							
							@Override
							public void run() {
								errorCallback.errorOccurred();
							}
						});
					}
					return;
				}

				final ArrayList<Route> routes = new ArrayList<Route>();

				JSONObject jObject;
				try {
					jObject = new JSONObject(result);
					jObject = jObject.getJSONObject("timetableresult");

					if (!jObject.has("ttitem")) { // return empty result list
													// instead of crashing
						handler.post(new Runnable() {
							
							@Override
							public void run() {
								callback.searchComplete(routes);
							}
						});
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

					handler.post(new Runnable() {
						
						@Override
						public void run() {
							callback.searchComplete(routes);
						}
					});

				} catch (JSONException e) {
					if (errorCallback != null) {
						handler.post(new Runnable() {
							
							@Override
							public void run() {
								errorCallback.errorOccurred();
							}
						});
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
			final Handler handler = new Handler();
			task.setDownloadCompleteCallback(new DownloadCompleteCallback() {

				@Override
				public void downloadComplete(String result) {

					if (result == null) {
						if (errorCallback != null) {
							handler.post(new Runnable() {
								
								@Override
								public void run() {
									errorCallback.errorOccurred();
								}
							});
						}
						return;
					}

					final ArrayList<Location> locations = new ArrayList<Location>();
					JSONObject jObject;
					try {
						jObject = new JSONObject(result);
						jObject = jObject.getJSONObject("stationsinzoneresult");

						if (!jObject.has("location")) { // return empty result
														// list instead of
														// crashing
							handler.post(new Runnable() {
								
								@Override
								public void run() {
									callback.stationsInZoneComplete(locations);
								}
							});
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

						handler.post(new Runnable() {
							
							@Override
							public void run() {
								callback.stationsInZoneComplete(locations);
							}
						});
					} catch (JSONException e) {
						if (errorCallback != null) {

							handler.post(new Runnable() {
								
								@Override
								public void run() {
									errorCallback.errorOccurred();
								}
							});
						}
						e.printStackTrace();
					}
				}
			});

			task.execute(url);
		} catch (MalformedURLException e) {
			if (errorCallback != null) {
				errorCallback.errorOccurred();
			}
			e.printStackTrace();
		}
	}
	
	public void departures(int locationId, int timeSpan,
			final DeparturesCallback callback) {
		departures(locationId, timeSpan, callback, null);
	}
	
	public void departures(int locationId, int timeSpan,
			final DeparturesCallback callback,
			final ErrorCallback errorCallback) {
		URL url;
		try {
			url = new URL(getDeparturesUrl() + "/GetDepartures.json?key=" + this.departuresKey
					+ "&locationId=" + String.valueOf(locationId) + "&apiVersion=2.2"
					+ "&timeSpan=" + timeSpan + "&coordSys=" + coordSys.toString());
			
			DownloadTask task = new DownloadTask();
			final Handler handler = new Handler();
			
			task.setDownloadCompleteCallback(new DownloadCompleteCallback() {

				@Override
				public void downloadComplete(String result) {

					if (result == null) {
						if (errorCallback != null) {
							handler.post(new Runnable() {
								
								@Override
								public void run() {
									errorCallback.errorOccurred();
								}
							});
						}
						return;
					}

					final ArrayList<RouteSegment> departures = new ArrayList<RouteSegment>();
					JSONObject jObject;
					try {
						jObject = new JSONObject(result);
						jObject = jObject.getJSONObject("getdeparturesresult");

						if (!jObject.has("departuresegment")) { // return empty result
														// list instead of
														// crashing
							handler.post(new Runnable() {
								
								@Override
								public void run() {
									callback.departuresComplete(departures);
								}
							});
							return;
						}

						
						JSONArray arr = jObject.getJSONArray("departuresegment");
						if (arr != null) {
							for (int i = 0; i < arr.length(); i++) {
								RouteSegment departure = new RouteSegment(arr
										.getJSONObject(i));
								departures.add(departure);
							}
						}

						handler.post(new Runnable() {
							@Override
							public void run() {
								callback.departuresComplete(departures);
							}
						});
					} catch (JSONException e) {
						if (errorCallback != null) {

							handler.post(new Runnable() {
								
								@Override
								public void run() {
									errorCallback.errorOccurred();
								}
							});
						}
						e.printStackTrace();
					}
				}
			});
			task.execute(url);
		} catch (MalformedURLException e) {
			if (errorCallback != null) {
				errorCallback.errorOccurred();
			}
			e.printStackTrace();
		}
	}

	/* Getters and setters */

	public String getDeparturesKey() {
		return departuresKey;
	}

	public void setDeparturesKey(String departuresKey) {
		this.departuresKey = departuresKey;
	}

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
