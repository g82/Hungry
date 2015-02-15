package com.gamepari.hungryadventure;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gamepari.hungryadventure.country.ModelCountry;

import java.util.ArrayList;
import java.util.List;


public class SelectCountryActivity extends ActionBarActivity {

    CountryPagerAdapter mCountryPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_country);


        final ViewPager viewPager = (ViewPager) findViewById(R.id.vpager_country);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setPageMargin(-400);

        final Button btnStart = (Button) findViewById(R.id.btn_start);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent();
                i.setClass(SelectCountryActivity.this, AdventureActivity.class);

                String selectedCountryName = mCountryPagerAdapter.getListCountry().get(viewPager.getCurrentItem()).getName();
                Log.d(SelectCountryActivity.class.getSimpleName(), selectedCountryName);
                i.putExtra("country", selectedCountryName);

                startActivity(i);
                finish();
            }
        });


        mCountryPagerAdapter = new CountryPagerAdapter();
        viewPager.setAdapter(mCountryPagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {

                ModelCountry country = mCountryPagerAdapter.getListCountry().get(i);
                btnStart.setEnabled(!country.isLocked());
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });


    }


    private class CountryPagerAdapter extends PagerAdapter {

        private List<ModelCountry> listCountry = new ArrayList();


        private CountryPagerAdapter() {

            ModelCountry seoul = new ModelCountry("Seoul", R.drawable.thumb_seoul, false);
            ModelCountry tokyo = new ModelCountry("Tokyo", R.drawable.thumb_tokyo, true);

            listCountry.add(seoul);
            listCountry.add(tokyo);
        }

        @Override
        public float getPageWidth(int position) {

//            return 0.8f;
            return super.getPageWidth(position);
        }

        public List<ModelCountry> getListCountry() {
            return listCountry;
        }

        @Override
        public int getCount() {
            return listCountry.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);

            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            ModelCountry country = listCountry.get(position);

            View page = View.inflate(SelectCountryActivity.this, R.layout.page_country, null);

            TextView tvName = (TextView) page.findViewById(R.id.tv_country_name);
            ImageView ivThumb = (ImageView) page.findViewById(R.id.iv_thumb);
            TextView tvLocked = (TextView) page.findViewById(R.id.tv_country_locked);

            tvName.setText(country.getName());
            ivThumb.setImageResource(country.getThumbnail_id());
            tvLocked.setVisibility(country.isLocked() ? View.VISIBLE : View.INVISIBLE);

            container.addView(page, position);

            //return super.instantiateItem(container, position);
            return page;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }
    }

}
