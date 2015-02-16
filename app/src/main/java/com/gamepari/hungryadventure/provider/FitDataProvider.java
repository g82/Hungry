package com.gamepari.hungryadventure.provider;

import android.os.AsyncTask;
import android.text.format.Time;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessStatusCodes;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResult;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by gamepari on 1/8/15.
 */
public class FitDataProvider extends AsyncTask<String, Void, Object> {

    private static final String TAG = "FitDataProvider";

    private static final String DATE_FORMAT = "yyyy.MM.dd HH:mm:ss";

    private OnDataProvideListener mOnDataProvideListener;
    private GoogleApiClient mApiClient;

    public FitDataProvider(OnDataProvideListener mOnDataProvideListener, GoogleApiClient googleApiClient) {
        this.mOnDataProvideListener = mOnDataProvideListener;
        this.mApiClient = googleApiClient;
    }

    @Override
    protected Object doInBackground(String... params) {
        //subscribe();
        //requestData();

        String startDate = params[0];

        com.google.android.gms.common.api.Status status =
                Fitness.RecordingApi.subscribe(mApiClient, DataType.TYPE_STEP_COUNT_DELTA).await();

        if (status.isSuccess()) {
            if (status.getStatusCode()
                    == FitnessStatusCodes.SUCCESS_ALREADY_SUBSCRIBED) {
                Log.i(TAG, "Existing subscription for activity detected.");
            } else {
                Log.i(TAG, "Successfully subscribed!");
            }

            DataReadResult result = Fitness.HistoryApi.readData(mApiClient, makeReadRequest(startDate)).await();
            printData(result);
            FitDataAdapter fitDataAdapter = new FitDataAdapter(result);
            return fitDataAdapter.convertFitData();


        } else {
            Log.i(TAG, "There was a problem subscribing.");
            return null;
        }
    }

    private void requestData() {
        PendingResult<DataReadResult> dataReadPendingResult =
                (PendingResult<DataReadResult>) Fitness.HistoryApi.readData(mApiClient, makeReadRequest()).await();

        dataReadPendingResult.setResultCallback(new ResultCallback<DataReadResult>() {
            @Override
            public void onResult(DataReadResult dataReadResult) {

                printData(dataReadResult);
                FitDataAdapter fitDataAdapter = new FitDataAdapter(dataReadResult);
                mOnDataProvideListener.onDataLoaded(fitDataAdapter.convertFitData());

            }
        });

    }

    /**
     * Subscribe to an available {@link com.google.android.gms.fitness.data.DataType}. Subscriptions can exist across application
     * instances (so data is recorded even after the application closes down).  When creating
     * a new subscription, it may already exist from a previous invocation of this app.  If
     * the subscription already exists, the method is a no-op.  However, you can check this with
     * a special success code.
     */
    public void subscribe() {
        // To create a subscription, invoke the Recording API. As soon as the subscription is
        // active, fitness data will start recording.
        // [START subscribe_to_datatype]

        PendingResult<com.google.android.gms.common.api.Status> subscribeResult =
                Fitness.RecordingApi.subscribe(mApiClient, DataType.TYPE_STEP_COUNT_DELTA);
        subscribeResult.setResultCallback(new ResultCallback<com.google.android.gms.common.api.Status>() {
            @Override
            public void onResult(com.google.android.gms.common.api.Status status) {

                if (status.isSuccess()) {
                    if (status.getStatusCode()
                            == FitnessStatusCodes.SUCCESS_ALREADY_SUBSCRIBED) {
                        Log.i(TAG, "Existing subscription for activity detected.");
                    } else {
                        Log.i(TAG, "Successfully subscribed!");
                    }
                } else {
                    Log.i(TAG, "There was a problem subscribing.");
                }

            }
        });
        // [END subscribe_to_datatype]
    }


    /**
     * @param startDate = "YYYYMMDDTHHMMSS"
     */

