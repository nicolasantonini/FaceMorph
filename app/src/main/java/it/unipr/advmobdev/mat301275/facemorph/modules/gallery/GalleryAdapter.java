package it.unipr.advmobdev.mat301275.facemorph.modules.gallery;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import it.unipr.advmobdev.mat301275.facemorph.R;
import it.unipr.advmobdev.mat301275.facemorph.entities.UserImage;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    private static final String TAG = "CustomAdapter";

    private GalleryAdapterCallback callback;
    public List<UserImage> images;

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;

        public ViewHolder(View v, GalleryViewHolderCallback callback) {
            super(v);
            v.setOnClickListener(v1 -> {
                callback.indexSelected(getAdapterPosition());
                    });
            imageView = v.findViewById(R.id.item_image);
        }

        public ImageView getImageView() {
            return imageView;
        }
    }

    public GalleryAdapter(GalleryAdapterCallback callback) {
        this.callback = callback;
        this.images = new ArrayList<>();
    }

    public void setDataset(List<UserImage> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_grid_item, parent, false);

        return new ViewHolder(v, new GalleryViewHolderCallback() {
            @Override
            public void indexSelected(int index) {
                callback.userImageSelected(images.get(index));
            }
        });
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getImageView().setImageBitmap(this.images.get(position).getBitmap());
    }

    @Override
    public int getItemCount() {
        return images.size();
    }
}