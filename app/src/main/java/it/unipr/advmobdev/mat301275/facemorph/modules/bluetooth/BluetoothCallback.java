package it.unipr.advmobdev.mat301275.facemorph.modules.bluetooth;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public interface BluetoothCallback extends Parcelable {
    void bleRetrieveSuccess(Bitmap bitmap);
    void bleRetrieveFailed(Exception e);
    void bleRetrieveUpdatedStatus(int newStatus);
    void bleRetrieveUpdatedProgress(int progress);

    @Override
    default int describeContents() {
        return 0;
    }

    @Override
    default void writeToParcel(Parcel dest, int flags) { }
}