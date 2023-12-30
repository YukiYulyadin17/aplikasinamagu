package com.skripsi.aplikasinamagu;

import android.os.Parcel;
import android.os.Parcelable;

public class HamaModel implements Parcelable {
    private int _id;
    private String namahama;
    private String detailhama;
    private String imagepathhama;

    public HamaModel(int id, String namahama, String detailhama, String imagepathhama) {
        this._id = id;
        this.namahama = namahama;
        this.detailhama = detailhama;
        this.imagepathhama = imagepathhama;
    }

    public int getId() {
        return _id;
    }

    public String getnamahama() {
        return namahama;
    }

    public String getDetailhama() {
        return detailhama;
    }

    public String getimagepathhama() {
        return imagepathhama;
    }

    // Parcelable implementation
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeString(namahama);
        dest.writeString(detailhama);
        dest.writeString(imagepathhama);
    }

    protected HamaModel(Parcel in) {
        _id = in.readInt();
        namahama = in.readString();
        detailhama = in.readString();
        imagepathhama = in.readString();
    }

    public static final Parcelable.Creator<HamaModel> CREATOR = new Parcelable.Creator<HamaModel>() {
        @Override
        public HamaModel createFromParcel(Parcel source) {
            return new HamaModel(source);
        }

        @Override
        public HamaModel[] newArray(int size) {
            return new HamaModel[size];
        }
    };
}
