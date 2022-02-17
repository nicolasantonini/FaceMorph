package it.unipr.advmobdev.mat301275.facemorph.modules.gallery;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.List;

import it.unipr.advmobdev.mat301275.facemorph.R;
import it.unipr.advmobdev.mat301275.facemorph.entities.UserImage;
import it.unipr.advmobdev.mat301275.facemorph.modules.camera.CameraCallback;
import it.unipr.advmobdev.mat301275.facemorph.modules.camera.CameraFragmentArgs;
import it.unipr.advmobdev.mat301275.facemorph.modules.home.HomeFragmentDirections;
import it.unipr.advmobdev.mat301275.facemorph.opencv.Utilities;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GalleryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GalleryFragment extends Fragment {

    GalleryController controller = new GalleryController(this);

    private static final String TAG = "DarkRoomTag";
    private static final int SELECT_PICTURE = 1;
    private String selectedImagePath;
    Mat sampledImage=null;
    Mat originalImage=null;
    Mat greyImage=null;

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
        GalleryCallback callback = GalleryFragmentArgs.fromBundle(getArguments()).getCallback();
        controller.setCallback(callback);
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.gallery_recycler_view);
        mAddButton = (Button) getView().findViewById(R.id.add_a_photo_button);
        mAddButton.setOnClickListener(v -> {
            mPermissionResult.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        });
        mProgressBar = (ProgressBar) getView().findViewById(R.id.gallery_progress_bar);
        mLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new GalleryAdapter(userImage -> {
            controller.userImageSelected(userImage);
        });
        mRecyclerView.setAdapter(mAdapter);
        controller.viewCreated();
    }

    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                Log.i(TAG, "selectedImagePath: " + selectedImagePath);
                loadImage(selectedImagePath);
                //displayImage(sampledImage);
            }
        }
    }

    private final ActivityResultLauncher<String> mPermissionResult = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            result -> {
                if(result) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_PICK);
                    startActivityForResult(Intent.createChooser(intent,
                            "Seleziona immagine"), SELECT_PICTURE);
                } else {
                    Toast.makeText(getActivity(), "Storage permission denied", Toast.LENGTH_SHORT).show();
                }
            });


    private void loadImage(String path) {
        originalImage = Imgcodecs.imread(path);
        Mat rgbImage = new Mat();
        Imgproc.cvtColor(originalImage, rgbImage, Imgproc.COLOR_BGR2RGB);
        final Bitmap bitmap = Bitmap.createBitmap(rgbImage.cols(), rgbImage.rows(), Bitmap.Config.RGB_565);
        Utils.matToBitmap(rgbImage, bitmap);
        if (bitmap.getWidth() > 1080) {
            double factor = bitmap.getWidth() / 1080.0;
            int newHeight = (int) (bitmap.getHeight() / factor);
            Bitmap resizedBitmap = Utilities.resizeBitmapTo(bitmap, 1080, newHeight);
            controller.userImageSelected(new UserImage(resizedBitmap));
        } else {
            controller.userImageSelected(new UserImage(bitmap));
        }


    }


        private String getPath(Uri uri) {
        if(uri == null ) {
            return null;
        }
// try to retrieve the image first from the Media Store
// this however works only for images selected from the gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(uri, projection,
                null, null, null);
        if(cursor != null ){
            int column_index = cursor.getColumnIndexOrThrow(
                    MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
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