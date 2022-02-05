package it.unipr.advmobdev.mat301275.facemorph.storage;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.unipr.advmobdev.mat301275.facemorph.entities.UserImage;


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

    private final FirebaseFirestore db;

    public void addImage(String userId, UserImage userImage, CreateCallback callback) {
        db.collection("users").document(userId).collection("images").add(userImage.toDict()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                callback.createSuccess();
            } else {
                callback.createFailed(task.getException());
            }
        });
    }

    public void getImages(String userId, FetchCallback callback) {
        db.collection("users").document(userId).collection("images").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<UserImage> images = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    images.add(new UserImage(document.getData()));
                }
                callback.fetchSuccess(images);
            } else {
                callback.fetchFailed(task.getException());
            }
        });
    }

    /*
    Deletes user images (even if Google Firebase docs say not to do it "https://firebase.google.com/docs/firestore/manage-data/delete-data#java_4")
     */
    public void eraseUserData(String userId, EraseCallback callback) {
        db.collection("users").document(userId).collection("images").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<UserImage> images = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    db.collection("users").document(userId).collection("images").document(document.getId()).delete();
                }
                callback.eraseSuccess();
            } else {
                callback.eraseFailed(task.getException());
            }
        });
    }

}
