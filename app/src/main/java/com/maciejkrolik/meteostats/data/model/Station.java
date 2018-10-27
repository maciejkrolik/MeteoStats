package com.maciejkrolik.meteostats.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity
public class Station implements Parcelable {

    @PrimaryKey
    private int no;
    private String name;
    private boolean rain;
    private boolean water;
    private boolean winddir;
    private boolean windlevel;
    private boolean isSaved;

    public Station(int no, String name, boolean rain, boolean water,
                   boolean winddir, boolean windlevel, boolean isSaved) {
        this.no = no;
        this.name = name;
        this.rain = rain;
        this.water = water;
        this.winddir = winddir;
        this.windlevel = windlevel;
        this.isSaved = isSaved;
    }

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

    public boolean isSaved() {
        return isSaved;
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

    public void setSaved(boolean saved) {
        isSaved = saved;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(no);
        parcel.writeString(name);
        parcel.writeByte((byte) (rain ? 1 : 0));
        parcel.writeByte((byte) (water ? 1 : 0));
        parcel.writeByte((byte) (winddir ? 1 : 0));
        parcel.writeByte((byte) (windlevel ? 1 : 0));
        parcel.writeByte((byte) (isSaved ? 1 : 0));
    }

    public static final Creator<Station> CREATOR = new Creator<Station>() {
        @Override
        public Station createFromParcel(Parcel parcel) {
            int no = parcel.readInt();
            String name = parcel.readString();
            Boolean rain = parcel.readByte() != 0;
            Boolean water = parcel.readByte() != 0;
            Boolean winddir = parcel.readByte() != 0;
            Boolean windlevel = parcel.readByte() != 0;
            Boolean isSaved = parcel.readByte() != 0;
            return new Station(no, name, rain, water, winddir, windlevel, isSaved);
        }

        @Override
        public Station[] newArray(int i) {
            return new Station[0];
        }
    };
}
