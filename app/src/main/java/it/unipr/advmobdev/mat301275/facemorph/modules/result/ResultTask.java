package it.unipr.advmobdev.mat301275.facemorph.modules.result;

import android.os.AsyncTask;

import java.io.IOException;

import it.unipr.advmobdev.mat301275.facemorph.opencv.Morph;
import it.unipr.advmobdev.mat301275.facemorph.opencv.ProgressCallback;

class ResultTask extends AsyncTask<ProgressCallback, Void, Void> {

    @Override
    protected Void doInBackground(ProgressCallback... progressCallbacks) {
        try {
            Morph.getMorph(progressCallbacks[0].getAlpha(), progressCallbacks[0].getContext(), progressCallbacks[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}