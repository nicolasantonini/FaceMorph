package it.unipr.advmobdev.mat301275.facemorph.modules.result;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.slider.Slider;

import it.unipr.advmobdev.mat301275.facemorph.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultFragment extends Fragment {

    ResultController controller = new ResultController(this);
    private Slider alphaSlider;
    private Slider trianglesSlider;
    private ImageView resultImageView;

    public ResultFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ResultFragment.
     */
    public static ResultFragment newInstance() {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        ResultAttachment attachment =  ResultFragmentArgs.fromBundle(getArguments()).getImages();
        controller.setAttachment(attachment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        alphaSlider = (Slider) getView().findViewById(R.id.alpha_slider);
        alphaSlider.setValue(0.00f);
        alphaSlider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                controller.alphaChanged(slider.getValue(), getContext());
            }
        });

        trianglesSlider = (Slider) getView().findViewById(R.id.triangles_slider);
        trianglesSlider.setValue(100.0f);
        trianglesSlider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                controller.trianglesChanged(slider.getValue(), getContext());
            }
        });

        resultImageView = (ImageView) getView().findViewById(R.id.result_image_view);

        controller.viewCreated(getContext());

    }

    @Override
    public void onStart() {
        super.onStart();
        controller.viewCreated(getContext());
    }

    public void setImage(Bitmap bitmap) {
        Handler mainHandler = new Handler(getContext().getMainLooper());
        Runnable myRunnable = () -> resultImageView.setImageBitmap(bitmap);
        mainHandler.post(myRunnable);
    }

    public void displayToast(String string) {
        Handler mainHandler = new Handler(getContext().getMainLooper());
        Runnable myRunnable = () -> Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
        mainHandler.post(myRunnable);
    }
}