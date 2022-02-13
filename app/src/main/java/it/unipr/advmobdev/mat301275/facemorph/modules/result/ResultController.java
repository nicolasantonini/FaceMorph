package it.unipr.advmobdev.mat301275.facemorph.modules.result;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;

import org.opencv.core.Mat;

import java.io.IOException;
import java.lang.ref.WeakReference;

import it.unipr.advmobdev.mat301275.facemorph.opencv.Morph;
import it.unipr.advmobdev.mat301275.facemorph.opencv.ProgressCallback;

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

    }

    public void alphaChanged(float alpha, Context context) {
        ResultFragment fragment = weakFragment.get();
        if (fragment != null) {
            fragment.displayToast(String.valueOf(alpha));
                new ResultTask().doInBackground(new ProgressCallback() {
                    @Override
                    public void triangleCalcolated(Bitmap bitmap) {
                        //fragment.setImage(bitmap);
                    }

                    @Override
                    public void imageCalcolated(Bitmap bitmap) {
                        fragment.setImage(bitmap);
                    }

                    @Override
                    public Context getContext() {
                        return context;
                    }

                    @Override
                    public double getAlpha() {
                        return 0.5;
                    }

                    @Override
                    public int getIterations() {
                        return (int) alpha;
                    }

                    @Override
                    public Bitmap getBitmap1() {
                        return attachment.getBitmapOne();
                    }

                    @Override
                    public Bitmap getBitmap2() {
                        return attachment.getBitmapTwo();
                    }


                });
        }

    }

}
