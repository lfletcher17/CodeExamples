package com.example.android.engagingtechtest;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import org.json.JSONException;

public class ProfileLoader extends AsyncTaskLoader<Profile> {

    private Profile cachedData;

    public ProfileLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if (cachedData == null) {
            forceLoad();
        } else {
            super.deliverResult(cachedData);
        }
    }

    @Override
    public Profile loadInBackground() {

        Profile result =  null;

        try {
            result = APIUtil.getProfile();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return result;
    }

    public void deliverResult (Profile data) {
        cachedData = data;
        super.deliverResult(data);
    }
}
