package com.shubhu.gallerylib.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Ranosys Technologies
 */

public class GalleryModel implements Parcelable {
    private String imagePath;
    private boolean isSelected;

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imagePath);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }

    public GalleryModel() {
    }

    protected GalleryModel(Parcel in) {
        this.imagePath = in.readString();
        this.isSelected = in.readByte() != 0;
    }

    public static final Creator<GalleryModel> CREATOR = new Creator<GalleryModel>() {
        @Override
        public GalleryModel createFromParcel(Parcel source) {
            return new GalleryModel(source);
        }

        @Override
        public GalleryModel[] newArray(int size) {
            return new GalleryModel[size];
        }
    };
}
