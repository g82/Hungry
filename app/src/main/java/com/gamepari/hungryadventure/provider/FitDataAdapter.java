package com.gamepari.hungryadventure.provider;

import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.result.DataReadResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by gamepari on 1/10/15.
 */
public class FitDataAdapter {

    private DataReadResult mDataReadResult;

    public FitDataAdapter(DataReadResult dataReadResult) {
        mDataReadResult = dataReadResult;
    }

    public List<FitData> convertFitData() {

        List<FitData> listFitData = new ArrayList<>();

        for (Bucket bucket : mDataReadResult.getBuckets()) {
            for (DataSet dataSet : bucket.getDataSets()) {
                for (DataPoint dataPoint : dataSet.getDataPoints()) {

                    FitData fitData = new FitData();

                    //fitData.setType(dataPoint.getDataType().getName());
                    fitData.setStartTime(dataPoint.getStartTime(TimeUnit.MILLISECONDS));
                    fitData.setEndTime(dataPoint.getEndTime(TimeUnit.MILLISECONDS));
                    fitData.setValue(dataPoint.getValue(Field.FIELD_STEPS).asInt());

                    listFitData.add(fitData);
                }
            }
        }

        return listFitData;
    }
}
