package com.example.android.eventsuggester;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public final class SongKickUtils {

    private static final String TAG = SongKickUtils.class.getSimpleName();
    private static final String SONGKICK_URL = "https://api.songkick.com/api/3.0";
    private static final String SONGKICK_API_KEY = "F51wCDEQKz2g8ZXz";

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static String removeSpaces (String input) {
        String[] splitted = input.split("\\s+");
        String artistFor = "";
        for (int i = 0; i < splitted.length; i++) {
            if(i > 0) {
                artistFor = artistFor + "+";
            }
            artistFor = artistFor + splitted[i];
        }
        return artistFor;
    }

    public static URL buildLocationUrl(String locationQuery) {
        try {
            locationQuery = URLEncoder.encode(locationQuery, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String urlString = SONGKICK_URL + "/search/locations.json?query=" + locationQuery +"&apikey=" + SONGKICK_API_KEY;
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildLongLatLocationUrl(String latitude, String longitude) {
        try {
            latitude = URLEncoder.encode(latitude, "UTF-8");
            longitude = URLEncoder.encode(longitude, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String urlString = SONGKICK_URL + "/search/locations.json?location=geo:" + latitude + "," + longitude +"&apikey=" + SONGKICK_API_KEY;

        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


    public static ArrayList<Location> getLocation(String locationQueryResult) throws JSONException {
        JSONObject rawJSON = new JSONObject(locationQueryResult);
        JSONObject resultsPage = rawJSON.getJSONObject("resultsPage");
        JSONObject results = resultsPage.getJSONObject("results");
        JSONArray locations = results.getJSONArray("location");

        ArrayList<Location> list = new ArrayList<Location>();

        for(int i=0; i<locations.length(); i++){
            JSONObject obj = locations.getJSONObject(i);
            JSONObject metro = obj.getJSONObject("metroArea");
            int metroID = metro.getInt("id");
            String city = metro.getString("displayName");
            JSONObject countryObj = metro.getJSONObject("country");
            String country = countryObj.getString("displayName");
            String lng = metro.getString("lng");
            String lat = metro.getString("lat");
            if (metro.has("state")) {
                JSONObject stateObj = metro.getJSONObject("state");
                String state = stateObj.getString("displayName");
                Location location = new Location(metroID, city, state, country, lng, lat);
                list.add(location);
            } else {
                Location location = new Location(metroID, city, country, lng, lat);
                list.add(location);
            }
        }
        return list;
    }


    public static URL buildEventSearchUrl(String metroID, String minDate, String maxDate, int pageNo) {
        Log.d("METRO", metroID);
        try {
            metroID = URLEncoder.encode(metroID, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String urlString = SONGKICK_URL + "/metro_areas/" + metroID + "/calendar.json?"  + "apikey=" + SONGKICK_API_KEY
                + "&min_date=" + minDate + "&max_date=" + maxDate + "&page=" + String.valueOf(pageNo);
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildEventSearchUrl(String metroID, String artist) {
        Log.d("METRO", metroID);
        try {
            metroID = URLEncoder.encode(metroID, "UTF-8");
            artist = URLEncoder.encode(artist, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String urlString = SONGKICK_URL + "/events.json?" + "location=sk:" + metroID + "&artist_name=" + removeSpaces(artist) +"&apikey=" + SONGKICK_API_KEY;
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildEventSearchUrl(String eventID) {
        Log.d("METRO", eventID);
        try {
            eventID = URLEncoder.encode(eventID, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String urlString = SONGKICK_URL + "/events/" +  eventID +".json?" +"apikey=" + SONGKICK_API_KEY;
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static Event getEventByID(String eventID) throws IOException, JSONException {
        Event result = null;
        String jsonString = getResponseFromHttpUrl(buildEventSearchUrl(eventID));
        JSONObject rawJSON = new JSONObject(jsonString);
        JSONObject resultsPage = rawJSON.getJSONObject("resultsPage");
        JSONObject results = resultsPage.getJSONObject("results");
        if (results.has("event")) {
            JSONObject event = results.getJSONObject("event");
            int id = event.getInt("id");
            String eventName = event.getString("displayName");
            JSONObject venue = event.getJSONObject("venue");
            String venueName = venue.getString("displayName");
            JSONObject start = event.getJSONObject("start");
            String date = start.getString("date");
            String uri = event.getString("uri");
            JSONArray performers = event.getJSONArray("performance");
            ArrayList<SongKickArtist> artists = new ArrayList<SongKickArtist>();
            for (int j = 0; j < performers.length(); j++) {
                JSONObject performer = performers.getJSONObject(j);
                JSONObject artist = performer.getJSONObject("artist");
                int artistId = artist.getInt("id");
                String artistName = artist.getString("displayName");
                String artistUri = artist.getString("uri");
                String billing = performer.getString("billing");
                SongKickArtist skArtist = new SongKickArtist(artistId, artistName, artistUri, billing);
                artists.add(skArtist);
            }
            result = new Event(id, eventName, venueName, date, uri, artists);
        }
        return result;

    }

    public static ArrayList<Event> getEventsForMetro (String metroID, String minDate, String maxDate) throws IOException, JSONException {
        Map<Integer, Event> map = new HashMap<Integer, Event>();
        int pageNo = 0;
        int count = 0;

        while (count % 50 == 0) {
            String jsonString = getResponseFromHttpUrl(buildEventSearchUrl(metroID, minDate, maxDate, pageNo));
            JSONObject rawJSON = new JSONObject(jsonString);
            JSONObject resultsPage = rawJSON.getJSONObject("resultsPage");
            JSONObject results = resultsPage.getJSONObject("results");

            if (results.has("event")) {
                JSONArray events = results.getJSONArray("event");

                for (int i = 0; i < events.length(); i++) {

                    JSONObject obj = events.getJSONObject(i);
                    int eventID = obj.getInt("id");
                    count++;

                    if (!map.containsKey(eventID)) {
                        String eventName = obj.getString("displayName");

                        JSONObject venue = obj.getJSONObject("venue");
                        String venueName = venue.getString("displayName");

                        JSONObject start = obj.getJSONObject("start");
                        String date = start.getString("date");

                        String uri = obj.getString("uri");

                        JSONArray performers = obj.getJSONArray("performance");

                        ArrayList<SongKickArtist> artists = new ArrayList<SongKickArtist>();

                        for (int j = 0; j < performers.length(); j++) {
                            JSONObject performer = performers.getJSONObject(j);
                            JSONObject artist = performer.getJSONObject("artist");
                            int artistId = artist.getInt("id");
                            String artistName = artist.getString("displayName");
                            String artistUri = artist.getString("uri");
                            String billing = performer.getString("billing");
                            SongKickArtist skArtist = new SongKickArtist(artistId, artistName, artistUri, billing);
                            artists.add(skArtist);
                        }
                        Event event = new Event(eventID, eventName, venueName, date, uri, artists);
                        Log.d("event listed", event.getEventName());
                        map.put(event.getEventID(), event);
                    }

                }
            } else {
                break;
            }
            pageNo++;

        }
        List<Event> list = new ArrayList<Event>(map.values());
        ArrayList<Event> events = new ArrayList<Event>(list);
        return events;
    }


    public static ArrayList<Event> getEvent(String eventQueryResult) throws JSONException {

        ArrayList<Event> list = new ArrayList<Event>();
        JSONObject rawJSON = new JSONObject(eventQueryResult);
        JSONObject resultsPage = rawJSON.getJSONObject("resultsPage");
        int numberOfResults = resultsPage.getInt("totalEntries");

        if (numberOfResults > 0) {

            JSONObject results = resultsPage.getJSONObject("results");
            JSONArray events = results.getJSONArray("event");

            for (int i = 0; i < events.length(); i++) {

                JSONObject obj = events.getJSONObject(i);
                int eventID = obj.getInt("id");
                String eventName = obj.getString("displayName");

                JSONObject venue = obj.getJSONObject("venue");
                String venueName = venue.getString("displayName");

                JSONObject start = obj.getJSONObject("start");
                String date = start.getString("date");

                String uri = obj.getString("uri");

                JSONArray performers = obj.getJSONArray("performance");

                ArrayList<SongKickArtist> artists = new ArrayList<SongKickArtist>();

                for (int j = 0; j < performers.length(); j++) {
                    JSONObject performer = performers.getJSONObject(j);
                    JSONObject artist = performer.getJSONObject("artist");
                    int artistId = artist.getInt("id");
                    String artistName = artist.getString("displayName");
                    String artistUri = artist.getString("uri");
                    String billing = performer.getString("billing");
                    SongKickArtist skArtist = new SongKickArtist(artistId, artistName, artistUri, billing);
                    artists.add(skArtist);
                }
                Event event = new Event(eventID, eventName, venueName, date, uri, artists);
                list.add(event);
            }
        }
        return list;
    }
}
