package it.unipr.advmobdev.mat301275.facemorph.entities;

import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.Map;

import it.unipr.advmobdev.mat301275.facemorph.opencv.Utilities;

public class UserImage {

    private Bitmap bitmap;

    public UserImage(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public UserImage(Map<String, Object> dict) {
        byte[] byteArray = (byte[]) dict.get("data");
        this.bitmap = Utilities.byteArrayToBitmap(byteArray);
    }

    public Map<String, Object> toDict() {
        HashMap<String, Object> map = new HashMap<>();
        byte[] byteArray = Utilities.bitmapToByteArray(this.bitmap);
        map.put("data", byteArray);
        return new HashMap<>();
    }

}
