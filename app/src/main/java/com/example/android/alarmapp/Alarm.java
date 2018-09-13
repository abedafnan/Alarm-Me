package com.example.android.alarmapp;

/**
 * Created by Afnan A. A. Abed on 9/8/2018.
 */

public class Alarm {
    private long time;
    private int isEnabled;
    private long id;

    public Alarm(long time, int isEnabled) {
        this.time = time;
        this.isEnabled = isEnabled;
    }

    public Alarm(long time, int isEnabled, long id) {
        this.time = time;
        this.isEnabled = isEnabled;
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int isEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(int isEnabled) {
        this.isEnabled = isEnabled;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
