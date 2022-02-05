package it.unipr.advmobdev.mat301275.facemorph.modules.gallery;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.unipr.advmobdev.mat301275.facemorph.R;
import it.unipr.advmobdev.mat301275.facemorph.entities.UserImage;
import it.unipr.advmobdev.mat301275.facemorph.modules.camera.CameraCallback;
import it.unipr.advmobdev.mat301275.facemorph.modules.home.HomeFragmentDirections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GalleryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GalleryFragment extends Fragment {

    GalleryController controller = new GalleryController(this);

    protected RecyclerView mRecyclerView;
    protected Button mAddButton;
    protected ProgressBar mProgressBar;

    protected GalleryAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;

    public GalleryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    public static GalleryFragment newInstance() {
        GalleryFragment fragment = new GalleryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.gallery_recycler_view);
        mAddButton = (Button) getView().findViewById(R.id.add_a_photo_button);
        mAddButton.setOnClickListener(v -> {
            controller.addPhotoClicked();
        });
        mProgressBar = (ProgressBar) getView().findViewById(R.id.gallery_progress_bar);
        mLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new GalleryAdapter();
        mRecyclerView.setAdapter(mAdapter);
        controller.viewCreated();
    }

    public void displayToast(String string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
    }

    public void setImages(List<UserImage> images) {
        new Handler(Looper.getMainLooper()).post(() -> mAdapter.setDataset(images));
    }

    public void disableInteraction() {
        mAddButton.setClickable(false);
        mAddButton.setAlpha(0.8f);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void enableInteraction() {
        mAddButton.setClickable(true);
        mAddButton.setAlpha(1.0f);
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    public void navigateToCamera(CameraCallback callback) {
        GalleryFragmentDirections.ActionGalleryFragmentToCameraFragment action = GalleryFragmentDirections.actionGalleryFragmentToCameraFragment(callback);
        NavHostFragment.findNavController(this).navigate(action);
    }

    public void popFragment() {
        NavHostFragment.findNavController(this).popBackStack();
    }

}