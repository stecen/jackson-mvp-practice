package com.stecen.jacksonmusic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.util.List;

public class MusicListActivity extends AppCompatActivity implements MusicJSONAsyncInterface {
    ListViewCompat musicList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextAppearance(this, R.style.JacksonText);
        setSupportActionBar(toolbar);

        musicList = findViewById(R.id.music_list);

        Song.getSongData(this);
    }

    @Override
    public void processMusic(List<Song> songList) {
        ListMusicAdapter adapter = new ListMusicAdapter(this, songList);
        musicList.setAdapter(adapter);
        Log.e("size", Integer.toString(songList.size()));
    }
}
