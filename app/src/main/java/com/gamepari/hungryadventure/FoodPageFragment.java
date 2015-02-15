package com.gamepari.hungryadventure;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gamepari.hungryadventure.foods.ModelFood;


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
        return inflater.inflate(R.layout.fragment_food_page, container, false);
    }


}
