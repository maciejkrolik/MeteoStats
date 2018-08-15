package com.maciejkrolik.meteostats.data.model;

public class Station {

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
}
