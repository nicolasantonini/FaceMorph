package it.unipr.advmobdev.mat301275.facemorph.modules.gallery;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.List;

import it.unipr.advmobdev.mat301275.facemorph.authentication.AuthenticationManager;
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
            fragment.disableInteraction();
        }
        String userId = AuthenticationManager.getInstance().getUserId();
        StorageManager.getInstance().getImages(userId, new FetchCallback() {
            @Override
            public void fetchSuccess(List<UserImage> images) {
                GalleryFragment fragment = weakFragment.get();
                if (fragment != null) {
                    fragment.enableInteraction();
                    fragment.setImages(images);
                }
            }

            @Override
            public void fetchFailed(Exception e) {
                GalleryFragment fragment = weakFragment.get();
                if (fragment != null) {
                    fragment.enableInteraction();
                    fragment.displayToast(e.getLocalizedMessage());
                }
            }
        });
    }

    public void addPhotoClicked() {
        GalleryFragment fragment = weakFragment.get();
        if (fragment != null) {
            fragment.navigateToCamera(new CameraCallback() {
                @Override
                public void imageTaken(Bitmap bitmap) {
                    Log.i("Nic", "Image taken");
                    fragment.popFragment();
                    fragment.disableInteraction();
                    String userId = AuthenticationManager.getInstance().getUserId();
                    UserImage image = new UserImage(bitmap);
                    StorageManager.getInstance().addImage(userId, image, new CreateCallback() {
                        @Override
                        public void createSuccess() {
                            StorageManager.getInstance().getImages(userId, new FetchCallback() {
                                @Override
                                public void fetchSuccess(List<UserImage> images) {
                                    GalleryFragment fragment = weakFragment.get();
                                    if (fragment != null) {
                                        fragment.enableInteraction();
                                        fragment.setImages(images);
                                    }
                                }

                                @Override
                                public void fetchFailed(Exception e) {
                                    GalleryFragment fragment = weakFragment.get();
                                    if (fragment != null) {
                                        fragment.enableInteraction();
                                        fragment.displayToast(e.getLocalizedMessage());
                                    }
                                }
                            });
                        }
                        @Override
                        public void createFailed(Exception e) {
                            fragment.displayToast(e.getLocalizedMessage());
                            fragment.enableInteraction();
                        }
                    });
                }

                @Override
                public boolean isSelectFromGalleryEnabled() {
                    return false;
                }
            });
        }
    }

}
