Resrobot
========

Resrobot is an API-wrapper for Android, although it should be usable in a regular Java applications too. This wrapper doesn't contain all the available API-calls at this moment. Only Search, StationsInZone and FindLocation is implemented.

Documentation for the API (not the wrapper) can be found here: http://www.trafiklab.se/api/resrobot-sok-resa


Usage
-----

First you must create an instance of the ResrobotClient. This is the object you use to make the API calls. 

ResrobotClient client = new ResrobotClient("YOUR_API_KEY");

The constructor requires an API key which you can get from http://www.trafiklab.se/api/resrobot-sok-resa

Example usage:

ResrobotClient client = new ResrobotClient("YOUR_API_KEY");

client.search("7400001", "7400002", new Date(), false, new SearchCallback() {

	@Override
	public void searchComplete(ArrayList<Route> result) {
		//TODO: do something with the result.
	}
});

Methods
-------

public void findLocation(String from, String to, final FindLocationCallback callback);

Can be used to find a location. The callback has two parameters, one ArrayList with location-matches for the 'from search-query' and one for the 'to search-query'. The semantic meaning of 'from' and 'to' can be ignored and simply used to search for two locations. 

public void search(String fromId, String toId, Date date, boolean arrival, final SearchCallback callback);

Used to search for a route between two location-id's. Date is the date/time of the trip. Arrival should be true if you prefer to arrive at the chosen date or false if you want to depart at that date/time.

public void search(String from, String to, double fromX, double fromY, double toX, double toY, Date date, boolean arrival, SearchCallback callback);

Also searches between to locations, but instead of id's this uses gps coordinates. The first two parameters are used as the name for the coordinates.

public void stationsInZone(double centerX, double centerY, int radius, final StationsInZoneCallback callback);

Finds the stations within the radius of the specified coordinates.

License
-------

    Copyright 2012 Mattias Bergström

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

