package com.gamepari.hungryadventure;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gamepari.hungryadventure.assets.AssetImageTask;
import com.gamepari.hungryadventure.contents.HungryDatabase;
import com.gamepari.hungryadventure.contents.ModelCity;
import com.gamepari.hungryadventure.preferences.PreferenceIO;

import java.util.ArrayList;
import java.util.List;


public class SelectCityActivity extends ActionBarActivity {

    CountryPagerAdapter mCountryPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_country);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.vpager_country);
        viewPager.setOffscreenPageLimit(2);
        //viewPager.setPageMargin(-400);

        final Button btnStart = (Button) findViewById(R.id.btn_start);


        // Adventure Start.
        // save country  preference
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String selectedCountryName = mCountryPagerAdapter.getListCountry().get(viewPager.getCurrentItem()).getEng_name();
                Log.d(SelectCityActivity.class.getSimpleName(), selectedCountryName);

                PreferenceIO.savePreference(SelectCityActivity.this, PreferenceIO.KEY_COUNTRY, selectedCountryName);

                Time time = new Time();
                time.setToNow();

                PreferenceIO.savePreference(SelectCityActivity.this, PreferenceIO.KEY_START_DATE, String.valueOf(time.toMillis(true)));

                startActivity(new Intent(SelectCityActivity.this, AdventureActivity.class));
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

                ModelCity country = mCountryPagerAdapter.getListCountry().get(i);
                btnStart.setEnabled(country.isUnlocked());
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        new DatabaseTask().execute();
    }

    private class DatabaseTask extends AsyncTask<Void, Void, List<ModelCity>> {

        @Override
        protected List<ModelCity> doInBackground(Void... voids) {

            HungryDatabase database = new HungryDatabase(SelectCityActivity.this, HungryDatabase.DB_HUNGRY, null, 1);
            return database.getCities();
        }

        @Override
        protected void onPostExecute(List<ModelCity> modelCountries) {
            super.onPostExecute(modelCountries);
            mCountryPagerAdapter.setDatas(modelCountries);
        }
    }

    private class CountryPagerAdapter extends PagerAdapter {

        private List<ModelCity> listCountry = new ArrayList();

        public List<ModelCity> getListCountry() {
            return listCountry;
        }

        @Override
        public int getCount() {
            return listCountry.size();
        }

        public void setDatas(List<ModelCity> listCity) {
            for (ModelCity city : listCity) {

                listCountry.add(city);
            }

            notifyDataSetChanged();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            ModelCity country = listCountry.get(position);

            View page = View.inflate(SelectCityActivity.this, R.layout.page_country, null);

            TextView tvName = (TextView) page.findViewById(R.id.tv_country_name);
            ImageView ivThumb = (ImageView) page.findViewById(R.id.iv_thumb);
            TextView tvLocked = (TextView) page.findViewById(R.id.tv_country_locked);
            TextView tvEngName = (TextView) page.findViewById(R.id.tv_eng_name);

            tvName.setText(country.getLocal_name());
            tvEngName.setText(country.getEng_name());

            new AssetImageTask(SelectCityActivity.this, ivThumb).execute(country.getLocal_name(), country.getMimgPath());

            tvLocked.setVisibility(country.isUnlocked() ? View.INVISIBLE : View.VISIBLE);

            container.addView(page, position);

            return page;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }
    }

}
