package com.mattiasbergstrom.resrobot;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class RouteSegment implements Parcelable {
	
	private RouteSegmentId segmentId;
	private RouteSegmentEnd departure;
	private RouteSegmentEnd arrival;
	private String direction;
	
	public RouteSegment(JSONObject jsonObject) {
		try {
			segmentId = new RouteSegmentId(jsonObject.getJSONObject("segmentid"));
			departure = new RouteSegmentEnd(jsonObject.getJSONObject("departure"));
			if(jsonObject.has("arrival")) {
				arrival = new RouteSegmentEnd(jsonObject.getJSONObject("arrival"));
			}
			direction = jsonObject.optString("direction");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	public RouteSegment(Parcel in) {
		this.segmentId = in.readParcelable(RouteSegmentId.class.getClassLoader());
		this.departure = in.readParcelable(RouteSegmentEnd.class.getClassLoader());
		this.arrival = in.readParcelable(RouteSegmentEnd.class.getClassLoader());
		this.direction = in.readString();
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

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(segmentId, flags);
		dest.writeParcelable(departure, flags);
		dest.writeParcelable(arrival, flags);
		dest.writeString(direction);
	}

	public static final Parcelable.Creator<RouteSegment> CREATOR = new Parcelable.Creator<RouteSegment>() {
		public RouteSegment createFromParcel(Parcel in) {
			return new RouteSegment(in);
		}

		public RouteSegment[] newArray(int size) {
			return new RouteSegment[size];
		}
	};
	
}
