package com.example.android.engagingtechtest;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class APIUtil {

    private static final String TAG = APIUtil.class.getSimpleName();
    private static final String URL = "http://private-bc5bb-engagingtech.apiary-mock.com/user";

    private static String getResponseFromHttpUrl(URL url) throws IOException {
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

    private static URL buildUrl() {

        URL url = null;
        try {
            url = new URL(URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static Profile getProfile() throws JSONException {
        String profileResponse = null;
        Profile result = null;

        URL url = buildUrl();
        try {
            profileResponse = getResponseFromHttpUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (profileResponse != null) {
            Log.d("printingProfResponse: ", profileResponse);
            JSONObject rawJSON = new JSONObject(profileResponse);

            String uuID = rawJSON.getString("uuid");
            Log.d("uuid", uuID);

            String name = rawJSON.getString("name");
            Log.d("name", name);

            String profileURL = rawJSON.getString("profile_url");
            Log.d("profileURL", profileURL);

            String userNumber = rawJSON.getString("user_number");
            Log.d("userNumber", String.valueOf(userNumber));

            ArrayList<TelephoneNumber> numbers = new ArrayList<TelephoneNumber>();
            JSONArray telNumbers = rawJSON.getJSONArray("telephone_numbers");

            for (int i = 0; i < telNumbers.length(); i++) {
                JSONObject obj = telNumbers.getJSONObject(i);
                String type = obj.getString("type");
                String telNumber = obj.getString("number");
                numbers.add(new TelephoneNumber(type, telNumber));
            }

            result = new Profile(uuID, name, profileURL, userNumber, numbers);

        }


        return result;
    }



}
