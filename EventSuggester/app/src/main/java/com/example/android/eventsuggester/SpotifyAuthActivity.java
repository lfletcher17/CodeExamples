package com.example.android.eventsuggester;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.eventsuggester.Database.AppDatabase;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpotifyAuthActivity extends AppCompatActivity implements ConnectionStateCallback {

    private static final String SPOTIFY_CLIENT_ID = "9c7db37d947d41519f7148ad5076f76a";
    private static final String SPOTIFY_REDIRECT_URI = "proto-login://callback";
    private static final int SPOTIFY_REQUEST_CODE = 2018;
    private static final int LOCATION_REQUEST_CODE = 20;
    private static final int LOCATION_LOADERID = 21;
    private String mSpotifyToken;
    private Button mCurrentLocation;
    private Button mManualLocation;
    private Boolean mChangeLocation = false;

    private TextView mLocationError;
    private TextView mDescription;

    private Location mAuthorisedAndroidLocation;
    private LocationManager mLocationManager;
    private FusedLocationProviderClient mFusedLocationClient;
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify_auth);

        Intent intent = getIntent();
        mChangeLocation = intent.getBooleanExtra("change_location", false);

        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(SPOTIFY_CLIENT_ID, AuthenticationResponse.Type.TOKEN, SPOTIFY_REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming", "user-follow-read", "user-top-read"});
        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity(this, SPOTIFY_REQUEST_CODE, request);

    }

    public void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION },
                    LOCATION_REQUEST_CODE );
        } else {
            checkLocationProviders();
            if (mAuthorisedAndroidLocation == null) {
                mCurrentLocation.setAlpha(.5f);
                mCurrentLocation.setClickable(false);
                mLocationError.setVisibility(View.VISIBLE);
            } else {
                getLocation();
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        checkLocationProviders();
        if (mAuthorisedAndroidLocation == null) {
            mCurrentLocation.setAlpha(.5f);
            mCurrentLocation.setClickable(false);
            mLocationError.setVisibility(View.VISIBLE);
        } else {
            getLocation();
        }
    }

    private void checkLocationProviders () {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location location = null;

        for (String provider : providers) {
            try {
                location = mLocationManager.getLastKnownLocation(provider);
            } catch (SecurityException e) {
                e.printStackTrace();
                //WILL NEVER GET CALLED, THIS METHOD ONLY CALLED IF PERMISSION WAS ALREADY GRANTED
            }
            if (mAuthorisedAndroidLocation == null) {
                mAuthorisedAndroidLocation = location;
            } else if (location != null){
                if (mAuthorisedAndroidLocation.getAccuracy() < location.getAccuracy()) {
                    mAuthorisedAndroidLocation = location;
                }
            }
        }
    }

    private void getLocation () {
        double latitude = mAuthorisedAndroidLocation.getLatitude();
        double longitude = mAuthorisedAndroidLocation.getLongitude();
        URL url = SongKickUtils.buildLongLatLocationUrl(String.valueOf(latitude), String.valueOf(longitude));
        new currentLocationTask().execute(url);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == SPOTIFY_REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                //FINE, THE APP CAN CONTINUE BUT WE NEED TO STORE THIS ACCESS TOKEN SOMEWHERE
                mSpotifyToken = response.getAccessToken();

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SpotifyAuthActivity.this);

                if (!sharedPreferences.getString("location_id", "null").equals("null") && mChangeLocation == false) {
                    Context context = SpotifyAuthActivity.this;
                    Class destinationActivity = EventActivity.class;
                    Intent eventIntent = new Intent (context, destinationActivity);
                    eventIntent.putExtra("skID", String.valueOf(sharedPreferences.getString("location_id", "null")));
                    eventIntent.putExtra("spotifyToken", mSpotifyToken);
                    startActivity(eventIntent);
                }

                mLocationError = (TextView) findViewById(R.id.location_error_message);
                mDescription = (TextView) findViewById(R.id.location_desc);
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
                mCurrentLocation = (Button) findViewById(R.id.btn_current_location);
                mCurrentLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        checkLocationPermission();
                    }
                });
                mManualLocation = (Button) findViewById(R.id.btn_manual_location);
                mManualLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Context context = SpotifyAuthActivity.this;
                        Class destinationActivity = LocationActivity.class;
                        Intent locationIntent = new Intent (context, destinationActivity);
                        locationIntent.putExtra("spotifyToken", mSpotifyToken);
                        startActivity(locationIntent);
                    }
                });
                mDescription.setVisibility(View.VISIBLE);
                mCurrentLocation.setVisibility(View.VISIBLE);
                mManualLocation.setVisibility(View.VISIBLE);
            } else {
                Context context = SpotifyAuthActivity.this;
                Class destinationActivity = SpotifyErrorActivity.class;
                Intent spotifyErrorIntent = new Intent (context, destinationActivity);
                startActivity(spotifyErrorIntent);
            }
        }
    }

    @Override
    public void onLoggedIn() {
        Log.d("LocationActivity", "User logged in");
    }

    @Override
    public void onLoggedOut() {
        Log.d("LocationActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Error var1) {
        Log.d("LocationActivity", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("LocationActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("LocationActivity", "Received connection message: " + message);
    }

    public class currentLocationTask extends AsyncTask<URL,Void,String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String locationResult = null;
            try {
                locationResult = SongKickUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return locationResult;
        }

        @Override
        protected void onPostExecute(String s) {
            ArrayList<com.example.android.eventsuggester.Location> locationResults = new ArrayList<com.example.android.eventsuggester.Location>();
            if (s == null) {
                Log.d("ISSUE", "should handle error here?");
                Context context = SpotifyAuthActivity.this;
                Class destinationActivity = SpotifyErrorActivity.class;
                Intent spotifyErrorIntent = new Intent (context, destinationActivity);
                startActivity(spotifyErrorIntent);
                return;
            } else {
                try {
                    locationResults = SongKickUtils.getLocation(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            Map<Integer, Integer> ids = new HashMap<>();
            for (com.example.android.eventsuggester.Location l : locationResults) {
                if (!ids.containsKey(l.getId())) {
                    ids.put(l.getId(), 1);
                } else {
                    int count = ids.get(l.getId());
                    ids.put(l.getId(), count +1);
                }
            }
            int mostCommon = 0;
            int winningID = 0;
            for (Map.Entry<Integer, Integer> entry : ids.entrySet()) {
                Integer id = entry.getKey();
                Integer count = entry.getValue();
                if (count > mostCommon) {
                    mostCommon = count;
                    winningID = id;
                }
            }

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SpotifyAuthActivity.this);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("location_id", String.valueOf(winningID));
            editor.putString("location_name", String.valueOf(locationResults.get(0).getCity()));
            editor.apply();

            Context context = SpotifyAuthActivity.this;
            Class destinationActivity = EventActivity.class;
            Intent eventIntent = new Intent (context, destinationActivity);
            eventIntent.putExtra("skID", String.valueOf(winningID));
            eventIntent.putExtra("spotifyToken", mSpotifyToken);
            startActivity(eventIntent);

        }
    }
}
