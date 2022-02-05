package it.unipr.advmobdev.mat301275.facemorph.modules.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import it.unipr.advmobdev.mat301275.facemorph.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    SettingsController controller = new SettingsController(this);

    private Button eraseButton;
    private Button quitButton;
    protected ProgressBar mProgressBar;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SettingsFragment.
     */
    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
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
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eraseButton = (Button) getView().findViewById(R.id.erase_data_button);
        quitButton = (Button) getView().findViewById(R.id.quit_button);
        mProgressBar = (ProgressBar) getView().findViewById(R.id.settings_progress_bar);
        eraseButton.setOnClickListener( buttonView -> {
            this.controller.eraseClicked();
        });

        quitButton.setOnClickListener(buttonView -> {
            this.controller.quitClicked();
        });
    }

    public void displayToast(String string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
    }

    public void disableInteraction() {
        eraseButton.setClickable(false);
        eraseButton.setAlpha(0.8f);
        quitButton.setClickable(false);
        quitButton.setAlpha(0.8f);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void enableInteraction() {
        eraseButton.setClickable(true);
        eraseButton.setAlpha(1.0f);
        quitButton.setClickable(true);
        quitButton.setAlpha(1.0f);
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    public void navigateToLogin() {
        NavHostFragment.findNavController(this).navigate(R.id.action_settingsFragment_to_loginFragment);
    }
}