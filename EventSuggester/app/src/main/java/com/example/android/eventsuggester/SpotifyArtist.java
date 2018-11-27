package com.example.android.eventsuggester;

import kaaes.spotify.webapi.android.models.Artist;
import android.os.Parcel;
import android.support.annotation.NonNull;

import java.util.Comparator;

public class SpotifyArtist implements Comparable<SpotifyArtist> {

    private Artist artist;
    private int score;

    public SpotifyArtist (Artist artist, int score) {
        this.artist = artist;
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }


    @Override
    public int compareTo(@NonNull SpotifyArtist spotifyArtist) {

        Integer score = this.score;
        Integer otherScore = spotifyArtist.getScore();

        if (score > otherScore) {
            return -1;
        } else if (score.equals(otherScore)) {
            return 0;
        } else {
            return 1;
        }

    }
}
