package it.unipr.advmobdev.mat301275.facemorph.modules.home;

import java.lang.ref.WeakReference;

import it.unipr.advmobdev.mat301275.facemorph.modules.login.LoginFragment;


public class HomeController {

    private WeakReference<HomeFragment> weakFragment = null;

    public HomeController(HomeFragment fragment) {
        weakFragment = new WeakReference<>(fragment);
    }

    public void cameraPressed() {
        HomeFragment fragment = weakFragment.get();
        if (fragment != null) {
            fragment.navigateToCamera();
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
