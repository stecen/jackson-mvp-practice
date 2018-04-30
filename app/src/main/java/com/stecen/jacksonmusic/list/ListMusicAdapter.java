package com.stecen.jacksonmusic.list;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.stecen.jacksonmusic.details.DetailsActivity;
import com.stecen.jacksonmusic.R;
import com.stecen.jacksonmusic.data.Song;
import com.stecen.jacksonmusic.web.GetImageAsyncTask;

import java.util.List;

public class ListMusicAdapter extends ArrayAdapter<Song> {
    private Context context;
    private List<Song> songs;

    public ListMusicAdapter(@NonNull Context context, @NonNull List<Song> songs) {
        super(context, 0, songs);
        this.context = context;
        this.songs = songs;
    }

    static class ViewCache {
        ImageView songImage;
        TextView trackText, collectionText;
        RelativeLayout songRelative;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Song currSong = songs.get(position);
        final ViewCache viewCache;
        if (convertView == null) {
            viewCache = new ViewCache();
            convertView = LayoutInflater.from(context).inflate(R.layout.row_music, parent, false);

            viewCache.trackText = convertView.findViewById(R.id.track_text);
            viewCache.collectionText = convertView.findViewById(R.id.collection_text);
            viewCache.songImage = convertView.findViewById(R.id.song_img);
            viewCache.songRelative = convertView.findViewById(R.id.song_relative);

            convertView.setTag(viewCache);
        } else {
            viewCache = (ViewCache) convertView.getTag();
        }

        viewCache.trackText.setText(currSong.getTrackStr());
        viewCache.collectionText.setText(currSong.getCollectionStr());
        viewCache.songRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailsIntent = new Intent(context, DetailsActivity.class);
                detailsIntent.putExtra(DetailsActivity.TRACK_KEY, currSong.getTrackStr());
                detailsIntent.putExtra(DetailsActivity.COLLECTION_KEY, currSong.getCollectionStr());
                detailsIntent.putExtra(DetailsActivity.GENRE_KEY, currSong.getGenreStr());
                detailsIntent.putExtra(DetailsActivity.DATE_KEY, currSong.getYearStr());
                detailsIntent.putExtra(DetailsActivity.IMG_KEY, currSong.getUrlStr());
                detailsIntent.putExtra(DetailsActivity.TIME_KEY, currSong.getTimeStr());

                context.startActivity(detailsIntent);
            }
        });

        GetImageAsyncTask imageAsyncTask = new GetImageAsyncTask(currSong.getUrlStr(), new GetImageAsyncTask.ImageAsyncInterface() {
            @Override
            public void processImage(Bitmap bitmap) {
                viewCache.songImage.setImageBitmap(bitmap);
            }
        });
        imageAsyncTask.execute();

        return convertView;
    }
}
