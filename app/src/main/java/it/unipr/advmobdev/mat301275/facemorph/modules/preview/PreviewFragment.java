package it.unipr.advmobdev.mat301275.facemorph.modules.preview;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import it.unipr.advmobdev.mat301275.facemorph.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PreviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PreviewFragment extends Fragment {

    PreviewController controller = new PreviewController(this);

    private Button continueWithCameraButton;
    private Button continueWithGalleryButton;
    private Button continueWithBluetoothButton;
    private ImageView previewImageView;

    public PreviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PreviewFragment.
     */
    public static PreviewFragment newInstance() {
        PreviewFragment fragment = new PreviewFragment();
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
        PreviewAttachment attachment = PreviewFragmentArgs.fromBundle(getArguments()).getBitmap();
        controller.setBitmap(attachment.getBitmap());
        return inflater.inflate(R.layout.fragment_preview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        continueWithCameraButton = getView().findViewById(R.id.continue_with_camera);
        continueWithGalleryButton = getView().findViewById(R.id.continue_with_gallery);
        continueWithBluetoothButton  = getView().findViewById(R.id.continue_with_bluetooth);
        previewImageView  = getView().findViewById(R.id.preview_image_view);
        continueWithCameraButton.setOnClickListener(v -> controller.cameraClicked());
        continueWithGalleryButton.setOnClickListener(v -> controller.galleryClicked());
        continueWithBluetoothButton.setOnClickListener(v -> controller.bluetoothClicked());
        controller.viewCreated();
    }

    public void setImage(Bitmap image) {
        previewImageView.setImageBitmap(image);
    }
}