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

    MorphConfiguration configuration;

    public ResultController(ResultFragment fragment) {
        weakFragment = new WeakReference<>(fragment);
    }

    public void setAttachment(ResultAttachment attachment) {
        this.attachment = attachment;
    }

    public void viewCreated(Context context) {
        ResultFragment fragment = weakFragment.get();
        if (fragment != null) {
            configuration = MorphSetup.getConfiguration(attachment.getBitmapOne(), attachment.getBitmapTwo());
            fragment.setImage(Morph.morph(configuration, attachment.getBitmapOne(), attachment.getBitmapTwo(), lastAlpha, (int) lastTriangles));
        }
    }

    public void alphaChanged(float alpha, Context context) {
        ResultFragment fragment = weakFragment.get();
        if (fragment != null) {
            lastAlpha = alpha;
            fragment.setImage(Morph.morph(configuration, attachment.getBitmapOne(), attachment.getBitmapTwo(), lastAlpha, (int) lastTriangles));
        }
    }

    public void trianglesChanged(float triangles, Context context) {
        ResultFragment fragment = weakFragment.get();
        if (fragment != null) {
            lastTriangles = triangles;
            fragment.setImage(Morph.morph(configuration, attachment.getBitmapOne(), attachment.getBitmapTwo(), lastAlpha, (int) lastTriangles));
        }
    }

}
