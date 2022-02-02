package it.unipr.advmobdev.mat301275.facemorph.authentication;

public interface UserCreateCallback {
    void userCreateSuccess();
    void userCreateFailure(Exception exception);
}
