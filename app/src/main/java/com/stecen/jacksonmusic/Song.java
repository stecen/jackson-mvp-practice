package com.stecen.jacksonmusic;

import java.util.concurrent.TimeUnit;

/**
 * Created by stevecen on 4/27/18.
 */

public class Song {
    private String trackStr, collectionStr, dateStr, urlStr, genreStr;
    private int timeMillis;
    // genre?

    public Song(String trackName, String collectionName, String dateStr, String urlStr, String genreStr, int timeMillis) {
        this.trackStr = trackName;
        this.collectionStr = collectionName;
        this.dateStr = dateStr;
        this.urlStr = urlStr;
        this.genreStr = genreStr;
        this.timeMillis = timeMillis;
    }

    public String getTrackStr() {
        return this.trackStr;
    }

    public String getCollectionStr() {
        return this.collectionStr;
    }

    public String getDateStr() {
        return this.dateStr;
    }

    public String getYearStr() {
        if (this.dateStr.length() >= 4) {
            return this.dateStr.substring(0, 4);
        } else {
            return this.dateStr;
        }
    }

    public String getUrlStr() {
        return this.urlStr;
    }

    public String getGenreStr() {
        return this.genreStr;
    }

    public int getTimeMillis() {
        return this.timeMillis;
    }

    // Assumes no hour-long-plus music
    public String getTimeStr() {
        String minute = Long.toString(TimeUnit.MILLISECONDS.toMinutes(this.timeMillis));

        long secondLong = TimeUnit.MILLISECONDS.toSeconds(this.timeMillis)
                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(this.timeMillis));
        String second = ((secondLong >= 10) ? "" : "0") + Long.toString(secondLong);

        return minute + ":" + second;
    }

    public static void getSongData(MusicJSONAsyncInterface callback) {
        MusicJSONAsyncTask musicAsync = new MusicJSONAsyncTask(callback);
        musicAsync.execute();
    }
}
