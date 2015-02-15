package com.gamepari.hungryadventure.country;

/**
 * Created by gamepari on 2/14/15.
 */
public class ModelCountry {

    private String name;
    private int thumbnail_id;
    private boolean isLocked;

    public ModelCountry(String name, int thumbnail_id, boolean isLocked) {
        this.name = name;
        this.thumbnail_id = thumbnail_id;
        this.isLocked = isLocked;
    }

    public String getName() {
        return name;
    }

    public int getThumbnail_id() {
        return thumbnail_id;
    }

    public boolean isLocked() {
        return isLocked;
    }
}
