package com.stecen.jacksonmusic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Collection;
import java.util.List;

/**
 * Created by stevecen on 4/27/18.
 */

public class ListMusicAdapter extends ArrayAdapter<Song> /*implements AdapterView.OnItemClickListener*/ {
    private Context context;
    private List<Song> songs;

    private final static String TAG = "adapter";

    class MusicOnClick implements RelativeLayout.OnClickListener {
        private Song song;
        public MusicOnClick(Song song) {
            this.song = song;
        }
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra(DetailsActivity.TRACK_KEY, song.getTrackStr());
            intent.putExtra(DetailsActivity.COLLECTION_KEY, song.getCollectionStr());
            intent.putExtra(DetailsActivity.GENRE_KEY, song.getGenreStr());
            intent.putExtra(DetailsActivity.DATE_KEY, song.getDateStr());
            // todo: image parceabble -- not just url is good

            // todo 

            context.startActivity(intent);
        }
    }

    public ListMusicAdapter(@NonNull Context context, @NonNull List<Song> songs) {
        super(context, 0, songs);
        this.context = context;
        this.songs = songs;
        Log.d(TAG, "constructor");
    }

    static class ViewCache {
        ImageView songImage;
        TextView trackText, collectionText;
        RelativeLayout songRelative;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        final Song currSong = songs.get(position);
        final ViewCache viewCache;
        if (convertView == null) {
            viewCache = new ViewCache();
            convertView = LayoutInflater.from(context).inflate(R.layout.row_music, parent, false);

            viewCache.trackText = (TextView) convertView.findViewById(R.id.track_text);
            viewCache.collectionText = (TextView) convertView.findViewById(R.id.collection_text);
            viewCache.songImage = (ImageView) convertView.findViewById(R.id.song_img);
            viewCache.songRelative = (RelativeLayout) convertView.findViewById(R.id.song_relative);
//            viewCache.songRelative.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(context, DetailsActivity.class);
//                    intent.putExtra(DetailsActivity.TRACK_KEY, currSong.getTrackStr());
//                    intent.putExtra(DetailsActivity.COLLECTION_KEY, currSong.getCollectionStr());
//                    intent.putExtra(DetailsActivity.GENRE_KEY, currSong.getGenreStr());
//                    intent.putExtra(DetailsActivity.DATE_KEY, currSong.getDateStr());
//                    // todo: image parceabble -- not just url is good
//
//                    context.startActivity(intent);
//                }
//            });

//            viewCache.trackText.setText(currSong.getTrackStr());
//            viewCache.collectionText.setText(currSong.getCollectionStr());
//            viewCache.songImage.setImageResource(R.mipmap.ic_launcher);
            convertView.setTag(viewCache);
        } else {
            Log.d(TAG, "converting");
            viewCache = (ViewCache) convertView.getTag();
        }
//        TextView trackText = (TextView) convertView.findViewById(R.id.track_text);
//        TextView collectionText = (TextView) convertView.findViewById(R.id.collection_text);
//        trackText.setText(currSong.getTrackStr());
//        collectionText.setText(currSong.getCollectionStr());

        viewCache.trackText.setText(currSong.getTrackStr());
        viewCache.collectionText.setText(currSong.getCollectionStr());
//        viewCache.songImage.setImageResource(R.mipmap.ic_launcher_round); // todo: default image when no internet
        viewCache.songRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra(DetailsActivity.TRACK_KEY, currSong.getTrackStr());
                intent.putExtra(DetailsActivity.COLLECTION_KEY, currSong.getCollectionStr());
                intent.putExtra(DetailsActivity.GENRE_KEY, currSong.getGenreStr());
                intent.putExtra(DetailsActivity.DATE_KEY, currSong.getDateStr());
                // todo: image parceabble -- not just url is good

                context.startActivity(intent);
            }
        });

        GetImageAsyncTask imageAsyncTask = new GetImageAsyncTask(currSong.getUrlStr(), new ImageAsyncInterface() {
            @Override
            public void processImage(Bitmap bitmap) {
                viewCache.songImage.setImageBitmap(bitmap);
                viewCache.songImage.setScaleX(1.5f);
                viewCache.songImage.setScaleY(1.5f);
            }
        });
        imageAsyncTask.execute();


        Log.d(TAG, "setting text");
        return convertView;
    }

    @Override
    public void add(@Nullable Song object) {
        super.add(object);
    }

    @Override
    public void addAll(@NonNull Collection<? extends Song> collection) {
        super.addAll(collection);
    }

    @Override
    public void addAll(Song... items) {
        super.addAll(items);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public Song getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getPosition(@Nullable Song item) {
        return super.getPosition(item);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
