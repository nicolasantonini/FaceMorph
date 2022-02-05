package it.unipr.advmobdev.mat301275.facemorph.storage;

public interface CreateCallback {
    void createSuccess();
    void createFailed(Exception e);
}
