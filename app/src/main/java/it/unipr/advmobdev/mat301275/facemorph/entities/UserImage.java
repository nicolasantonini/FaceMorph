package it.unipr.advmobdev.mat301275.facemorph.entities;

import android.graphics.Bitmap;

import com.google.firebase.firestore.Blob;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import it.unipr.advmobdev.mat301275.facemorph.opencv.Utilities;

public class UserImage {

    private Bitmap bitmap;
    private Date uploadDate;

    public UserImage(Bitmap bitmap, Date uploadDate) {
        this.bitmap = bitmap;
        this.uploadDate = uploadDate;
    }

    public UserImage(Bitmap bitmap) {
        this.bitmap = bitmap;
        this.uploadDate = new Date(System.currentTimeMillis());
    }

    public UserImage(Map<String, Object> dict) {
        Blob dataBlob = (Blob) dict.get("data");
        byte[] byteArray = dataBlob.toBytes();
        com.google.firebase.Timestamp date = (com.google.firebase.Timestamp) dict.get("uploadDate");
        this.bitmap = Utilities.byteArrayToBitmap(byteArray);
        this.uploadDate = date.toDate();
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Map<String, Object> toDict() {
        HashMap<String, Object> map = new HashMap<>();
        byte[] byteArray = Utilities.bitmapToByteArray(this.bitmap);
        map.put("data", Blob.fromBytes(byteArray));
        map.put("uploadDate", this.uploadDate);
        return map;
    }

}
