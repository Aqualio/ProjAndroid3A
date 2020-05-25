package com.example.projandroid3a;

import android.os.Parcel;
import android.os.Parcelable;

public class NierCharacter implements Parcelable {
    private String name;
    private String imgUrl;
    private String faction;
    private String bio;

    protected NierCharacter(Parcel in) {
        name = in.readString();
        imgUrl = in.readString();
        faction = in.readString();
        bio = in.readString();
    }

    public static final Creator<NierCharacter> CREATOR = new Creator<NierCharacter>() {
        @Override
        public NierCharacter createFromParcel(Parcel in) {
            return new NierCharacter(in);
        }

        @Override
        public NierCharacter[] newArray(int size) {
            return new NierCharacter[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getFaction() {
        return faction;
    }

    public String getBio(){
        return bio;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(imgUrl);
        dest.writeString(faction);
        dest.writeString(bio);
    }
}
