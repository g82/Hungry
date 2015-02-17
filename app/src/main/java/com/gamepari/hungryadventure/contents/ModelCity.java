package com.gamepari.hungryadventure.contents;

/**
 * Created by gamepari on 2/14/15.
 */
public class ModelCity {

    private int id;
    private String local_name;
    private String eng_name;
    private String mimgPath;
    private boolean isLocked;

    public ModelCity(int id, String local_name, String eng_name, String mimgPath, boolean isLocked) {
        this.id = id;
        this.local_name = local_name;
        this.eng_name = eng_name;
        this.mimgPath = mimgPath;
        this.isLocked = isLocked;
    }

    public ModelCity(String local_name, boolean isLocked, String mimgPath) {
        this.local_name = local_name;
        this.isLocked = isLocked;
        this.mimgPath = mimgPath;
    }

    public int getId() {
        return id;
    }

    public String getLocal_name() {
        return local_name;
    }

    public String getEng_name() {
        return eng_name;
    }

    public String getMimgPath() {
        return mimgPath;
    }

    public boolean isUnlocked() {
        return isLocked;
    }
}
