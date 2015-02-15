package com.gamepari.hungryadventure.country;

/**
 * Created by gamepari on 2/14/15.
 */
public class ModelCountry {

    private String name;
    private boolean isLocked;
    private String mAssetPath;

    public ModelCountry(String name, boolean isLocked, String mAssetPath) {
        this.name = name;
        this.isLocked = isLocked;
        this.mAssetPath = mAssetPath;
    }

    public String getName() {
        return name;
    }

    public String getmAssetPath() {
        return mAssetPath;
    }

    public boolean isLocked() {
        return isLocked;
    }
}
