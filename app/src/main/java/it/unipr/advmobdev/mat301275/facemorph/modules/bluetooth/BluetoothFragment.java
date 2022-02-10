package it.unipr.advmobdev.mat301275.facemorph.modules.bluetooth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

        hostButton.setOnClickListener(v -> {
            controller.hostBluetooth();
        });

        joinButton.setOnClickListener(v -> {
            controller.joinBluetooth();
        });

        cancelButton.setOnClickListener(v -> {
            controller.cancelClicked();
        });

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
        hostButton.setEnabled(false);
        joinButton.setEnabled(false);
    }

    public void enableInteraction() {
        hostButton.setEnabled(true);
        joinButton.setEnabled(true);
    }
}