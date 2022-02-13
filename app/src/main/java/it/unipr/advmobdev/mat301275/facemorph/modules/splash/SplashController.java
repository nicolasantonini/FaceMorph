package it.unipr.advmobdev.mat301275.facemorph.modules.splash;

import android.content.Context;
import android.util.Log;

import org.opencv.android.InstallCallbackInterface;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.lang.ref.WeakReference;

import it.unipr.advmobdev.mat301275.facemorph.authentication.AuthenticationManager;

public class SplashController {

    private String TAG = "FaceMorphApp";

    private WeakReference<SplashFragment> weakFragment = null;

    public SplashController(SplashFragment fragment) {
        weakFragment = new WeakReference<>(fragment);
    }

    public void viewDidLoad(Context context) {
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, context, new LoaderCallbackInterface() {
                @Override
                public void onManagerConnected(int status) {
                    if (status == LoaderCallbackInterface.SUCCESS) {
                        SplashController.this.navigate();
                    } else {
                        Log.e(TAG, "Unable to load OpenCV");
                        System.exit(-1);
                    }
                }

                @Override
                public void onPackageInstall(int operation, InstallCallbackInterface callback) {
                    SplashController.this.navigate();
                }
            });
        } else {
            this.navigate();
        }

    }

    private void navigate() {
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
