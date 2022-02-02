package it.unipr.advmobdev.mat301275.facemorph.modules.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import it.unipr.advmobdev.mat301275.facemorph.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    HomeController controller = new HomeController(this);

    private FloatingActionButton cameraButton = null;
    private FloatingActionButton galleryButton = null;
    private FloatingActionButton settingsButton = null;

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
        galleryButton = (FloatingActionButton) getView().findViewById(R.id.gallery_button);
        settingsButton = (FloatingActionButton) getView().findViewById(R.id.settings_button);

        cameraButton.setOnClickListener(v -> controller.cameraPressed());
        galleryButton.setOnClickListener(v -> controller.galleryPressed());
        settingsButton.setOnClickListener(v -> controller.settingsPressed());
    }

    public void navigateToCamera() {
        NavHostFragment.findNavController(this).navigate(R.id.action_homeFragment_to_cameraFragment);
    }

    public void navigateToSettings() {
        NavHostFragment.findNavController(this).navigate(R.id.action_homeFragment_to_settingsFragment);
    }

    public void navigateToGallery() {
        NavHostFragment.findNavController(this).navigate(R.id.action_homeFragment_to_galleryFragment);
    }

    public void navigateToPreview() {
        NavHostFragment.findNavController(this).navigate(R.id.action_homeFragment_to_previewFragment);
    }
}