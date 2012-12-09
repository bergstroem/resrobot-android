package com.mattiasbergstrom.resrobot;

import java.util.LinkedList;

import android.os.Parcel;
import android.os.Parcelable;

public class Route implements Parcelable {
	
	private LinkedList<RouteSegment> segments;

	public Route() {
		segments = new LinkedList<RouteSegment>();
	}
	
	public Route(Parcel in) {
		Parcelable[] routeSegments = in.readParcelableArray(RouteSegment.class.getClassLoader());
		this.segments = new LinkedList<RouteSegment>();
		for(int i = 0;i<routeSegments.length;i++){
			segments.add((RouteSegment) routeSegments[i]);
		}
	}

	public LinkedList<RouteSegment> getSegments() {
		return segments;
	}

	public void addSegment(RouteSegment segment) {
		segments.add(segment);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelableArray(segments.toArray(new RouteSegment[segments.size()]), flags);
	}

	public static final Parcelable.Creator<Route> CREATOR = new Parcelable.Creator<Route>() {
		public Route createFromParcel(Parcel in) {
			return new Route(in);
		}

		public Route[] newArray(int size) {
			return new Route[size];
		}
	};
	
}
