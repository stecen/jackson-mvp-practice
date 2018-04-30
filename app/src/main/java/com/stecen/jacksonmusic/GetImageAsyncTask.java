package com.stecen.jacksonmusic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by stevecen on 4/28/18.
 */

public class GetImageAsyncTask extends AsyncTask<Void, Integer, Bitmap> {
    private ImageAsyncInterface callback;
    private String urlStr;
    private final static String TAG = "imageAsyncTask";

    public GetImageAsyncTask(String urlStr, ImageAsyncInterface callback) {
        this.callback = callback;
        this.urlStr = urlStr;
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        Bitmap bitmap = null;
        try {
            InputStream inputStream = new java.net.URL(urlStr).openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        callback.processImage(bitmap);
    }
}
