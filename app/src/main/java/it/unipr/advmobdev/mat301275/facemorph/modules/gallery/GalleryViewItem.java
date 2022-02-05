package it.unipr.advmobdev.mat301275.facemorph.modules.gallery;

import android.content.Context;
import android.util.AttributeSet;

public class GalleryViewItem extends androidx.appcompat.widget.AppCompatImageView {

    public GalleryViewItem(Context context) {
        super(context);
    }

    public GalleryViewItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GalleryViewItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}