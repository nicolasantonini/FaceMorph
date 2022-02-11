package it.unipr.advmobdev.mat301275.facemorph.modules.result;

import java.lang.ref.WeakReference;

public class ResultController {

    private WeakReference<ResultFragment> weakFragment = null;
    private ResultAttachment attachment;

    public ResultController(ResultFragment fragment) {
        weakFragment = new WeakReference<>(fragment);
    }

    public void setAttachment(ResultAttachment attachment) {
        this.attachment = attachment;
    }

    public void viewCreated() {
        ResultFragment fragment = weakFragment.get();
        if (fragment != null) {
            fragment.setImage(this.attachment.getBitmapTwo());
        }
    }

}
