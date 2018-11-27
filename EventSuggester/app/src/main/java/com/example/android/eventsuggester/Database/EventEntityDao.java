package com.example.android.eventsuggester.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface EventEntityDao {

    @Query("SELECT * FROM attended_event")
    List<EventEntity> loadAllEvents();

    @Query("SELECT * FROM attended_event WHERE event_id == :eventID")
    EventEntity loadEvent(String eventID);

    @Insert
    void insertEvent(EventEntity eventEntity);

    @Delete
    void deleteEvent(EventEntity eventEntity);

}
