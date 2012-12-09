package com.mattiasbergstrom.resrobot;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class TransportType implements Parcelable {
	
	private String type;
	private String displayType;
    private String name;

	public TransportType(JSONObject jsonObject) {
		type = jsonObject.optString("@type");
		name = jsonObject.optString("#text");
		displayType = jsonObject.optString("@displaytype");
	}
	
	public TransportType(Parcel in) {
		this.type = in.readString();
		this.displayType = in.readString();
		this.name = in.readString();
	}
	
	public String getType() {
		return type;
	}
	
	public String getDisplayType() {
		return displayType;
	}
	
	public String getName() {
		return name;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(type);
		dest.writeString(displayType);
		dest.writeString(name);
	}

	public static final Parcelable.Creator<TransportType> CREATOR = new Parcelable.Creator<TransportType>() {
		public TransportType createFromParcel(Parcel in) {
			return new TransportType(in);
		}

		public TransportType[] newArray(int size) {
			return new TransportType[size];
		}
	};
	
}
