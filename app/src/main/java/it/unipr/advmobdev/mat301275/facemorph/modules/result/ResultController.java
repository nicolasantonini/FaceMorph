package it.unipr.advmobdev.mat301275.facemorph.modules.result;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.IOException;
import java.lang.ref.WeakReference;

import it.unipr.advmobdev.mat301275.facemorph.opencv.Morph;

public class ResultController {

    private WeakReference<ResultFragment> weakFragment = null;
    private ResultAttachment attachment;

    public ResultController(ResultFragment fragment) {
        weakFragment = new WeakReference<>(fragment);
    }

    public void setAttachment(ResultAttachment attachment) {
        this.attachment = attachment;
    }

    public void viewCreated(Context context) {
        ResultFragment fragment = weakFragment.get();
        if (fragment != null) {
            try {
                Bitmap img = Morph.getMorph(0.5, context);
                fragment.setImage(img);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //fragment.setImage(this.attachment.getBitmapTwo());
        }
    }

    public void alphaChanged(float alpha) {
        ResultFragment fragment = weakFragment.get();
        if (fragment != null) {
            fragment.displayToast(String.valueOf(alpha));
        }
    }

}
