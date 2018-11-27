package com.example.android.engagingtechtest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.InputStream;


public class ProfileActivity extends AppCompatActivity {

    private ProgressBar mLoadingIndicator;
    private TextView mLoadingText;
    private static final int PROFILE_DATA_LOADERID = 99;
    private ImageView mProfileImage;
    private TextView mUserName;
    private TextView mUserNumber;
    private Profile mProfile;
    private RelativeLayout mProfileLayout;
    private RecyclerView mRecyclerView;
    private TelephoneAdapter mTelephoneAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
        mLoadingIndicator.setVisibility(View.VISIBLE);
        mLoadingText = (TextView) findViewById(R.id.loading_text);
        mLoadingText.setVisibility(View.VISIBLE);

        mProfileLayout = (RelativeLayout) findViewById(R.id.profileLayout);

        mProfileImage = (ImageView) findViewById(R.id.profile_image);
        mProfileImage.setClipToOutline(true);

        mUserName = (TextView) findViewById(R.id.user_name);
        mUserNumber = (TextView) findViewById(R.id.user_number);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_telephones);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mTelephoneAdapter = new TelephoneAdapter();
        mRecyclerView.setAdapter(mTelephoneAdapter);
        mRecyclerView.setFocusable(false);


        getSupportLoaderManager().initLoader(PROFILE_DATA_LOADERID, null, loaderCallbacks);

    }

    private void showDataView() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mLoadingText.setVisibility(View.INVISIBLE);

        new DownloadImageTask(mProfileImage).execute(mProfile.getProfileURL());
        mUserName.setText(mProfile.getName());
        mUserNumber.setText("#" + mProfile.getUserNumber());

        mTelephoneAdapter.setmTelephoneData(mProfile.getNumbers());

        mProfileLayout.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        //add a text view to display an error
    }

    private LoaderManager.LoaderCallbacks<Profile> loaderCallbacks = new LoaderManager.LoaderCallbacks<Profile>() {
        @Override
        public Loader<Profile> onCreateLoader(int id, Bundle args) {
            Context context = getApplicationContext();

            return new ProfileLoader(context);
        }

        @Override
        public void onLoadFinished(Loader<Profile> loader, Profile data) {
            Log.d("", "load finished!");
            if (data == null) {
                showErrorMessage();
            } else {
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                mProfile = data;
                showDataView();
            }
        }

        @Override
        public void onLoaderReset(Loader<Profile> loader) {
            //
        }
    };

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap image = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                image = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return image;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}
