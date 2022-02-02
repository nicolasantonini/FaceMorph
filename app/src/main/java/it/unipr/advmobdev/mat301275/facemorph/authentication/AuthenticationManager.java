package it.unipr.advmobdev.mat301275.facemorph.authentication;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

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

    public String getUserEmail() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            return currentUser.getEmail();
        } else {
            return null;
        }
    }

    public void createUserWithEmailAndPassword(String email, String password, UserCreateCallback callback) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Executor) this, task -> {
                    if (task.isSuccessful()) {
                        callback.userCreateSuccess();
                    } else {
                        Exception exception = task.getException();
                        callback.userCreateFailure(exception);
                    }
                });
    }

}
