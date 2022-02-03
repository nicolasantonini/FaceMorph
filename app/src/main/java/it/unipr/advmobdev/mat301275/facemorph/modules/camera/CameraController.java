package it.unipr.advmobdev.mat301275.facemorph.modules.camera;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.lang.ref.WeakReference;

import it.unipr.advmobdev.mat301275.facemorph.modules.home.HomeFragment;

public class CameraController {

    private CameraCallback callback = null;
    private WeakReference<CameraFragment> weakFragment = null;

    public CameraController(CameraFragment fragment) {
        weakFragment = new WeakReference<>(fragment);
    }

    public void setCallback(CameraCallback callback) {
        this.callback = callback;
    }

    public void cameraPermissionAccepted() {
        CameraFragment fragment = weakFragment.get();
        if (fragment != null) {
            fragment.enableCamera();
        }
    }

    public void cameraPermissionDenied() {
        CameraFragment fragment = weakFragment.get();
        if (fragment != null) {
            fragment.quit();
        }
    }

    public void photoTaken(Mat mat) {
        final Bitmap bitmap = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.RGB_565);
        Utils.matToBitmap(mat, bitmap);
        callback.imageTaken(bitmap);
    }

}
