package com.example.android.eventsuggester.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "attended_event")
public class EventEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "event_id")
    private int eventId;

    @Ignore
    public EventEntity(int eventId) {
        this.eventId = eventId;
    }

    public EventEntity(int id, int eventId) {
        this.id = id;
        this.eventId = eventId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }
}
