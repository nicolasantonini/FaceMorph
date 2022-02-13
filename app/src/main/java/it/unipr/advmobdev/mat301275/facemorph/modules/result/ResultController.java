package it.unipr.advmobdev.mat301275.facemorph.modules.result;

import android.content.Context;
import android.graphics.Bitmap;

import java.lang.ref.WeakReference;

import it.unipr.advmobdev.mat301275.facemorph.opencv.Morph;
import it.unipr.advmobdev.mat301275.facemorph.opencv.MorphConfiguration;
import it.unipr.advmobdev.mat301275.facemorph.opencv.MorphSetup;

public class ResultController {

    private WeakReference<ResultFragment> weakFragment = null;
    private ResultAttachment attachment;

    private double lastAlpha = 0.5;
    private double lastTriangles = 100.0;

    public ResultController(ResultFragment fragment) {
        weakFragment = new WeakReference<>(fragment);
    }

    public void setAttachment(ResultAttachment attachment) {
        this.attachment = attachment;
    }

    public void viewCreated(Context context) {

    }

    public void alphaChanged(float alpha, Context context) {
        ResultFragment fragment = weakFragment.get();
        if (fragment != null) {
            fragment.displayToast(String.valueOf(alpha));
            MorphConfiguration configuration = MorphSetup.getConfiguration(attachment.getBitmapOne(), attachment.getBitmapTwo());
            fragment.setImage(Morph.morph(configuration, attachment.getBitmapOne(), attachment.getBitmapTwo(), 0.5, (int) alpha));
        }

    }

}
