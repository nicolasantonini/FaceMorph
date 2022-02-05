package it.unipr.advmobdev.mat301275.facemorph.modules.settings;

import java.lang.ref.WeakReference;

import it.unipr.advmobdev.mat301275.facemorph.authentication.AuthenticationManager;
import it.unipr.advmobdev.mat301275.facemorph.modules.login.LoginFragment;
import it.unipr.advmobdev.mat301275.facemorph.storage.EraseCallback;
import it.unipr.advmobdev.mat301275.facemorph.storage.StorageManager;

public class SettingsController {

    private WeakReference<SettingsFragment> weakFragment = null;

    public SettingsController(SettingsFragment fragment) {
        weakFragment = new WeakReference<>(fragment);
    }

    public void eraseClicked() {
        SettingsFragment fragment = weakFragment.get();
        if (fragment != null) {
            fragment.disableInteraction();
        }
        String userId = AuthenticationManager.getInstance().getUserId();
        StorageManager.getInstance().eraseUserData(userId, new EraseCallback() {
            @Override
            public void eraseSuccess() {
                SettingsFragment fragment = weakFragment.get();
                if (fragment != null) {
                    fragment.enableInteraction();
                    fragment.displayToast("Data erased");
                }
            }

            @Override
            public void eraseFailed(Exception e) {
                SettingsFragment fragment = weakFragment.get();
                if (fragment != null) {
                    fragment.enableInteraction();
                    fragment.displayToast(e.getLocalizedMessage());
                }
            }
        });
    }

    public void quitClicked() {
        SettingsFragment fragment = weakFragment.get();
        if (fragment != null) {
            AuthenticationManager.getInstance().logout();
            fragment.navigateToLogin();
        }

    }

}
