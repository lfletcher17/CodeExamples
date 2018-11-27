package com.example.android.eventsuggester.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "blacklisted_artist")
public class ArtistEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int artistId;
    private String artistName;

    @Ignore
    public ArtistEntity(int artistId, String artistName) {
        this.artistId = artistId;
        this.artistName = artistName;
    }

    public ArtistEntity(int id, int artistId, String artistName) {
        this.id = id;
        this.artistId = artistId;
        this.artistName = artistName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getArtistId() {
        return artistId;
    }

    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }
}
