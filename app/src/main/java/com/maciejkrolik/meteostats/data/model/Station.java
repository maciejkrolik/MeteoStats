package com.maciejkrolik.meteostats.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

@Entity
public class Station implements Parcelable {

    @PrimaryKey
    private int no;

    private String name;

    @SerializedName("rain")
    private boolean hasRain;

    @SerializedName("water")
    private boolean hasWater;

    @SerializedName("winddir")
    private boolean hasWindDir;

    @SerializedName("windlevel")
    private boolean hasWindLevel;

    private boolean isSaved;

    public Station(int no, String name, boolean hasRain, boolean hasWater,
                   boolean hasWindDir, boolean hasWindLevel, boolean isSaved) {
        this.no = no;
        this.name = name;
        this.hasRain = hasRain;
        this.hasWater = hasWater;
        this.hasWindDir = hasWindDir;
        this.hasWindLevel = hasWindLevel;
        this.isSaved = isSaved;
    }

    public int getNo() {
        return no;
    }

    public String getName() {
        return name;
    }

    public boolean hasRain() {
        return hasRain;
    }

    public boolean hasWater() {
        return hasWater;
    }

    public boolean hasWindDir() {
        return hasWindDir;
    }

    public boolean hasWindLevel() {
        return hasWindLevel;
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

    public void setHasRain(boolean hasRain) {
        this.hasRain = hasRain;
    }

    public void setHasWater(boolean hasWater) {
        this.hasWater = hasWater;
    }

    public void setHasWindDir(boolean hasWindDir) {
        this.hasWindDir = hasWindDir;
    }

    public void setHasWindLevel(boolean hasWindLevel) {
        this.hasWindLevel = hasWindLevel;
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
        parcel.writeByte((byte) (hasRain ? 1 : 0));
        parcel.writeByte((byte) (hasWater ? 1 : 0));
        parcel.writeByte((byte) (hasWindDir ? 1 : 0));
        parcel.writeByte((byte) (hasWindLevel ? 1 : 0));
        parcel.writeByte((byte) (isSaved ? 1 : 0));
    }

    public static final Creator<Station> CREATOR = new Creator<Station>() {
        @Override
        public Station createFromParcel(Parcel parcel) {
            int no = parcel.readInt();
            String name = parcel.readString();
            Boolean hasRain = parcel.readByte() != 0;
            Boolean hasWater = parcel.readByte() != 0;
            Boolean hasWindDir = parcel.readByte() != 0;
            Boolean hasWindLevel = parcel.readByte() != 0;
            Boolean isSaved = parcel.readByte() != 0;
            return new Station(no, name, hasRain, hasWater, hasWindDir, hasWindLevel, isSaved);
        }

        @Override
        public Station[] newArray(int i) {
            return new Station[0];
        }
    };
}
