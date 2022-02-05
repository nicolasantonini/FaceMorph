package it.unipr.advmobdev.mat301275.facemorph.storage;

public interface EraseCallback {
    void eraseSuccess();
    void eraseFailed(Exception e);
}
