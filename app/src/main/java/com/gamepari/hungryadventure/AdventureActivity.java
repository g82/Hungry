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


public class AdventureActivity extends ActionBarActivity implements View.OnClickListener, UnlockDialogFragment.UnlockDialogListener {

    private static final String TAG = "AdventureActivity";
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    private static final String DIALOG_ERROR = "dialog_error";
    private static final String STATE_RESOLVING_ERROR = "resolving_error";
    private FoodsPagerAdapter mFoodPagerAdapter;
    private GoogleApiClient mGoogleApiClient;
    private boolean mResolvingError = false;
    private TextView tvSteps;
    private TextView tvTotalSteps;

    private String cityName;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mResolvingError = savedInstanceState != null
                && savedInstanceState.getBoolean(STATE_RESOLVING_ERROR, false);

        setContentView(R.layout.activity_adventure);

        tvSteps = (TextView) findViewById(R.id.tv_steps);
        tvTotalSteps = (TextView) findViewById(R.id.tv_totalsteps);

        buildGoogleClient();

        cityName = PreferenceIO.loadPreference(this, PreferenceIO.KEY_COUNTRY);

        getSupportActionBar().setTitle("Hungry " + cityName);

        findViewById(R.id.btn_history).setOnClickListener(this);

        mViewPager = (ViewPager) findViewById(R.id.vpager_foods);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setPageMargin(-500);
    }

    @Override
    public void onDialogPositiveClick(ModelFood food) {
        //TODO update data

        int needPoint = food.getmRequiredStepCount();
        int availpoint = PreferenceIO.loadAvailCount(this, PreferenceIO.KEY_AVAIL_STEPS);

        PreferenceIO.saveAvailCount(this, PreferenceIO.KEY_AVAIL_STEPS, availpoint - needPoint);

        int remainPoint = PreferenceIO.loadAvailCount(this, PreferenceIO.KEY_AVAIL_STEPS);

        tvSteps.setText("Available step point : " + String.valueOf(remainPoint));

        String usedStep = PreferenceIO.loadPreference(this, PreferenceIO.KEY_USED_STEPS);

        if (usedStep == null) usedStep = "0";

        int usedstp = Integer.valueOf(usedStep);

        usedstp += needPoint;

        PreferenceIO.savePreference(this, PreferenceIO.KEY_USED_STEPS, String.valueOf(usedstp));

        new DatabaseTask().execute(DatabaseTask.OPER_UNLOCK_FOOD, food.getmName_eng());
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
    }

    private void loadFitData() {
        String startDate = PreferenceIO.loadPreference(AdventureActivity.this,
                PreferenceIO.KEY_START_DATE);

        findViewById(R.id.fl_loading).setVisibility(View.GONE);

        new FitDataProvider(new FitDataProvider.OnDataProvideListener() {
            @Override
            public void onDataLoaded(Object o) {

                List<FitData> listFitData = (List<FitData>) o;

                int totalSteps = 0;
                for (FitData fit : listFitData) {
                    totalSteps += fit.getValue();
                }

                totalSteps = (totalSteps > 0) ? totalSteps : 0;

                String usedSteps = PreferenceIO.loadPreference(AdventureActivity.this, PreferenceIO.KEY_USED_STEPS);
                if (usedSteps == null) usedSteps = "0";
                int availstep = totalSteps - Integer.valueOf(usedSteps);

                PreferenceIO.saveAvailCount(AdventureActivity.this, PreferenceIO.KEY_AVAIL_STEPS, availstep);

                availstep = PreferenceIO.loadAvailCount(AdventureActivity.this, PreferenceIO.KEY_AVAIL_STEPS);

                tvSteps.setText("Available step point : " + String.valueOf(availstep));
                tvTotalSteps.setText("Total step point in " + cityName + " : " + String.valueOf(totalSteps));


            }
        }, mGoogleApiClient).execute(startDate);

    }

    private void buildGoogleClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Fitness.API)
                .addScope(Fitness.SCOPE_ACTIVITY_READ)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {

                        Log.d(TAG, "onConnected");

                        loadFitData();


                        new DatabaseTask().execute(DatabaseTask.OPER_GET_FOOD, cityName);

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
            } else {
                mGoogleApiClient.connect();
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

    public void showUnlockDialog(ModelFood modelFood) {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new UnlockDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("food", modelFood);
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "UnlockDialogFragment");
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

        private String operation_type;
        private String eng_name;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(String... strings) {

            operation_type = strings[0];

            HungryDatabase hungryDatabase = new HungryDatabase(AdventureActivity.this, HungryDatabase.DB_HUNGRY, null, 1);

            switch (strings[0]) {
                case OPER_GET_FOOD:
                    String country = strings[1];
                    return hungryDatabase.getFoods(country);

                case OPER_UNLOCK_FOOD:
                    eng_name = strings[1];
                    return hungryDatabase.unlockFood(eng_name);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            switch (operation_type) {
                case OPER_GET_FOOD:
                    if (o == null) return;

                    int page = mViewPager.getCurrentItem();
                    List<ModelFood> modelFoods = (List<ModelFood>) o;
                    mFoodPagerAdapter = new FoodsPagerAdapter(getSupportFragmentManager(), modelFoods);
                    mViewPager.setAdapter(mFoodPagerAdapter);
                    mViewPager.setCurrentItem(page);
                    break;

                case OPER_UNLOCK_FOOD:
                    new DatabaseTask().execute(OPER_GET_FOOD, cityName);
                    break;
            }
        }
    }

    private class FoodsPagerAdapter extends FragmentStatePagerAdapter {

        List<Fragment> listFragments;
        List<ModelFood> mListFoods;

        private FoodsPagerAdapter(FragmentManager fm, List<ModelFood> mListFoods) {
            super(fm);

            listFragments = new ArrayList<>();

            this.mListFoods = mListFoods;

            for (ModelFood food : mListFoods) {
                listFragments.add(FoodPageFragment.newInstance(food));
            }

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
