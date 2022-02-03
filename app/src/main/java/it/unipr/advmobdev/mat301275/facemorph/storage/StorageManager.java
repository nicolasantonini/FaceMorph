package it.unipr.advmobdev.mat301275.facemorph.storage;

import android.graphics.Bitmap;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class StorageManager {
    private static StorageManager instance = null;

    private StorageManager() {
        db = FirebaseFirestore.getInstance();
    }

    public static StorageManager getInstance() {
        if (instance == null) {
            instance = new StorageManager();
        }
        return instance;
    }

    private FirebaseFirestore db;

    public void addImage(String userId, Bitmap bitmap) {

    }

    public void removeImage(String userId, String id) {

    }

    public void getImages(/* Callback */) {

    }

}
