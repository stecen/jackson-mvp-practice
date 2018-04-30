package com.stecen.jacksonmusic.list;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.stecen.jacksonmusic.R;
import com.stecen.jacksonmusic.data.Song;
import com.stecen.jacksonmusic.web.MusicJSONAsyncTask;

import java.util.List;

public class MusicListActivity extends AppCompatActivity implements MusicJSONAsyncTask.MusicJSONAsyncInterface {
    ListViewCompat musicList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_str);
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
