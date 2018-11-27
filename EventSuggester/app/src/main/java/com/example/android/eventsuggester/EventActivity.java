package com.example.android.eventsuggester;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.eventsuggester.Database.AppDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import kaaes.spotify.webapi.android.models.Artist;


public class EventActivity extends AppCompatActivity implements EventAdapter.EventAdapterOnClickHandler {

    private static final int EVENT_DATA_LOADERID = 23;
    private static final String SEARCH_EVENT = "events";
    private String mlocationSkID;
    private String mSpotifyToken;
    private ProgressBar mLoadingIndicator;
    private TextView mEventErrorMessageDisplay;
    private SpotifyHandler mSpotifyHandler;
    private Spinner mEventSpinner;

    private static final String MIN_DATE = "min date";
    private static final String MAX_DATE = "max date";

    private RecyclerView mRecyclerView;
    private EventAdapter mEventAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Intent intent = getIntent();
        mlocationSkID = intent.getStringExtra("skID");
        mSpotifyToken = intent.getStringExtra("spotifyToken");
        mSpotifyHandler = new SpotifyHandler(mSpotifyToken);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_events);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mEventAdapter = new EventAdapter(this);
        mRecyclerView.setAdapter(mEventAdapter);


        mEventErrorMessageDisplay = (TextView) findViewById(R.id.events_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
        mLoadingIndicator.setVisibility(View.VISIBLE);

        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();
        max.add(Calendar.MONTH, 3);
        Bundle queryBundle = new Bundle();
        queryBundle.putLong(MIN_DATE, min.getTimeInMillis());
        queryBundle.putLong(MAX_DATE, max.getTimeInMillis());

        getSupportLoaderManager().initLoader(EVENT_DATA_LOADERID, queryBundle, loaderCallbacks);
    }

    public void loadMore () {
        Toast toast = Toast.makeText(this, "loadMore", Toast.LENGTH_LONG);
        toast.show();
    }


    private void showDataView() {
        mEventErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mEventErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Event selectedEvent) {
        Context context = EventActivity.this;
        Class destinationActivity = EventDetailActivity.class;
        Intent eventIntent = new Intent (context, destinationActivity);
        eventIntent.putExtra("spotifyToken", mSpotifyToken);
        eventIntent.putExtra("selectedEvent", selectedEvent);
        startActivity(eventIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings) {
            Intent startSettings = new Intent(this, SettingsActivity.class);
            startActivity(startSettings);
            return true;
        } else if (id == R.id.my_events) {
            Intent startMyEvents = new Intent(this, MyEventsActivity.class);
            startMyEvents.putExtra("spotifyToken", mSpotifyToken);
            startActivity(startMyEvents);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private LoaderManager.LoaderCallbacks<ArrayList<Event>> loaderCallbacks = new LoaderManager.LoaderCallbacks<ArrayList<Event>>() {
        @Override
        public Loader<ArrayList<Event>> onCreateLoader(int id, Bundle args) {
            Context context = getApplicationContext();
            Calendar min = Calendar.getInstance();
            min.setTimeInMillis(args.getLong(MIN_DATE));
            Calendar max = Calendar.getInstance();
            max.setTimeInMillis(args.getLong(MAX_DATE));

            Boolean includeRelatedArtists = true;

            return new EventLoader(context, mSpotifyHandler, mlocationSkID, min, max, includeRelatedArtists);
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<Event>> loader, ArrayList<Event> data) {
            Log.d("", "load finished!");
            if (data == null) {
                showErrorMessage();
            } else {
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                Collections.sort(data);
                mEventAdapter.setEventData(data);
                showDataView();
            }
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Event>> loader) {
            //
        }
    };



}
