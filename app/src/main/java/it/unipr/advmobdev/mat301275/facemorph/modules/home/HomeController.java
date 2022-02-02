package it.unipr.advmobdev.mat301275.facemorph.modules.home;

import java.lang.ref.WeakReference;


public class HomeController {

    private WeakReference<HomeFragment> weakFragment = null;

    public HomeController(HomeFragment fragment) {
        weakFragment = new WeakReference<>(fragment);
    }

    public void cameraPressed() {

    }

    public void settingsPressed() {

    }

    public void galleryPressed() {

    }

}
