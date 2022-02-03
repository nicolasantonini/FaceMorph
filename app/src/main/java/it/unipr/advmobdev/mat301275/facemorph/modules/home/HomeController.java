package it.unipr.advmobdev.mat301275.facemorph.modules.home;

import android.os.Parcel;
import android.util.Log;

import java.lang.ref.WeakReference;

import it.unipr.advmobdev.mat301275.facemorph.modules.camera.CameraCallback;
import it.unipr.advmobdev.mat301275.facemorph.modules.login.LoginFragment;


public class HomeController {

    static String KEY_TEXT = "KEY_TEXT";

    private WeakReference<HomeFragment> weakFragment = null;

    public HomeController(HomeFragment fragment) {
        weakFragment = new WeakReference<>(fragment);
    }

    public void cameraPressed() {
        HomeFragment fragment = weakFragment.get();
        if (fragment != null) {
            fragment.navigateToCamera(new CameraCallback() {
                @Override
                public void imageSelected() {
                    Log.i("Nic", "Photo selected");
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {

                }
            });
        }
    }

    public void settingsPressed() {
        HomeFragment fragment = weakFragment.get();
        if (fragment != null) {
            fragment.navigateToSettings();
        }
    }

    public void galleryPressed() {
        HomeFragment fragment = weakFragment.get();
        if (fragment != null) {
            fragment.navigateToGallery();
        }
    }

}
