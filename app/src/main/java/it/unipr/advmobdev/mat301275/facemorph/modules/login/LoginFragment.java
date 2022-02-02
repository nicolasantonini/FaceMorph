package it.unipr.advmobdev.mat301275.facemorph.modules.login;

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
import android.widget.Toast;

import it.unipr.advmobdev.mat301275.facemorph.R;
import it.unipr.advmobdev.mat301275.facemorph.modules.preview.PreviewFragment;
import it.unipr.advmobdev.mat301275.facemorph.modules.splash.SplashController;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    LoginController controller = new LoginController(this);
    private Button loginButton;
    private Button signupButton;
    private EditText emailEditText;
    private EditText passwordEditText;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoginFragment.
     */
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginButton = (Button) getView().findViewById(R.id.login_button);
        signupButton = (Button) getView().findViewById(R.id.signup_button);
        emailEditText = (EditText) getView().findViewById(R.id.mail_edit_text);
        passwordEditText = (EditText) getView().findViewById(R.id.password_edit_text);

        loginButton.setOnClickListener( buttonView -> {
            this.controller.loginClicked();
        });

        signupButton.setOnClickListener(buttonView -> {
            this.controller.signUpClicked();
        });
    }

    public String getEmail() {
        return emailEditText.getText().toString();
    }

    public String getPassword() {
        return passwordEditText.getText().toString();
    }

    public void displayToast(String string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
    }

    public void disableInteraction() {
        signupButton.setEnabled(false);
        loginButton.setEnabled(false);
        passwordEditText.setEnabled(false);
        emailEditText.setEnabled(false);
    }

    public void enableInteraction() {
        signupButton.setEnabled(true);
        loginButton.setEnabled(true);
        passwordEditText.setEnabled(true);
        emailEditText.setEnabled(true);
    }

    public void navigateToHome() {
        NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_homeFragment);
    }


}