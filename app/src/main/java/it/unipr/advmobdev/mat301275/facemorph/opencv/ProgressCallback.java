package it.unipr.advmobdev.mat301275.facemorph.opencv;

import android.content.Context;
import android.graphics.Bitmap;

import org.opencv.core.Mat;

public interface ProgressCallback {
    void triangleCalcolated(Bitmap bitmap);
    void imageCalcolated(Bitmap bitmap);
    Context getContext();
    double getAlpha();
    int getIterations();
    Bitmap getBitmap1();
    Bitmap getBitmap2();
}
