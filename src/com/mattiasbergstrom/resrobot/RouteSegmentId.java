package com.mattiasbergstrom.resrobot;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class RouteSegmentId implements Parcelable {
	
	private TransportType transportType;
	private Carrier carrier;
	private int distance;

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
	
	public RouteSegmentId(Parcel in) {
		this.transportType = in.readParcelable(TransportType.class.getClassLoader());
		this.carrier = in.readParcelable(Carrier.class.getClassLoader());
		this.distance = in.readInt();
	}
	
	public TransportType getTransportType() {
		return transportType;
	}

	public Carrier getCarrier() {
		return carrier;
	}

	public int getDistance() {
		return distance;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(transportType, flags);
		dest.writeParcelable(carrier, flags);
		dest.writeInt(distance);
	}

	public static final Parcelable.Creator<RouteSegmentId> CREATOR = new Parcelable.Creator<RouteSegmentId>() {
		public RouteSegmentId createFromParcel(Parcel in) {
			return new RouteSegmentId(in);
		}

		public RouteSegmentId[] newArray(int size) {
			return new RouteSegmentId[size];
		}
	};
	
}
