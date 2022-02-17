package it.unipr.advmobdev.mat301275.facemorph.modules.login;

import java.lang.ref.WeakReference;

public class LoginController {

    private WeakReference<LoginFragment> weakFragment = null;

    public LoginController(LoginFragment fragment) {
        weakFragment = new WeakReference<>(fragment);
    }

    public void loginClicked() {
        LoginFragment fragment = weakFragment.get();
        if (fragment != null) {
            String email = fragment.getEmail();
            String password = fragment.getPassword();

            fragment.disableInteraction();
        }
    }

    public void signUpClicked() {
        LoginFragment fragment = weakFragment.get();
        if (fragment != null) {
            String email = fragment.getEmail();
            String password = fragment.getPassword();

            fragment.disableInteraction();


        }
    }


}
