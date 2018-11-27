package com.example.android.eventsuggester;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.android.eventsuggester.Database.AppDatabase;
import com.example.android.eventsuggester.Database.EventEntity;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class MyEventLoader extends AsyncTaskLoader<ArrayList<Event>> {

    private ArrayList<Event> cachedData;
    private AppDatabase mDb;

    public MyEventLoader(Context context) {
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
    public ArrayList<Event> loadInBackground() {
        ArrayList<Event> result = new ArrayList<Event>();
        mDb = AppDatabase.getsInstance(getContext());
        List<EventEntity> myEvents = mDb.eventEntityDao().loadAllEvents();
        for (EventEntity e : myEvents) {
            Event event = null;
            try {
                event = SongKickUtils.getEventByID(String.valueOf(e.getEventId()));
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            result.add(event);
        }
        return result;
    }

    public void deliverResult (ArrayList<Event> data) {
        cachedData = data;
        super.deliverResult(data);
    }
}
