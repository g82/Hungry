package com.gamepari.hungryadventure;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.gamepari.hungryadventure.assets.AssetImageTask;
import com.gamepari.hungryadventure.contents.ModelFood;

import java.text.SimpleDateFormat;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FoodPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FoodPageFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "food_param";
    AdventureActivity mActivity;
    // TODO: Rename and change types of parameters
    private ModelFood modelFood;


    public FoodPageFragment() {
        // Required empty public constructor
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (AdventureActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            modelFood = (ModelFood) getArguments().getSerializable(ARG_PARAM1);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_food_page, container, false);

        ModelFood food = modelFood;

        Time unlockedDate = food.getmUnlockTime();

        ImageView ivFood = (ImageView) v.findViewById(R.id.iv_food);

        new AssetImageTask(mActivity, ivFood).execute(food.getmName_local(), food.getmAssetImagePath());

        FrameLayout flLocked = (FrameLayout) v.findViewById(R.id.fl_locked);
        TextView tvName = (TextView) v.findViewById(R.id.tv_food_name);
        tvName.setText(food.getmName_local());

        TextView tvDate = (TextView) v.findViewById(R.id.tv_food_unlockdate);

        if (unlockedDate == null) {
            //FOOD IS LOCK.
            flLocked.setVisibility(View.VISIBLE);

            tvName.setText("Locked");
            tvDate.setVisibility(View.INVISIBLE);
        } else {
            //FOOD UNLOCK.
            flLocked.setVisibility(View.INVISIBLE);

            tvName.setText(food.getmName_local());

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM dd, yyyy");
            tvDate.setText("Unlocked " + simpleDateFormat.format(unlockedDate));
        }

        ivFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.showUnlockDialog();
            }
        });

        return v;
    }


}
