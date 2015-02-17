package com.gamepari.hungryadventure;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.gamepari.hungryadventure.contents.HungryDatabase;
import com.gamepari.hungryadventure.contents.ModelFood;
import com.gamepari.hungryadventure.preferences.PreferenceIO;
import com.gamepari.hungryadventure.provider.FitData;
import com.gamepari.hungryadventure.provider.FitDataProvider;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.fitness.Fitness;

import java.util.ArrayList;
import java.util.List;


public class AdventureActivity extends ActionBarActivity implements View.OnClickListener {

    private static final String TAG = "AdventureActivity";
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    private static final String DIALOG_ERROR = "dialog_error";
    private static final String STATE_RESOLVING_ERROR = "resolving_error";
    private FoodsPagerAdapter mFoodPagerAdapter;
    private GoogleApiClient mGoogleApiClient;
    private boolean mResolvingError = false;
    private TextView tvSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mResolvingError = savedInstanceState != null
                && savedInstanceState.getBoolean(STATE_RESOLVING_ERROR, false);

        setContentView(R.layout.activity_adventure);

        tvSteps = (TextView) findViewById(R.id.tv_steps);

        buildGoogleClient();

        String countryName = PreferenceIO.loadPreference(this, PreferenceIO.KEY_COUNTRY);

        getSupportActionBar().setTitle("Hungry " + countryName);

        findViewById(R.id.btn_history).setOnClickListener(this);

        ViewPager viewPager = (ViewPager) findViewById(R.id.vpager_foods);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setPageMargin(-500);

        mFoodPagerAdapter = new FoodsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mFoodPagerAdapter);

        new DatabaseTask().execute("GET_FOOD", countryName);
    }

    private void buildGoogleClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Fitness.API)
                .addScope(Fitness.SCOPE_ACTIVITY_READ)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {

                        Log.d(TAG, "onConnected");

                        String startDate = PreferenceIO.loadPreference(AdventureActivity.this,
                                PreferenceIO.KEY_START_DATE);

                        new FitDataProvider(new FitDataProvider.OnDataProvideListener() {
                            @Override
                            public void onDataLoaded(Object o) {

                                List<FitData> listFitData = (List<FitData>) o;

                                int totalSteps = 0;
                                for (FitData fit : listFitData) {
                                    totalSteps += fit.getValue();
                                }

                                tvSteps.setText(String.valueOf(totalSteps) + " Steps");

                            }
                        }, mGoogleApiClient).execute(startDate);

                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                        Log.d(TAG, "onConnectionSuspended");

                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {

                        Log.i(TAG, "Connection failed. Cause: " + connectionResult.toString());

                        if (mResolvingError) {
                            return;
                        } else if (connectionResult.hasResolution()) {

                            try {
                                mResolvingError = true;
                                connectionResult.startResolutionForResult(AdventureActivity.this, REQUEST_RESOLVE_ERROR);
                            } catch (IntentSender.SendIntentException e) {
                                // There was an error with the resolution intent. Try again.
                                mGoogleApiClient.connect();
                            }
                        } else {
                            // Show the localized error dialog
                            showErrorDialog(connectionResult.getErrorCode());
                            mResolvingError = true;
                        }
                    }

                })
                .build();
    }

    private void showErrorDialog(int errorCode) {

        // Create a fragment for the error dialog
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getSupportFragmentManager(), "errordialog");
    }

    /* Called from ErrorDialogFragment when the dialog is dismissed. */
    public void onDialogDismissed() {
        mResolvingError = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mResolvingError) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_RESOLVE_ERROR) {

            mResolvingError = false;

            if (resultCode == RESULT_OK) {
                if (!mGoogleApiClient.isConnecting() && !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
            }

        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_RESOLVING_ERROR, mResolvingError);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_history:
                break;
        }
    }

    public void showUnlockDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new UnlockFragment();
        dialog.show(getSupportFragmentManager(), "UnlockFragment");
    }

    /* A fragment to display an error dialog */
    public static class ErrorDialogFragment extends DialogFragment {
        public ErrorDialogFragment() {
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get the error code and retrieve the appropriate dialog
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GooglePlayServicesUtil.getErrorDialog(errorCode,
                    this.getActivity(), REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            ((AdventureActivity) getActivity()).onDialogDismissed();
        }
    }

    private class DatabaseTask extends AsyncTask<String, Void, Object> {

        public static final String OPER_GET_FOOD = "GET_FOOD";
        public static final String OPER_UNLOCK_FOOD = "UNLOCK_FOOD";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(String... strings) {

            HungryDatabase hungryDatabase = new HungryDatabase(AdventureActivity.this, HungryDatabase.DB_HUNGRY, null, 1);

            switch (strings[0]) {
                case OPER_GET_FOOD:

                    String country = strings[1];
                    return hungryDatabase.getFoods(country);

                case OPER_UNLOCK_FOOD:

                    break;

            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            if (o == null) return;

            List<ModelFood> modelFoods = (List<ModelFood>) o;
            mFoodPagerAdapter.setDatas(modelFoods);

        }
    }

    private class FoodsPagerAdapter extends FragmentStatePagerAdapter {

        List<Fragment> listFragments;

        private FoodsPagerAdapter(FragmentManager fm) {
            super(fm);

            listFragments = new ArrayList<>();

            //you have eaten 56% of seoul.

        }

        public void setDatas(List<ModelFood> listFoods) {

            for (ModelFood food : listFoods) {
                listFragments.add(FoodPageFragment.newInstance(food));
            }

            notifyDataSetChanged();
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
