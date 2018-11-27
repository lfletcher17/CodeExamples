package com.example.android.eventsuggester;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.spotify.sdk.android.authentication.AuthenticationClient;

public class SettingsActivity extends AppCompatActivity {

    private TextView mCurrentLocation;
    private Button mChangeLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mCurrentLocation = (TextView) findViewById(R.id.location_name);

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
        mCurrentLocation.setText(sharedPreferences.getString("location_name", "null"));
        mChangeLocation = (Button) findViewById(R.id.btn_change_location);
        mChangeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = SettingsActivity.this;
                Class destinationActivity = SpotifyAuthActivity.class;
                Intent locationIntent = new Intent (context, destinationActivity);
                locationIntent.putExtra("change_location", true);
                startActivity(locationIntent);
            }
        });


    }


}
