package com.stecen.jacksonmusic;

import android.os.AsyncTask;
import android.util.Log;

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

/**
 * Created by stevecen on 4/27/18.
 */

public class MusicJSONAsyncTask extends AsyncTask<Void, Integer, List<Song>> {
    private static final String MUSIC_QUERY = "https://itunes.apple.com/search?term=Michael+jackson";
//    private static final String MUSIC_QUERY = "https://glosbe.com/gapi/translate?from=eng&dest=zho&format=json&pretty=true&phrase=";
    private static final int TIMEOUT = 15000;

    private static final String RESULTS_KEY = "results";
    private static final String KIND_KEY = "kind", KIND_SONG_VAL = "song";
    private static final String TRACK_KEY = "trackName";
    private static final String COLLECTIONS_KEY = "collectionName";
    private static final String RELEASE_DATE_KEY = "releaseDate";
    private static final String URL60_KEY = "artworkUrl60";
    private static final String URL100_KEY = "artworkUrl100";
    private static final String GENRE_KEY = "primaryGenreName";

    private static final String NOT_FOUND = "---";

    MusicJSONAsyncInterface callback;

    public MusicJSONAsyncTask(MusicJSONAsyncInterface callback) {
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
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

                     songsList = getSongsFromJson(resultsArr); // to let the finally block run
                }
            }

        } catch (IOException e) {
            Log.e("IOException", e.toString());
        } catch (JSONException e) {
            Log.e("JSONException", e.toString());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e("Reader", e.toString());
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

            songsList.add(new Song(trackStr, collectionStr, dateStr, urlStr, genreStr));
        }

        return songsList;

    }

    private boolean isValidKey(JSONObject obj, String key) {
        return obj.has(key) && !obj.isNull(key);
    }

    @Override
    protected void onPostExecute(List<Song> songsList) {
        super.onPostExecute(songsList);
        Log.e("size1",""+ songsList.size());
        callback.processMusic(songsList);

    }
}
