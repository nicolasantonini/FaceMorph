package it.unipr.advmobdev.mat301275.facemorph.modules.bluetooth;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import it.unipr.advmobdev.mat301275.facemorph.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BluetoothFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BluetoothFragment extends BottomSheetDialogFragment {

    BluetoothController controller = new BluetoothController(this);

    private Button cancelButton;
    private Button hostButton;
    private Button joinButton;
    private ClipDrawable mClipDrawable;

    private ActivityResultContracts.RequestMultiplePermissions requestMultiplePermissionsContract;
    private ActivityResultLauncher<String[]> multiplePermissionActivityResultLauncher;

    public BluetoothFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BluetoothFragment.
     */
    public static BluetoothFragment newInstance() {
        BluetoothFragment fragment = new BluetoothFragment();
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
        BluetoothCallback callback = BluetoothFragmentArgs.fromBundle(getArguments()).getCallback();
        controller.setCallback(callback);
        return inflater.inflate(R.layout.fragment_bluetooth, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cancelButton = (Button) getView().findViewById(R.id.exit_bluetooth_button);
        hostButton = (Button) getView().findViewById(R.id.host_button);
        joinButton = (Button) getView().findViewById(R.id.join_button);

        LayerDrawable layerDrawable = (LayerDrawable) getView().findViewById(R.id.exit_bluetooth_button).getBackground();
        mClipDrawable = (ClipDrawable) layerDrawable.findDrawableByLayerId(R.id.clip_drawable);

        hostButton.setOnClickListener(v -> {
            controller.hostBluetooth();
        });

        joinButton.setOnClickListener(v -> {
            controller.joinBluetooth();
        });

        cancelButton.setOnClickListener(v -> {
            controller.cancelClicked();
        });

        mPermissionResult.launch(Manifest.permission.ACCESS_FINE_LOCATION);

    }

    public void popFragment() {
        NavHostFragment.findNavController(this).popBackStack();
    }

    @Override
    public void onStop() {
        super.onStop();
        controller.viewGone();
    }

    public void disableInteraction() {
        Handler mainHandler = new Handler(getContext().getMainLooper());
        Runnable myRunnable = () -> {
            hostButton.setEnabled(false);
            joinButton.setEnabled(false);
            cancelButton.setEnabled(false);
        };
        mainHandler.post(myRunnable);
    }

    public void enableInteraction() {
        Handler mainHandler = new Handler(getContext().getMainLooper());
        Runnable myRunnable = () -> {
            hostButton.setEnabled(true);
            joinButton.setEnabled(true);
            cancelButton.setEnabled(true);
        };
        mainHandler.post(myRunnable);
    }

    public void displayToast(String string) {
        Handler mainHandler = new Handler(getContext().getMainLooper());
        Runnable myRunnable = () -> Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
        mainHandler.post(myRunnable);
    }

    public void setProgress(int progress) {
        Handler mainHandler = new Handler(getContext().getMainLooper());
        Runnable myRunnable = () -> cancelButton.setText(String.valueOf(progress) + "%");
        mainHandler.post(myRunnable);
    }

    public void setInitializing() {
        Handler mainHandler = new Handler(getContext().getMainLooper());
        Runnable myRunnable = () -> cancelButton.setText(R.string.initializing);
        mainHandler.post(myRunnable);
    }

    public void setFailed() {
        Handler mainHandler = new Handler(getContext().getMainLooper());
        Runnable myRunnable = () -> cancelButton.setText(R.string.cancel);
        mainHandler.post(myRunnable);
    }

    private final ActivityResultLauncher<String> mPermissionResult = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            result -> {
                if(result) {

                } else {
                    Toast.makeText(getActivity(), "BLE permission denied", Toast.LENGTH_SHORT).show();
                    controller.blePermissionsDenied();
                }
            });


}