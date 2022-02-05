package it.unipr.advmobdev.mat301275.facemorph.modules.home;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.util.Log;

import java.lang.ref.WeakReference;

import it.unipr.advmobdev.mat301275.facemorph.authentication.AuthenticationManager;
import it.unipr.advmobdev.mat301275.facemorph.entities.UserImage;
import it.unipr.advmobdev.mat301275.facemorph.modules.camera.CameraCallback;
import it.unipr.advmobdev.mat301275.facemorph.modules.login.LoginFragment;
import it.unipr.advmobdev.mat301275.facemorph.storage.CreateCallback;
import it.unipr.advmobdev.mat301275.facemorph.storage.StorageManager;


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
                public void imageTaken(Bitmap bitmap) {
                    Log.i("Nic", "Image taken");
                    fragment.popFragment();

                    String userId = AuthenticationManager.getInstance().getUserId();
                    UserImage image = new UserImage(bitmap);
                    StorageManager.getInstance().addImage(userId, image, new CreateCallback() {
                        @Override
                        public void createSuccess() {
                            Log.i("Nic", "Immagine salvata");
                        }

                        @Override
                        public void createFailed(Exception e) {
                            fragment.displayToast(e.getLocalizedMessage());
                        }
                    });
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
