package it.unipr.advmobdev.mat301275.facemorph.authentication;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthenticationManager {

    private static AuthenticationManager instance = null;

    private AuthenticationManager() {
        mAuth = FirebaseAuth.getInstance();
    }

    public static AuthenticationManager getInstance() {
        if (instance == null) {
            instance = new AuthenticationManager();
        }
        return instance;
    }

    private FirebaseAuth mAuth;

    public boolean isUserSignedIn() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            return true;
        } else {
            return false;
        }
    }

    public String getUserId() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            return currentUser.getUid();
        } else {
            return null;
        }
    }

    public void createUserWithEmailAndPassword(String email, String password, UserCreateCallback callback) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                callback.userCreateSuccess();
            } else {
                Exception exc = task.getException();
                callback.userCreateFailure(exc);
            }
        });
    }

    public void loginWithEmailAndPassword(String email, String password, UserLoginCallback callback) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                callback.userLoginSuccess();
            } else {
                Exception exc = task.getException();
                callback.userLoginFailure(exc);
            }
        });
    }

    public void logout() {
        mAuth.signOut();
    }

}
