package it.unipr.advmobdev.mat301275.facemorph.opencv;

import android.content.Context;
import android.graphics.Bitmap;

public interface ProgressCallback {
    void triangleCalcolated(Bitmap bitmap);
    void imageCalcolated(Bitmap bitmap);
    Context getContext();
    double getAlpha();
    int getIterations();
}
