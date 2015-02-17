package com.gamepari.hungryadventure.contents;

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
    private Time mUnlockTime;
    private int mCityCode;
    private int mCalories;
    private int mCost;

    public ModelFood(int mId, String mName_local, String mName_eng, int mRequiredStepCount, String mAssetImagePath, Time mUnlockTime, int mCityCode) {
        this.mId = mId;
        this.mName_local = mName_local;
        this.mName_eng = mName_eng;
        this.mRequiredStepCount = mRequiredStepCount;
        this.mAssetImagePath = mAssetImagePath;
        this.mUnlockTime = mUnlockTime;
        this.mCityCode = mCityCode;
    }

    public ModelFood(int mId, String mName_local, String mName_eng, int mRequiredStepCount, String mAssetImagePath, Time mUnlockTime, int mCityCode, int mCalories, int mCost) {
        this.mId = mId;
        this.mName_local = mName_local;
        this.mName_eng = mName_eng;
        this.mRequiredStepCount = mRequiredStepCount;
        this.mAssetImagePath = mAssetImagePath;
        this.mUnlockTime = mUnlockTime;
        this.mCityCode = mCityCode;
        this.mCalories = mCalories;
        this.mCost = mCost;
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

    public Time getmUnlockTime() {
        return mUnlockTime;
    }

    public void setmUnlockTime(Time mUnlockTime) {
        this.mUnlockTime = mUnlockTime;
    }

    public String getmName_eng() {
        return mName_eng;
    }

    public int getmCityCode() {
        return mCityCode;
    }
}
