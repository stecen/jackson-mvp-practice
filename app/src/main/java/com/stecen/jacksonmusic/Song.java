package com.stecen.jacksonmusic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stevecen on 4/27/18.
 */

public class Song {
    private String trackStr, collectionStr, dateStr, urlStr, genreStr;
    // genre?

    public Song(String trackName, String collectionName, String dateStr, String urlStr, String genreStr) {
        this.trackStr = trackName;
        this.collectionStr = collectionName;
        this.dateStr = dateStr;
        this.urlStr = urlStr;
        this.genreStr = genreStr;
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

    public static void getSongData(MusicJSONAsyncInterface callback) {
        MusicJSONAsyncTask musicAsync = new MusicJSONAsyncTask(callback);
        musicAsync.execute();
    }
}
