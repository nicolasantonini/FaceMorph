package it.unipr.advmobdev.mat301275.facemorph.modules.camera;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public interface CameraCallback extends Parcelable {
    void imageTaken(Bitmap bitmap);

    @Override
    default int describeContents() {
        return 0;
    }

    @Override
    default void writeToParcel(Parcel dest, int flags) { }
}
