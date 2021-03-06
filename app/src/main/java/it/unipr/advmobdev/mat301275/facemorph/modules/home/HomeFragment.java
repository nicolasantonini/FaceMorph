package it.unipr.advmobdev.mat301275.facemorph.modules.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Parcel;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import it.unipr.advmobdev.mat301275.facemorph.R;
import it.unipr.advmobdev.mat301275.facemorph.modules.camera.CameraCallback;
import it.unipr.advmobdev.mat301275.facemorph.modules.gallery.GalleryCallback;
import it.unipr.advmobdev.mat301275.facemorph.modules.preview.PreviewAttachment;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    HomeController controller = new HomeController(this);

    private FloatingActionButton cameraButton = null;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cameraButton = (FloatingActionButton) getView().findViewById(R.id.camera_button);
        cameraButton.setOnClickListener(v -> controller.cameraPressed());
    }

    public void navigateToCamera(CameraCallback callback) {
        HomeFragmentDirections.ActionHomeFragmentToCameraFragment action = HomeFragmentDirections.actionHomeFragmentToCameraFragment(callback);
        NavHostFragment.findNavController(this).navigate(action);
    }

    public void navigateToSettings() {
        NavHostFragment.findNavController(this).navigate(R.id.action_homeFragment_to_settingsFragment);
    }

    public void navigateToGallery(GalleryCallback callback) {
        HomeFragmentDirections.ActionHomeFragmentToGalleryFragment action = HomeFragmentDirections.actionHomeFragmentToGalleryFragment(callback);
        NavHostFragment.findNavController(this).navigate(action);
    }

    public void navigateToPreview(PreviewAttachment previewAttachment) {
        HomeFragmentDirections.ActionHomeFragmentToPreviewFragment action = HomeFragmentDirections.actionHomeFragmentToPreviewFragment(previewAttachment);
        NavHostFragment.findNavController(this).navigate(action);
    }

    public void navigateToPreview() {
        NavHostFragment.findNavController(this).navigate(R.id.action_homeFragment_to_previewFragment);
    }

    public void popFragment() {
        NavHostFragment.findNavController(this).popBackStack();
    }

    public void displayToast(String string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
    }
}