package com.example.android.engagingtechtest;

import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button mTapMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTapMe = (Button) findViewById(R.id.btn_tap_me);
        mTapMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProfile();
            }
        });
    }

    public void openProfile () {
        Context context = MainActivity.this;
        Class destinationActivity = ProfileActivity.class;
        Intent profileIntent = new Intent (context, destinationActivity);
        startActivity(profileIntent);
    }
}
