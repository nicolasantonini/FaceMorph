package it.unipr.advmobdev.mat301275.facemorph.modules.splash;

import java.lang.ref.WeakReference;

public class SplashController {

    WeakReference<SplashFragment> weakFragment = null;

    public SplashController(SplashFragment fragment) {
        weakFragment = new WeakReference<>(fragment);
    }

    public void viewDidLoad() {
        SplashFragment fragment = weakFragment.get();
        if (fragment != null) {
            fragment.navigateToLogin();
        }
    }

}
