package com.mattiasbergstrom.resrobot;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Municipality implements Parcelable {
	
	private int id;
	private int countyId;
	private String name;
	
	public Municipality(JSONObject jsonObject) {
		id = jsonObject.optInt("@id");
		countyId = jsonObject.optInt("@county_id");
		name = jsonObject.optString("#text");
	}
	
	public Municipality(Parcel in) {
		this.id = in.readInt();
		this.countyId = in.readInt();
		this.name = in.readString();
	}

	public int getId() {
		return id;
	}

	public int getCountyId() {
		return countyId;
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
		dest.writeInt(id);
		dest.writeInt(countyId);
		dest.writeString(name);
	}

	public static final Parcelable.Creator<Municipality> CREATOR = new Parcelable.Creator<Municipality>() {
		public Municipality createFromParcel(Parcel in) {
			return new Municipality(in);
		}

		public Municipality[] newArray(int size) {
			return new Municipality[size];
		}
	};
	
}
