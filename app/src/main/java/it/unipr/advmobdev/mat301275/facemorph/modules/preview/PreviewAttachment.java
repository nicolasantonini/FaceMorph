package it.unipr.advmobdev.mat301275.facemorph.modules.preview;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class PreviewAttachment implements Parcelable {

    private Bitmap bitmap;

    public PreviewAttachment(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    protected PreviewAttachment(Parcel in) {
    }

    public static final Creator<PreviewAttachment> CREATOR = new Creator<PreviewAttachment>() {
        @Override
        public PreviewAttachment createFromParcel(Parcel in) {
            return new PreviewAttachment(in);
        }

        @Override
        public PreviewAttachment[] newArray(int size) {
            return new PreviewAttachment[size];
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
