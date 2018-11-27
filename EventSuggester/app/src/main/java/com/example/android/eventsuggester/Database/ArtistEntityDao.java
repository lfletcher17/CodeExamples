package com.example.android.eventsuggester.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ArtistEntityDao {

    @Query("SELECT * FROM blacklisted_artist")
    List<ArtistEntity> loadAllEvents();

    @Insert
    void insertEvent(ArtistEntity artistEntity);

    @Delete
    void deleteEvent(ArtistEntity artistEntity);

}
