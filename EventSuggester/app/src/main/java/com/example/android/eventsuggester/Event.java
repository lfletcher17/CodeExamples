package com.example.android.eventsuggester;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Event implements Comparable<Event>, Parcelable {

    private int eventID;
    private SongKickArtist headliner = null;
    private String eventName;
    private String venue;
    private Date date;
    private String uri;
    private ArrayList<SongKickArtist> performers;

    public Event (int eventId, String eventName, String venue, String date, String uri, ArrayList<SongKickArtist> performers) {
        this.eventID = eventId;
        for (int i = 0; i < performers.size(); i++) {
            if (performers.get(i).getBilling().equals("headline")) {
                this.headliner = performers.get(i);
            }
        }
        this.eventName = eventName;
        this.venue = venue;
        try {
            this.date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.uri = uri;
        this.performers = performers;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(eventID);
        out.writeParcelable(headliner, 0);
        out.writeString(eventName);
        out.writeString(venue);
        out.writeLong(date.getTime());
        out.writeString(uri);
        SongKickArtist[] performersArr = new SongKickArtist[performers.size()];
        performersArr = performers.toArray(performersArr);
        out.writeTypedArray(performersArr, 0);
    }

    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        public Event[] newArray(int size) {
            return new Event[size];
        }
    };


    private Event(Parcel in) {
        eventID = in.readInt();
        headliner = in.readParcelable(SongKickArtist.class.getClassLoader());
        eventName = in.readString();
        venue = in.readString();
        date = new Date(in.readLong());
        uri = in.readString();
        performers = in.createTypedArrayList(SongKickArtist.CREATOR);

    }


    @Override
    public String toString() {
        return this.eventName;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public SongKickArtist getHeadliner() {
        return headliner;
    }

    public void setHeadliner(SongKickArtist headliner) {
        this.headliner = headliner;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public ArrayList<SongKickArtist> getPerformers() {
        return performers;
    }

    public void setPerformers(ArrayList<SongKickArtist> performers) {
        this.performers = performers;
    }

    @Override
    public int compareTo(Event o) {
        return this.getDate().compareTo(o.getDate());
    }

    public String formattedDate () {
        DateFormat df = new SimpleDateFormat("E, MMM dd yyyy");
        return df.format(this.date);
    }

}
