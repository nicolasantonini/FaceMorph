package it.unipr.advmobdev.mat301275.facemorph.modules.gallery;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.List;

import it.unipr.advmobdev.mat301275.facemorph.entities.UserImage;
import it.unipr.advmobdev.mat301275.facemorph.modules.camera.CameraCallback;
import it.unipr.advmobdev.mat301275.facemorph.modules.login.LoginFragment;
import it.unipr.advmobdev.mat301275.facemorph.storage.CreateCallback;
import it.unipr.advmobdev.mat301275.facemorph.storage.FetchCallback;
import it.unipr.advmobdev.mat301275.facemorph.storage.StorageManager;

public class GalleryController {

    private GalleryCallback callback;
    private WeakReference<GalleryFragment> weakFragment = null;

    public GalleryController(GalleryFragment fragment) {
        weakFragment = new WeakReference<>(fragment);
    }

    public void setCallback(GalleryCallback callback) {
        this.callback = callback;
    }

    public void userImageSelected(UserImage userImage) {
        callback.imageSelected(userImage.getBitmap());
    }

    public void viewCreated() {
        GalleryFragment fragment = weakFragment.get();
        if (fragment != null) {

        }
    }

    public void addPhotoClicked() {
        GalleryFragment fragment = weakFragment.get();
        if (fragment != null) {

        }
    }

}
