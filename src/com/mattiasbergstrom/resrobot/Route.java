package com.mattiasbergstrom.resrobot;

import java.util.LinkedList;

public class Route {
	private LinkedList<RouteSegment> segments;
	
	public Route(){
		segments = new LinkedList<RouteSegment>();
	}

	public LinkedList<RouteSegment> getSegments() {
		return segments;
	}

	public void addSegment(RouteSegment segment) {
		segments.add(segment);
	}
}
