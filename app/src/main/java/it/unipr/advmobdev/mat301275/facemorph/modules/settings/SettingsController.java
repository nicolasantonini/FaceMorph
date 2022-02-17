package it.unipr.advmobdev.mat301275.facemorph.modules.settings;

import java.lang.ref.WeakReference;

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
            fragment.displayToast("Unavailable");
        }

    }

    public void quitClicked() {
        SettingsFragment fragment = weakFragment.get();
        if (fragment != null) {
            fragment.displayToast("Unavailable");
        }

    }

}
