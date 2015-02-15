package com.gamepari.hungryadventure;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.gamepari.hungryadventure.foods.ModelFood;

import java.text.SimpleDateFormat;
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
        viewPager.setPageMargin(-600);
        viewPager.setOffscreenPageLimit(6);

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

            ModelFood food1 = new ModelFood(0, "Galbi", 300, new Date(System.currentTimeMillis()), R.drawable.item_galbi);
            ModelFood food2 = new ModelFood(1, "Bossam", 300, new Date(System.currentTimeMillis()), R.drawable.item_bossam);
            ModelFood food3 = new ModelFood(2, "Ddukbbok", 300, new Date(System.currentTimeMillis()), R.drawable.item_dduk);
            ModelFood food4 = new ModelFood(3, "galbitang", 300, new Date(System.currentTimeMillis()), R.drawable.item_galbitang);
            ModelFood food5 = new ModelFood(4, "Moolhoi", 300, new Date(System.currentTimeMillis()), R.drawable.item_moolfish);
            ModelFood food6 = new ModelFood(5, "Naengmyun", 300, null, R.drawable.item_myun);

            FoodPageFragment foodPageFragment = FoodPageFragment.newInstance(food1);

            listFragments.add(foodPageFragment);

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

    private class FoodPagerAdapter extends PagerAdapter {

        SimpleDateFormat simpleDateFormat;
        private List<ModelFood> listFoods;

        private FoodPagerAdapter() {

            listFoods = new ArrayList<>();

            ModelFood food1 = new ModelFood(0, "Galbi", 300, new Date(System.currentTimeMillis()), R.drawable.item_galbi);
            ModelFood food2 = new ModelFood(1, "Bossam", 300, new Date(System.currentTimeMillis()), R.drawable.item_bossam);
            ModelFood food3 = new ModelFood(2, "Ddukbbok", 300, new Date(System.currentTimeMillis()), R.drawable.item_dduk);
            ModelFood food4 = new ModelFood(3, "galbitang", 300, new Date(System.currentTimeMillis()), R.drawable.item_galbitang);
            ModelFood food5 = new ModelFood(4, "Moolhoi", 300, new Date(System.currentTimeMillis()), R.drawable.item_moolfish);
            ModelFood food6 = new ModelFood(5, "Naengmyun", 300, null, R.drawable.item_myun);

            listFoods.add(food1);
            listFoods.add(food2);
            listFoods.add(food3);
            listFoods.add(food4);
            listFoods.add(food5);
            listFoods.add(food6);


            simpleDateFormat = new SimpleDateFormat("MM dd, yyyy");
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);
            ((ViewPager) container).removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View v = View.inflate(AdventureActivity.this, R.layout.page_foods, null);

            ModelFood food = listFoods.get(position);

            Date unlockedDate = food.getmUnlockDate();

            ImageView ivFood = (ImageView) v.findViewById(R.id.iv_food);
            ivFood.setImageResource(food.getmImageResourceId());

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
                tvDate.setText("Unlocked " + simpleDateFormat.format(unlockedDate));

            }

//            return super.instantiateItem(container, position);

            ((ViewPager) container).addView(v);

            return v;
        }

        @Override
        public int getCount() {
            return listFoods.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }
    }
}
