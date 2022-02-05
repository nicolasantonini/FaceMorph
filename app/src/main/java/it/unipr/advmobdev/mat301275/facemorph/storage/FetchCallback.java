package it.unipr.advmobdev.mat301275.facemorph.storage;

import java.util.List;

import it.unipr.advmobdev.mat301275.facemorph.entities.UserImage;

public interface FetchCallback {
    void fetchSuccess(List<UserImage> images);
    void fetchFailed(Exception e);
}
