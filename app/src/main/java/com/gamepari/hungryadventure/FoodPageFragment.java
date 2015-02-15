package com.gamepari.hungryadventure;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.gamepari.hungryadventure.foods.ModelFood;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FoodPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FoodPageFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "food_param";

    // TODO: Rename and change types of parameters
    private ModelFood modelFood;

    AdventureActivity mActivity;


    public FoodPageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (AdventureActivity) activity;
    }

    // TODO: Rename and change types and number of parameters
    public static FoodPageFragment newInstance(ModelFood modelFood) {
        FoodPageFragment fragment = new FoodPageFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, modelFood);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            modelFood = (ModelFood) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    private class LoadAssetTask extends AsyncTask<ModelFood, Void, Bitmap> {

        private ImageView mImageView;

        public LoadAssetTask(ImageView imageView) {
            mImageView = imageView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(ModelFood... params) {

            String assetPath = params[0].getmAssetImagePath();

            Bitmap bitmap = mActivity.getBitmapFromCache(params[0].getmName());

            if (bitmap != null) {
                Log.d("LoadAssetTask", "cache hitted.");
                return bitmap;
            }

            try {
                InputStream inputStream = getActivity().getAssets().open(assetPath);

                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inPreferredConfig = Bitmap.Config.RGB_565;

                bitmap = BitmapFactory.decodeStream(inputStream, null, opts);

                mActivity.putBitmapToCache(params[0].getmName(), bitmap);
                Log.d("LoadAssetTask", "cache putted.");

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

    private class LoadAssetTask extends AsyncTask<ModelFood, Void, Bitmap> {

        private ImageView mImageView;

        public LoadAssetTask(ImageView imageView) {
            mImageView = imageView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(ModelFood... params) {

            String assetPath = params[0].getmAssetImagePath();

            Bitmap bitmap = mActivity.getBitmapFromCache(params[0].getmName());

            if (bitmap != null) {
                Log.d("LoadAssetTask", "cache hitted.");
                return bitmap;
            }

            try {
                InputStream inputStream = getActivity().getAssets().open(assetPath);

                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inPreferredConfig = Bitmap.Config.RGB_565;

                bitmap = BitmapFactory.decodeStream(inputStream, null, opts);

                mActivity.putBitmapToCache(params[0].getmName(), bitmap);
                Log.d("LoadAssetTask", "cache putted.");

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_food_page, container, false);

        ModelFood food = modelFood;

        Date unlockedDate = food.getmUnlockDate();

        ImageView ivFood = (ImageView) v.findViewById(R.id.iv_food);

        new LoadAssetTask(ivFood).execute(food);

        FrameLayout flLocked = (FrameLayout) v.findViewById(R.id.fl_locked);
        TextView tvName = (TextView) v.findViewById(R.id.tv_food_name);
        tvName.setText(food.getmName());

        TextView tvDate = (TextView) v.findViewById(R.id.tv_food_unlockdate);

        if (unlockedDate == null) {
            //FOOD IS LOCK.
            flLocked.setVisibility(View.VISIBLE);

            tvName.setText("Locked");
            tvDate.setVisibility(View.INVISIBLE);
        } else {
            //FOOD UNLOCK.
            flLocked.setVisibility(View.INVISIBLE);

            tvName.setText(food.getmName());

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM dd, yyyy");
            tvDate.setText("Unlocked " + simpleDateFormat.format(unlockedDate));
        }

        return v;
    }


}
