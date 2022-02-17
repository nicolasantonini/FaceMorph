package it.unipr.advmobdev.mat301275.facemorph.opencv;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.opencv.android.Utils;

import java.io.ByteArrayOutputStream;

public class Utilities {

    public static byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static Bitmap byteArrayToBitmap(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static Bitmap resizeBitmap(Bitmap src, Bitmap dst) {
        return Bitmap.createScaledBitmap(src, dst.getWidth(), dst.getHeight(), true);
    }

    public static Bitmap resizeBitmapTo(Bitmap src, int width, int height) {
        return Bitmap.createScaledBitmap(src, width, height, true);
    }

}
