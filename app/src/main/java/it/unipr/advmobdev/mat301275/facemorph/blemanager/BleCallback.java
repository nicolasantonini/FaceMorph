package it.unipr.advmobdev.mat301275.facemorph.blemanager;

import android.graphics.Bitmap;

public interface BleCallback {
    void bleSuccess(Bitmap bitmap);
    void bleFailed(Exception exception);
    void bleProgress(int progress);
}
