package it.unipr.advmobdev.mat301275.facemorph.modules.login;

import android.util.Log;

import java.lang.ref.WeakReference;

import it.unipr.advmobdev.mat301275.facemorph.authentication.AuthenticationManager;
import it.unipr.advmobdev.mat301275.facemorph.authentication.UserCreateCallback;

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


        }
    }

    public void signUpClicked() {
        LoginFragment fragment = weakFragment.get();
        if (fragment != null) {
            String email = fragment.getEmail();
            String password = fragment.getPassword();

            AuthenticationManager.getInstance().createUserWithEmailAndPassword(email, password, new UserCreateCallback() {
                @Override
                public void userCreateSuccess() {
                    Log.i("Nic", "Utente creato");
                }

                @Override
                public void userCreateFailure(Exception exception) {
                    Log.i("Nic", "Utente NON creato");
                }
            });
        }
    }


}
