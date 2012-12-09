package com.mattiasbergstrom.resrobot;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Location implements Parcelable {
	
	private String name;
	private Municipality municipality;
	private int id;
	private double longitude; // x in API
	private double latitude; // y in API
	private LinkedList<TransportType> transportList;
	private LinkedList<Integer> producerList;
	private boolean isBestMatch;
	
	public Location(JSONObject jsonObject) {
		try {
			isBestMatch = jsonObject.optBoolean("bestmatch");
			longitude = jsonObject.optDouble("@x");
			latitude = jsonObject.optDouble("@y");
			id = jsonObject.optInt("@id");
			
			// Try getting display name first, otherwise name
			name = jsonObject.optString("displayname");
			
			if(name == "") 
				name = jsonObject.optString("name");
			
			municipality = jsonObject.has("municipality") ? new Municipality(jsonObject.getJSONObject("municipality")) : null;
			
			
			JSONArray transports = jsonObject.optJSONArray("transportlist");
			transportList = new LinkedList<TransportType>();
			if(transports != null) {
					for(int i = 0; i < transports.length(); i++) {
						JSONObject obj = transports.getJSONObject(i);
						TransportType type = new TransportType(obj);
						transportList.add(type);
				}
			}
			producerList = new LinkedList<Integer>();
			JSONArray producers = jsonObject.optJSONArray("producerlist");
			if(producers != null) {
				for(int i = 0; i < producers.length(); i++) {
					JSONObject obj = producers.getJSONObject(i);
					
					producerList.add(obj.getInt("@id"));
				}
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public Location(Parcel in) {
		this.name = in.readString();
		this.municipality = in.readParcelable(Municipality.class.getClassLoader());
		this.id = in.readInt();
		this.longitude = in.readDouble();
		this.latitude = in.readDouble();
		
		Parcelable[] transportTypes = in.readParcelableArray(TransportType.class.getClassLoader());
		this.transportList = new LinkedList<TransportType>();
		for(int i = 0 ; i<transportTypes.length ; i++){
			this.transportList.add((TransportType) transportTypes[i]);
		}
		
		int[] producers = new int[in.readInt()];//read number of producers
		in.readIntArray(producers);
		this.producerList = new LinkedList<Integer>();
		for(int i = 0 ; i<producers.length ; i++){
			producerList.add(producers[i]);
		}
		
		this.isBestMatch = in.readInt() > 0;
	}

	public String getName() {
		return name;
	}
	
	public Municipality getMunicipality() {
		return municipality;
	}
	
	public int getId() {
		return id;
	}
	
	public double getLongitude() {
		return longitude;
	}

	public double getLatitude() {
		return latitude;
	}
	
	public LinkedList<TransportType> getTransportList() {
		return transportList;
	}
	
	public LinkedList<Integer> getProducerList() {
		return producerList;
	}
	
	public boolean isBestMatch() {
		return isBestMatch;
	}

	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeParcelable(municipality, flags);
		dest.writeInt(id);
		dest.writeDouble(longitude);
		dest.writeDouble(latitude);
		dest.writeParcelableArray(transportList.toArray(new TransportType[transportList.size()]), flags);
		int[] producers = new int[producerList.size()];
		for(int i = 0;i<producerList.size();i++){
			producers[i] = producerList.get(i);
		}
		dest.writeInt(producerList.size());
		dest.writeIntArray(producers);
		dest.writeInt(isBestMatch ? 1 : 0);
	}

	public static final Parcelable.Creator<Location> CREATOR = new Parcelable.Creator<Location>() {
		public Location createFromParcel(Parcel in) {
			return new Location(in);
		}

		public Location[] newArray(int size) {
			return new Location[size];
		}
	};
	
}
