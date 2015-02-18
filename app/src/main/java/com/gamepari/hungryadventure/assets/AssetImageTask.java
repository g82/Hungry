package com.gamepari.hungryadventure.assets;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by gamepari on 2015-02-15.
 */
public class AssetImageTask extends AsyncTask<String, Void, Bitmap> {

    /**
     * CACHE SIZE = 30Mbytes.
     */
    private static final int CACHE_SIZE = 30 * 1024 * 1024;
    private static LruCache<String, Bitmap> sImageCache;
    private ImageView mImageView;
    private Activity mActivity;

    public AssetImageTask(Activity activity, ImageView imageView) {
        mImageView = imageView;
        mActivity = activity;
    }

    public static LruCache<String, Bitmap> getCache() {

        if (sImageCache == null) {
            sImageCache = new LruCache<>(CACHE_SIZE);
        }
        return sImageCache;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    /**
     * @param params 0 key, 1 value.
     * @return
     */
    @Override
    protected Bitmap doInBackground(String... params) {

        String key = params[0];
        String assetPath = params[1];

        Bitmap bitmap = getCache().get(key);

        if (bitmap != null) {
            Log.d("LoadAssetTask", "cache hitted.");
            return bitmap;
        }

        try {
            InputStream inputStream = mActivity.getAssets().open(assetPath);

            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inPreferredConfig = Bitmap.Config.RGB_565;

            bitmap = BitmapFactory.decodeStream(inputStream, null, opts);

            getCache().put(key, bitmap);
            Log.d("LoadAssetTask", "cache putted.");

            inputStream.close();

            return bitmap;

        } catch (IOException e) {
            Log.d("LoadAssetTask", e.getMessage());
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (bitmap != null) mImageView.setImageBitmap(bitmap);
    }


}
