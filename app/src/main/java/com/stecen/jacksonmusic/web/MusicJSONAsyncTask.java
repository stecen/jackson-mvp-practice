package com.stecen.jacksonmusic.web;

import android.os.AsyncTask;
import android.util.Log;

import com.stecen.jacksonmusic.data.Song;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MusicJSONAsyncTask extends AsyncTask<Void, Integer, List<Song>> {
    private static final String MUSIC_QUERY = "https://itunes.apple.com/search?term=Michael+jackson";
    private static final int TIMEOUT = 15000;

    private static final String RESULTS_KEY = "results";
    private static final String KIND_KEY = "kind", KIND_SONG_VAL = "song";
    private static final String TRACK_KEY = "trackName";
    private static final String COLLECTIONS_KEY = "collectionName";
    private static final String RELEASE_DATE_KEY = "releaseDate";
    private static final String URL60_KEY = "artworkUrl60";
    private static final String URL100_KEY = "artworkUrl100";
    private static final String GENRE_KEY = "primaryGenreName";
    private static final String TIME_KEY = "trackTimeMillis";

    private static final String NOT_FOUND = "---";

    private static final String TAG = "musicAsync";

    MusicJSONAsyncInterface callback;

    public MusicJSONAsyncTask(MusicJSONAsyncInterface callback) {
        this.callback = callback;
    }

    @Override
    protected List<Song> doInBackground(Void... voids) {
        BufferedReader reader = null;
        URL url;

        List<Song> songsList = new ArrayList<>();

        try {
            url = new URL(MUSIC_QUERY);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setConnectTimeout(TIMEOUT);
            con.setDoInput(true);
            con.setDoOutput(false);

            int responseCode = con.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                StringBuilder sb = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }

                String jsonString = sb.toString();
                JSONObject resObj = new JSONObject(jsonString);

                if(isValidKey(resObj, RESULTS_KEY) ) {
                    JSONArray resultsArr = resObj.getJSONArray(RESULTS_KEY);

                     songsList = getSongsFromJson(resultsArr);
                }
            }

        } catch (IOException | JSONException e) {
            Log.e(TAG, e.toString());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                }
            }
        }
        return songsList;
    }

    private List<Song> getSongsFromJson(JSONArray songsArr) throws JSONException {
        ArrayList<Song> songsList = new ArrayList<>();

        for (int songIdx = 0; songIdx < songsArr.length(); songIdx++) {
            JSONObject songObj = songsArr.getJSONObject(songIdx);
            String kindStr = songObj.getString(KIND_KEY);
            if (!kindStr.equals(KIND_SONG_VAL)) {
                continue;
            }

            String trackStr = NOT_FOUND,
                    collectionStr = NOT_FOUND,
                    dateStr = NOT_FOUND,
                    urlStr = NOT_FOUND,
                    genreStr = NOT_FOUND;
            int timeMillis = 0;
            if (isValidKey(songObj, TRACK_KEY)) {
                trackStr = songObj.getString(TRACK_KEY);
            }
            if (isValidKey(songObj, COLLECTIONS_KEY)) {
                collectionStr = songObj.getString(COLLECTIONS_KEY);
            }
            if (isValidKey(songObj, RELEASE_DATE_KEY)) {
                dateStr = songObj.getString(RELEASE_DATE_KEY);
            }
            if (isValidKey(songObj, URL100_KEY)) {
                urlStr = songObj.getString(URL100_KEY);
            }
            if (isValidKey(songObj, GENRE_KEY)) {
                genreStr = songObj.getString(GENRE_KEY);
            }
            if (isValidKey(songObj, TIME_KEY)) {
                timeMillis = Integer.valueOf(songObj.getString(TIME_KEY));
            }

            songsList.add(new Song(trackStr, collectionStr, dateStr, urlStr, genreStr, timeMillis));
        }

        return songsList;

    }

    private boolean isValidKey(JSONObject obj, String key) {
        return obj.has(key) && !obj.isNull(key);
    }

    @Override
    protected void onPostExecute(List<Song> songsList) {
        super.onPostExecute(songsList);
        callback.processMusic(songsList);

    }

    public interface MusicJSONAsyncInterface {
        void processMusic(List<Song> songList);
    }
}
