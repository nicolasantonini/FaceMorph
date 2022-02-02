package it.unipr.advmobdev.mat301275.facemorph.authentication;

public class AuthenticationManager {

    private static AuthenticationManager instance = null;

    private AuthenticationManager() {}

    public static AuthenticationManager getInstance() {
        if (instance == null) {
            instance = new AuthenticationManager();
        }
        return instance;
    }


    //private FirebaseAuth mAuth;


}
