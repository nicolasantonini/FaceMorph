package it.unipr.advmobdev.mat301275.facemorph.modules.splash;

import java.lang.ref.WeakReference;

import it.unipr.advmobdev.mat301275.facemorph.authentication.AuthenticationManager;

public class SplashController {

    private WeakReference<SplashFragment> weakFragment = null;

    public SplashController(SplashFragment fragment) {
        weakFragment = new WeakReference<>(fragment);
    }

    public void viewDidLoad() {
        SplashFragment fragment = weakFragment.get();
        if (fragment != null) {
            if (AuthenticationManager.getInstance().isUserSignedIn()) {
                fragment.navigateToHome();
            } else {
                fragment.navigateToLogin();
            }

        }
    }

}
