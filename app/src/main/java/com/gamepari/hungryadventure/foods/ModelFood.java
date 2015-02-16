package com.gamepari.hungryadventure.foods;

import android.text.format.Time;

import java.io.Serializable;

/**
 * Created by gamepari on 2/14/15.
 */
public class ModelFood implements Serializable {

    private int mId;
    private String mName_local;
    private String mName_eng;
    private int mRequiredStepCount;
    private String mAssetImagePath;
    private Time mUnlockDate;
    private int mCountryCode;

    public ModelFood(int mId, String mName_local, String mName_eng, int mRequiredStepCount, String mAssetImagePath, Time mUnlockDate, int mCountryCode) {
        this.mId = mId;
        this.mName_local = mName_local;
        this.mName_eng = mName_eng;
        this.mRequiredStepCount = mRequiredStepCount;
        this.mAssetImagePath = mAssetImagePath;
        this.mUnlockDate = mUnlockDate;
        this.mCountryCode = mCountryCode;
    }

    public String getmAssetImagePath() {
        return mAssetImagePath;
    }

    public int getmId() {
        return mId;
    }

    public String getmName_local() {
        return mName_local;
    }

    public int getmRequiredStepCount() {
        return mRequiredStepCount;
    }

    public Time getmUnlockDate() {
        return mUnlockDate;
    }

    public void setmUnlockDate(Time mUnlockDate) {
        this.mUnlockDate = mUnlockDate;
    }

    public String getmName_eng() {
        return mName_eng;
    }

    public int getmCountryCode() {
        return mCountryCode;
    }
}
