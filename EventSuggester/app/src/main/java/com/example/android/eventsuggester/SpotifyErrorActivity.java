package com.example.android.eventsuggester;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

public class SpotifyErrorActivity extends AppCompatActivity {

    private Button mTryAgain;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify_error);
        mTryAgain = (Button) findViewById(R.id.btn_try_again);
        mTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = SpotifyErrorActivity.this;
                Class destinationActivity = SpotifyAuthActivity.class;
                Intent spotifyErrorIntent = new Intent (context, destinationActivity);
                startActivity(spotifyErrorIntent);
            }
        });


    }

}
