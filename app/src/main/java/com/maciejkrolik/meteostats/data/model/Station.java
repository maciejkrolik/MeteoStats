package com.maciejkrolik.meteostats.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Station {

    @PrimaryKey
    private int no;
    private String name;
    private boolean rain;
    private boolean water;
    private boolean winddir;
    private boolean windlevel;

    public int getNo() {
        return no;
    }

    public String getName() {
        return name;
    }

    public boolean isRain() {
        return rain;
    }

    public boolean isWater() {
        return water;
    }

    public boolean isWinddir() {
        return winddir;
    }

    public boolean isWindlevel() {
        return windlevel;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRain(boolean rain) {
        this.rain = rain;
    }

    public void setWater(boolean water) {
        this.water = water;
    }

    public void setWinddir(boolean winddir) {
        this.winddir = winddir;
    }

    public void setWindlevel(boolean windlevel) {
        this.windlevel = windlevel;
    }
}