    private DataReadRequest makeReadRequest(String startDate) {

        Log.d("startDate", startDate);

        Time time = new Time();
        time.set(Long.parseLong(startDate));

        long startLong = time.normalize(true);
        time.setToNow();

        long endLong = time.normalize(true);


        /*
        // [START build_read_data_request]
        // Setting a start and end date using a range of 1 week before this moment.
        Calendar cal = Calendar.getInstance();
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.DAY_OF_YEAR, -2);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long startTime = cal.getTimeInMillis();
        */

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        Log.i(TAG, "Range Start: " + dateFormat.format(startLong));
        Log.i(TAG, "Range End: " + dateFormat.format(endLong));

        DataReadRequest readRequest = new DataReadRequest.Builder()
                // The data request can specify multiple data types to return, effectively
                // combining multiple data queries into one call.
                // In this example, it's very unlikely that the request is for several hundred
                // datapoints each consisting of a few steps and a timestamp.  The more likely
                // scenario is wanting to see how many steps were walked per day, for 7 days.
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                        // Analogous to a "Group By" in SQL, defines how data should be aggregated.
                        // bucketByTime allows for a time span, whereas bucketBySession would allow
                        // bucketing by "sessions", which would need to be defined in code.
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startLong, endLong, TimeUnit.MILLISECONDS)
                .build();
        // [END build_read_data_request]

        return readRequest;
    }

    private DataReadRequest makeReadRequest() {

        // [START build_read_data_request]
        // Setting a start and end date using a range of 1 week before this moment.
        Calendar cal = Calendar.getInstance();
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.DAY_OF_YEAR, -2);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long startTime = cal.getTimeInMillis();

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        Log.i(TAG, "Range Start: " + dateFormat.format(startTime));
        Log.i(TAG, "Range End: " + dateFormat.format(endTime));

        DataReadRequest readRequest = new DataReadRequest.Builder()
                // The data request can specify multiple data types to return, effectively
                // combining multiple data queries into one call.
                // In this example, it's very unlikely that the request is for several hundred
                // datapoints each consisting of a few steps and a timestamp.  The more likely
                // scenario is wanting to see how many steps were walked per day, for 7 days.
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                        // Analogous to a "Group By" in SQL, defines how data should be aggregated.
                        // bucketByTime allows for a time span, whereas bucketBySession would allow
                        // bucketing by "sessions", which would need to be defined in code.
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();
        // [END build_read_data_request]

        return readRequest;
    }


    private void printData(DataReadResult dataReadResult) {
        // [START parse_read_data_result]
        // If the DataReadRequest object specified aggregated data, dataReadResult will be returned
        // as buckets containing DataSets, instead of just DataSets.
        if (dataReadResult.getBuckets().size() > 0) {
            Log.i(TAG, "Number of returned buckets of DataSets is: "
                    + dataReadResult.getBuckets().size());
            for (Bucket bucket : dataReadResult.getBuckets()) {
                List<DataSet> dataSets = bucket.getDataSets();
                for (DataSet dataSet : dataSets) {
                    dumpDataSet(dataSet);
                }
            }
        } else if (dataReadResult.getDataSets().size() > 0) {
            Log.i(TAG, "Number of returned DataSets is: "
                    + dataReadResult.getDataSets().size());
            for (DataSet dataSet : dataReadResult.getDataSets()) {
                dumpDataSet(dataSet);
            }
        }
        // [END parse_read_data_result]
    }

    // [START parse_dataset]
    private void dumpDataSet(DataSet dataSet) {
        Log.i(TAG, "Data returned for Data type: " + dataSet.getDataType().getName());
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

        for (DataPoint dp : dataSet.getDataPoints()) {
            Log.i(TAG, "Data point:");
            Log.i(TAG, "\tType: " + dp.getDataType().getName());
            Log.i(TAG, "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            Log.i(TAG, "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
            for (Field field : dp.getDataType().getFields()) {
                Log.i(TAG, "\tField: " + field.getName() +
                        " Value: " + dp.getValue(field));
            }
        }
    }
    // [END parse_dataset]

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        mOnDataProvideListener.onDataLoaded(o);
    }

    public interface OnDataProvideListener {
        public void onDataLoaded(Object o);
    }


}
