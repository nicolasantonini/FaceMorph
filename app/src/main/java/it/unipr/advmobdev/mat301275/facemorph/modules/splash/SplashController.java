package it.unipr.advmobdev.mat301275.facemorph.modules.splash;

import android.content.Context;
import android.util.Log;

import org.opencv.android.InstallCallbackInterface;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.lang.ref.WeakReference;

import it.unipr.advmobdev.mat301275.facemorph.authentication.AuthenticationManager;

public class SplashController {

    private WeakReference<SplashFragment> weakFragment = null;

    public SplashController(SplashFragment fragment) {
        weakFragment = new WeakReference<>(fragment);
    }

    public void viewDidLoad(Context context) {
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, context, new LoaderCallbackInterface() {
            @Override
            public void onManagerConnected(int status) {
                SplashFragment fragment = weakFragment.get();
                if (fragment != null) {
                    if (AuthenticationManager.getInstance().isUserSignedIn()) {
                        fragment.navigateToHome();
                    } else {
                        fragment.navigateToLogin();
                    }
                }
            }

            @Override
            public void onPackageInstall(int operation, InstallCallbackInterface callback) {
                SplashFragment fragment = weakFragment.get();
                if (fragment != null) {
                    if (AuthenticationManager.getInstance().isUserSignedIn()) {
                        fragment.navigateToHome();
                    } else {
                        fragment.navigateToLogin();
                    }
                }
            }
        });
    }

}
