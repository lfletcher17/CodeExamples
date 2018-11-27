package com.example.android.eventsuggester;

import android.os.Parcel;
import android.os.Parcelable;

public class Location implements Parcelable {

    private int id;
    private String city;
    private String state;
    private String country;
    private String lng;
    private String lat;

    public Location (int id, String city, String country, String lng, String lat) {
        this.id = id;
        this.city = city;
        this.state = null;
        this.country = country;
        this.lng = lng;
        this.lat = lat;
    }

    public Location (int id, String city, String state, String country, String lng, String lat) {
        this.id = id;
        this.city = city;
        this.state = state;
        this.country = country;
        this.lng = lng;
        this.lat = lat;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeString(city);
        out.writeString(state);
        out.writeString(country);
        out.writeString(lng);
        out.writeString(lat);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Location> CREATOR = new Parcelable.Creator<Location>() {
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private Location(Parcel in) {
        id = in.readInt();
        city = in.readString();
        state = in.readString();
        country = in.readString();
        lng = in.readString();
        lat = in.readString();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        if (state != null) {
            return this.city + ", " + this.state + ", " + this.country;
        } else {
            return this.city + ", " + this.country;
        }
    }
}
