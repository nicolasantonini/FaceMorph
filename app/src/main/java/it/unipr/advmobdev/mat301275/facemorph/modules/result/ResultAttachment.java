package it.unipr.advmobdev.mat301275.facemorph.modules.result;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class ResultAttachment implements Parcelable {

    private Bitmap bitmapOne;
    private Bitmap bitmapTwo;

    public ResultAttachment(Bitmap bitmapOne, Bitmap bitmapTwo) {
        this.bitmapOne = bitmapOne;
        this.bitmapTwo = bitmapTwo;
    }

    public Bitmap getBitmapOne() {
        return bitmapOne;
    }

    public Bitmap getBitmapTwo() {
        return bitmapTwo;
    }

    protected ResultAttachment(Parcel in) {
    }

    public static final Creator<ResultAttachment> CREATOR = new Creator<ResultAttachment>() {
        @Override
        public ResultAttachment createFromParcel(Parcel in) {
            return new ResultAttachment(in);
        }

        @Override
        public ResultAttachment[] newArray(int size) {
            return new ResultAttachment[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}

