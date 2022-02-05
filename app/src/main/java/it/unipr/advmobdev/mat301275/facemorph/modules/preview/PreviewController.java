package it.unipr.advmobdev.mat301275.facemorph.modules.preview;

import android.graphics.Bitmap;

import java.lang.ref.WeakReference;

import it.unipr.advmobdev.mat301275.facemorph.modules.bluetooth.BluetoothCallback;
import it.unipr.advmobdev.mat301275.facemorph.modules.camera.CameraCallback;
import it.unipr.advmobdev.mat301275.facemorph.modules.result.ResultAttachment;


public class PreviewController {

    private WeakReference<PreviewFragment> weakFragment = null;
    private Bitmap userBitmap;

    public PreviewController(PreviewFragment fragment) {
        weakFragment = new WeakReference<>(fragment);
    }

    public void viewCreated() {
        PreviewFragment fragment = weakFragment.get();
        if (fragment != null) {
            fragment.setImage(this.userBitmap);
        }
    }

    public void setUserBitmap(Bitmap userBitmap) {
        this.userBitmap = userBitmap;
    }

    public void cameraClicked() {
        PreviewFragment fragment = weakFragment.get();
        if (fragment != null) {
            fragment.navigateToCamera(new CameraCallback() {
                @Override
                public void imageTaken(Bitmap bitmap) {
                    ResultAttachment attachment = new ResultAttachment(userBitmap, bitmap);
                    fragment.navigateToResult(attachment);
                }

                @Override
                public boolean isSelectFromGalleryEnabled() {
                    return false;
                }
            });
        }
    }

    public void galleryClicked() {
        PreviewFragment fragment = weakFragment.get();
        if (fragment != null) {
            fragment.navigateToGallery(bitmap -> {
                ResultAttachment attachment = new ResultAttachment(userBitmap, bitmap);
                fragment.navigateToResult(attachment);
            });
        }
    }

    public void bluetoothClicked() {
        PreviewFragment fragment = weakFragment.get();
        if (fragment != null) {
            fragment.navigateToBluetooth(new BluetoothCallback() {
                @Override
                public void bleRetrieveSuccess(Bitmap bitmap) {
                    ResultAttachment attachment = new ResultAttachment(userBitmap, bitmap);
                    fragment.navigateToResult(attachment);
                }

                @Override
                public void bleRetrieveFailed(Exception e) {

                }

                @Override
                public void bleRetrieveUpdatedStatus(int newStatus) {

                }

                @Override
                public void bleRetrieveUpdatedProgress(int progress) {

                }
            });
        }
    }

}
