package com.gamepari.hungryadventure.foods;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by gamepari on 2/14/15.
 */
public class ModelFood implements Serializable {

    private int mIndex;
    private String mName;
    private int mUnlockStepCount;
    private Date mUnlockDate;
    private int mImageResourceId;

    public ModelFood(int mIndex, String mName, int mUnlockStepCount, Date mUnlockDate, int mImageResourceId) {
        this.mIndex = mIndex;
        this.mName = mName;
        this.mUnlockStepCount = mUnlockStepCount;
        this.mUnlockDate = mUnlockDate;
        this.mImageResourceId = mImageResourceId;
    }

    public int getmIndex() {
        return mIndex;
    }

    public String getmName() {
        return mName;
    }

    public int getmUnlockStepCount() {
        return mUnlockStepCount;
    }

    public Date getmUnlockDate() {
        return mUnlockDate;
    }

    public int getmImageResourceId() {
        return mImageResourceId;
    }
}
