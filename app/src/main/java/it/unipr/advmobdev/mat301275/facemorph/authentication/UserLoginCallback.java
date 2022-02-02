package it.unipr.advmobdev.mat301275.facemorph.authentication;

public interface UserLoginCallback {
    void userLoginSuccess();
    void userLoginFailure(Exception exception);
}