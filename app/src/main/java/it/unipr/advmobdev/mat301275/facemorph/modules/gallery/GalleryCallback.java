package it.unipr.advmobdev.mat301275.facemorph.modules.gallery;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public interface GalleryCallback extends Parcelable {
        void imageSelected(Bitmap bitmap);

        @Override
        default int describeContents() {
                return 0;
        }

        @Override
        default void writeToParcel(Parcel dest, int flags) { }
}
