package com.example.android.eventsuggester;

import java.util.ArrayList;

public class TestClass {

    private static String exampleArtist = "The Streets";

    public static void main (String[] args) {
//        String[] splitted = exampleArtist.split("\\s+");
//        String artistFor = "";
//        for (int i = 0; i < splitted.length; i++) {
//            if(i > 0) {
//                artistFor = artistFor + "+";
//            }
//            artistFor = artistFor + splitted[i];
//        }
//        System.out.println(artistFor);
        SongKickArtist sA = new SongKickArtist(0,"lee fletcher","some url", "headline");
        ArrayList<SongKickArtist> aL = new ArrayList<SongKickArtist>();
        aL.add(sA);
        Event e = new Event(0,"some event","o2","2018-09-17","some url", aL);
        System.out.println(e.formattedDate());
    }
}
