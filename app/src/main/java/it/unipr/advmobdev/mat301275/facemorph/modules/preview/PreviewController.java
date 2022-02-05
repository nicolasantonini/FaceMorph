package it.unipr.advmobdev.mat301275.facemorph.modules.preview;

import android.graphics.Bitmap;

import java.lang.ref.WeakReference;

import it.unipr.advmobdev.mat301275.facemorph.modules.camera.CameraFragment;


public class PreviewController {

    private WeakReference<PreviewFragment> weakFragment = null;
    private Bitmap bitmap;

    public PreviewController(PreviewFragment fragment) {
        weakFragment = new WeakReference<>(fragment);
    }

    public void viewCreated() {
        PreviewFragment fragment = weakFragment.get();
        if (fragment != null) {
            fragment.setImage(this.bitmap);
        }
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void cameraClicked() {

    }

    public void galleryClicked() {

    }

    public void bluetoothClicked() {

    }

}
