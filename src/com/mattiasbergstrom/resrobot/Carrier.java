package com.mattiasbergstrom.resrobot;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Carrier implements Parcelable {

	private String name;
	private String url;
	private int id;
	private int number;

	public Carrier(JSONObject jsonObject) {
		id = jsonObject.optInt("id");
		name = jsonObject.optString("name");
		number = jsonObject.optInt("number");
		url = jsonObject.optString("url");
	}

	public Carrier(Parcel in) {
		this.name = in.readString();
		this.url = in.readString();
		this.id = in.readInt();
		this.number = in.readInt();
	}

	public String getName() {
		return name;
	}

	public String getUrl() {
		return url;
	}

	public int getId() {
		return id;
	}

	public int getNumber() {
		return number;

	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(url);
		dest.writeInt(id);
		dest.writeInt(number);
	}

	public static final Parcelable.Creator<Carrier> CREATOR = new Parcelable.Creator<Carrier>() {
		public Carrier createFromParcel(Parcel in) {
			return new Carrier(in);
		}

		public Carrier[] newArray(int size) {
			return new Carrier[size];
		}
	};

}
