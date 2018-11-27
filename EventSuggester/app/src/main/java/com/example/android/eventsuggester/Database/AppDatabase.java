package com.example.android.eventsuggester.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

@Database(entities = {EventEntity.class, ArtistEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "event_suggester";
    private static AppDatabase sInstance;

    public static AppDatabase getsInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating Database");
                //NEED TO REMOVE THIS SHOULD ONLY RUN QUERIES ON SEPERATE THREAD
                sInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, AppDatabase.DATABASE_NAME).allowMainThreadQueries().build();
            }
        }
        Log.d(LOG_TAG, "getting database");
        return sInstance;
    }

    public abstract EventEntityDao eventEntityDao();

    public abstract ArtistEntityDao artistEntityDao();


}
