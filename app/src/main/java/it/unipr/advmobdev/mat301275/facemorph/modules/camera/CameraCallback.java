package it.unipr.advmobdev.mat301275.facemorph.modules.camera;

import android.graphics.Bitmap;
import android.os.Parcelable;

public interface CameraCallback extends Parcelable {
    void imageTaken(Bitmap bitmap);
}
