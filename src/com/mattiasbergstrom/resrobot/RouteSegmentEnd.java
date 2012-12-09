package com.mattiasbergstrom.resrobot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class RouteSegmentEnd implements Parcelable {
	
	private Location location;
	private Date dateTime;

	public RouteSegmentEnd(JSONObject jsonObject) {
		try {
			location = new Location(jsonObject.getJSONObject("location"));
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm",
					Locale.getDefault());
			dateTime = df.parse(jsonObject.getString("datetime"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public RouteSegmentEnd(Parcel in) {
		this.location = in.readParcelable(Location.class.getClassLoader());
		this.dateTime = new Date(in.readLong());
	}

	public Location getLocation() {
		return location;
	}

	public Date getDateTime() {
		return dateTime;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(location, flags);
		dest.writeLong(dateTime.getTime());
	}

	public static final Parcelable.Creator<RouteSegmentEnd> CREATOR = new Parcelable.Creator<RouteSegmentEnd>() {
		public RouteSegmentEnd createFromParcel(Parcel in) {
			return new RouteSegmentEnd(in);
		}

		public RouteSegmentEnd[] newArray(int size) {
			return new RouteSegmentEnd[size];
		}
	};

}
