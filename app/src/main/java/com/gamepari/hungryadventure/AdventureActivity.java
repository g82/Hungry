package com.gamepari.hungryadventure;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.gamepari.hungryadventure.foods.ModelFood;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class AdventureActivity extends ActionBarActivity implements View.OnClickListener {

    private FoodsPagerAdapter mFoodPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_adventure);

        String countryName = getIntent().getStringExtra("country");
        getSupportActionBar().setTitle("Hungry " + countryName);

        findViewById(R.id.btn_history).setOnClickListener(this);

        ViewPager viewPager = (ViewPager) findViewById(R.id.vpager_foods);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setPageMargin(-500);

        mFoodPagerAdapter = new FoodsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mFoodPagerAdapter);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_history:
                break;
        }
    }

    private class FoodsPagerAdapter extends FragmentStatePagerAdapter {

        List<Fragment> listFragments;

        private FoodsPagerAdapter(FragmentManager fm) {
            super(fm);

            listFragments = new ArrayList<>();

            ModelFood[] foods = new ModelFood[6];

            foods[0] = new ModelFood(0, "Galbi", 300, new Date(System.currentTimeMillis()), "item_galbi.png");
            foods[1] = new ModelFood(1, "Bossam", 300, new Date(System.currentTimeMillis()), "item_bossam.png");
            foods[2] = new ModelFood(2, "Ddukbbok", 300, new Date(System.currentTimeMillis()), "item_dduk.png");
            foods[3] = new ModelFood(3, "galbitang", 300, new Date(System.currentTimeMillis()), "item_galbitang.png");
            foods[4] = new ModelFood(4, "Moolhoi", 300, new Date(System.currentTimeMillis()), "item_moolfish.png");
            foods[5] = new ModelFood(5, "Naengmyun", 300, "item_myun.png");

            for (ModelFood food : foods) {
                listFragments.add(FoodPageFragment.newInstance(food));
            }

            for (ModelFood food : foods) {
                listFragments.add(FoodPageFragment.newInstance(food));
            }

            for (ModelFood food : foods) {
                listFragments.add(FoodPageFragment.newInstance(food));
            }

            for (ModelFood food : foods) {
                listFragments.add(FoodPageFragment.newInstance(food));
            }

            for (ModelFood food : foods) {
                listFragments.add(FoodPageFragment.newInstance(food));
            }

            for (ModelFood food : foods) {
                listFragments.add(FoodPageFragment.newInstance(food));
            }

            //you have eaten 56% of seoul.

        }

        @Override
        public Fragment getItem(int i) {
            return listFragments.get(i);
        }

        @Override
        public int getCount() {
            return listFragments.size();
        }
    }

}
