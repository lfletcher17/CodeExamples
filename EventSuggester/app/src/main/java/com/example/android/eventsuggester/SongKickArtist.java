package com.example.android.eventsuggester;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import kaaes.spotify.webapi.android.models.Artist;

public class SongKickArtist implements Parcelable {

    private int id;
    private String name;
    private String uri;
    private String billing;

    public SongKickArtist (int id, String name, String uri, String billing) {
        this.id = id;
        this.name = name;
        this.uri = uri;
        this.billing = billing;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getBilling() {
        return billing;
    }

    public void setBilling(String billing) {
        this.billing = billing;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeString(name);
        out.writeString(uri);
        out.writeString(billing);
    }

    public static final Parcelable.Creator<SongKickArtist> CREATOR = new Parcelable.Creator<SongKickArtist>() {
        public SongKickArtist createFromParcel(Parcel in) {
            return new SongKickArtist(in);
        }

        public SongKickArtist[] newArray(int size) {
            return new SongKickArtist[size];
        }
    };


    private SongKickArtist(Parcel in) {
        id = in.readInt();
        name = in.readString();
        uri = in.readString();
        billing = in.readString();
    }

}
