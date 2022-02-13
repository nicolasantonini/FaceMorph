package it.unipr.advmobdev.mat301275.facemorph.modules.camera;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayOutputStream;

import it.unipr.advmobdev.mat301275.facemorph.R;
import it.unipr.advmobdev.mat301275.facemorph.modules.gallery.GalleryCallback;
import it.unipr.advmobdev.mat301275.facemorph.modules.gallery.GalleryFragment;
import it.unipr.advmobdev.mat301275.facemorph.modules.home.HomeFragmentDirections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CameraFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CameraFragment extends Fragment implements CameraBridgeViewBase.CvCameraViewListener2 {

    private CameraController controller = new CameraController(this);
    private CameraBridgeViewBase mOpenCvCameraView;

    private Button takePictureButton;
    private Button selectFromGalleryButton;

    private Mat mRgba;
    private Mat mRgbaF;
    private Mat mRgbaT;
    private Mat mRgbaSnap;

    public CameraFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CameraFragment.
     */
    public static CameraFragment newInstance() {
        CameraFragment fragment = new CameraFragment();
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
        CameraCallback callback = CameraFragmentArgs.fromBundle(getArguments()).getCallback();
        controller.setCallback(callback);
        return inflater.inflate(R.layout.fragment_camera, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mOpenCvCameraView = getView().findViewById(R.id.camera_preview);
        takePictureButton = (Button) getView().findViewById(R.id.take_a_photo_button);
        selectFromGalleryButton = (Button) getView().findViewById(R.id.select_from_gallery_button);

        selectFromGalleryButton.setOnClickListener(v -> {
            controller.galleryPressed();
        });

        takePictureButton.setOnClickListener( buttonView -> {
            if (mRgba != null) {
                this.controller.photoTaken(mRgbaSnap);
            }
        });

        controller.viewCreated();
        mPermissionResult.launch(Manifest.permission.CAMERA);
    }

    public void hideSelectFromGallery() {
        selectFromGalleryButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        mRgbaF = new Mat(height, width, CvType.CV_8UC4);
        mRgbaT = new Mat(width, width, CvType.CV_8UC4);
        mRgbaSnap = new Mat(height, width, CvType.CV_8UC4);
    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        //Frame is from the front camera
        mRgba = inputFrame.rgba();
        Core.flip(mRgba, mRgbaF, 1 );
        Core.transpose(mRgbaF, mRgbaT);
        Core.flip(mRgbaT,mRgbaF,1);
        Imgproc.resize(mRgbaF, mRgba, mRgba.size(), 0,0, 0);

        //Creates a snap with only up-to-date images
        mRgba.copyTo(mRgbaSnap);
        return mRgbaSnap;
    }

    private final ActivityResultLauncher<String> mPermissionResult = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            result -> {
                if(result) {
                    controller.cameraPermissionAccepted();
                } else {
                    Toast.makeText(getActivity(), "Camera permission denied", Toast.LENGTH_SHORT).show();
                    controller.cameraPermissionDenied();
                }
            });

    public void enableCamera() {
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
        mOpenCvCameraView.enableView();
    }

    public void popFragment() {
        NavHostFragment.findNavController(this).popBackStack();
    }


    public void quit() {
        NavHostFragment.findNavController(this).popBackStack();
    }

    public void navigateToGallery(GalleryCallback callback) {
        CameraFragmentDirections.ActionCameraFragmentToGalleryFragment action = CameraFragmentDirections.actionCameraFragmentToGalleryFragment(callback);
        NavHostFragment.findNavController(this).navigate(action);
    }

}