package com.stecen.jacksonmusic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

/**
 * Created by stevecen on 4/28/18.
 */

public class DetailsActivity extends AppCompatActivity {
    public static final String TRACK_KEY = "TRACK";
    public static final String COLLECTION_KEY = "COLLEC";
    public static final String GENRE_KEY = "GENRE";
    public static final String DATE_KEY = "DATE";
    public static final String IMG_KEY = "IMG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle("THE JACKSON");
//        setSupportActionBar(toolbar);
        // todo: toolbar

        Intent infoIntent = getIntent();
        if (infoIntent != null) {
            TextView trackText, collectionText, genreText, dateText;
            trackText = (TextView) findViewById(R.id.track_detail_text);
            collectionText = (TextView) findViewById(R.id.collection_detail_text);
            genreText = (TextView) findViewById(R.id.genre_detail_text);
            dateText = (TextView) findViewById(R.id.date_detail_text);

            trackText.setText(infoIntent.getStringExtra(TRACK_KEY));
            collectionText.setText(infoIntent.getStringExtra(COLLECTION_KEY));
            genreText.setText(infoIntent.getStringExtra(GENRE_KEY));
            dateText.setText(infoIntent.getStringExtra(DATE_KEY));
        }


    }
}
