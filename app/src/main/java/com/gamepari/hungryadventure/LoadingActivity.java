package com.gamepari.hungryadventure;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.gamepari.hungryadventure.preferences.PreferenceIO;


public class LoadingActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        //TODO a little bit delay

        String country = PreferenceIO.loadPreference(this, PreferenceIO.KEY_COUNTRY);

        if (country == null) {
            startActivity(new Intent(this, SelectCityActivity.class));
        } else {
            startActivity(new Intent(this, AdventureActivity.class));
        }

        finish();
    }

}
