package it.unipr.advmobdev.mat301275.facemorph.modules.camera;

import java.lang.ref.WeakReference;

public class CameraController {

    private CameraCallback callback = null;
    private WeakReference<CameraFragment> weakFragment = null;

    public CameraController(CameraFragment fragment) {
        weakFragment = new WeakReference<>(fragment);
    }

    public void setCallback(CameraCallback callback) {
        this.callback = callback;
    }

}
