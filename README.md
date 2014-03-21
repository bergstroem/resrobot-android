Resrobot
========

Resrobot is an API-wrapper for Android, although it should be usable in a regular Java applications too. This wrapper doesn't contain all the available API-calls at this moment. Only Search, StationsInZone and FindLocation is implemented.

Documentation for the API (not the wrapper) can be found here: http://www.trafiklab.se/api/resrobot-sok-resa.


Usage
-----

First you must create an instance of the ResrobotClient. This is the object you use to make the API calls. 

```java
ResrobotClient client = new ResrobotClient("YOUR_API_KEY");
```

The constructor requires an API key which you can get from http://www.trafiklab.se/api/resrobot-sok-resa.

Example usage:
```java
ResrobotClient client = new ResrobotClient("YOUR_API_KEY");

client.search("7400001", "7400002", new Date(), false, new SearchCallback() {

	@Override
	public void searchComplete(ArrayList<Route> result) {
		//TODO: do something with the result.
	}
});
```
###Departures API (optional)
For some reason getting departures from the resrobot service has been separated into another API, this means a second API key is needed to get departures. You can get this API key from http://www.trafiklab.se/api/resrobot-stolptidtabeller. The departures API key should be provided as a second parameter to the ResrobotClient constructor.

```java
ResrobotClient client = new ResrobotClient("YOUR_API_KEY", "YOUR_DEPARTURES_API_KEY");
```

Methods
-------
All methods have overloads that take a an error callback as a last parameter.
```java
public void findLocation(String from, String to, final FindLocationCallback callback);
```
Can be used to find a location. The callback has two parameters, one ArrayList with location-matches for the 'from search-query' and one for the 'to search-query'. The semantic meaning of 'from' and 'to' can be ignored and simply used to search for two locations. 
```java
public void search(String fromId, String toId, Date date, boolean arrival, final SearchCallback callback);
```
Used to search for a route between two location-id's. Date is the date/time of the trip. Arrival should be true if you prefer to arrive at the chosen date or false if you want to depart at that date/time.
```java
public void search(String from, String to, double fromX, double fromY, double toX, double toY, Date date, boolean arrival, SearchCallback callback);
```
Also searches between to locations, but instead of id's this uses gps coordinates. The first two parameters are used as the name for the coordinates.
```java
public void stationsInZone(double centerX, double centerY, int radius, final StationsInZoneCallback callback);
```
Finds the stations within the radius of the specified coordinates.

```java
public void departures(int locationId, int timeSpan, final DeparturesCallback callback);
```
Finds departures from the specified locationId. (Needs a second API key, see Usage). 
