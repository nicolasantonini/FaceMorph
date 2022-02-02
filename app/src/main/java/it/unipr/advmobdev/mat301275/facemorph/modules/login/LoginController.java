package it.unipr.advmobdev.mat301275.facemorph.modules.login;

import java.lang.ref.WeakReference;

import it.unipr.advmobdev.mat301275.facemorph.authentication.AuthenticationManager;
import it.unipr.advmobdev.mat301275.facemorph.authentication.UserCreateCallback;
import it.unipr.advmobdev.mat301275.facemorph.authentication.UserLoginCallback;

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

            AuthenticationManager.getInstance().loginWithEmailAndPassword(email, password, new UserLoginCallback() {
                @Override
                public void userLoginSuccess() {
                    fragment.enableInteraction();
                    fragment.navigateToHome();
                }

                @Override
                public void userLoginFailure(Exception exception) {
                    fragment.enableInteraction();
                    fragment.displayToast(exception.getLocalizedMessage());
                }
            });
        }
    }

    public void signUpClicked() {
        LoginFragment fragment = weakFragment.get();
        if (fragment != null) {
            String email = fragment.getEmail();
            String password = fragment.getPassword();

            fragment.disableInteraction();

            AuthenticationManager.getInstance().createUserWithEmailAndPassword(email, password, new UserCreateCallback() {
                @Override
                public void userCreateSuccess() {
                    fragment.enableInteraction();
                    fragment.navigateToHome();
                }

                @Override
                public void userCreateFailure(Exception exception) {
                    fragment.enableInteraction();
                    fragment.displayToast(exception.getLocalizedMessage());
                }
            });
        }
    }


}
