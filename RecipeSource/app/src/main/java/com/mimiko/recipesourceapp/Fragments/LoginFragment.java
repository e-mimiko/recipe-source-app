package com.mimiko.recipesourceapp.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.mimiko.recipesourceapp.Activities.LoginCreateAccountActivity;
import com.mimiko.recipesourceapp.Activities.MainActivity;
import com.mimiko.recipesourceapp.R;
import com.mimiko.recipesourceapp.Tools;

import java.util.Objects;

//TODO: Make login button bigger, had to click multiple times.
//TODO: Make sure when text is being entered in layout, that everything shifts up and it shows the button as well, not covered by keyboard

public class LoginFragment extends Fragment {
    TextInputEditText emailEditText, passwordEditText;
    Button loginBtn;
    TextView createAccountBtnTextView;
    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        emailEditText = rootView.findViewById(R.id.enter_email);
        passwordEditText = rootView.findViewById(R.id.enter_password);
        loginBtn = rootView.findViewById(R.id.login_btn);
        createAccountBtnTextView = rootView.findViewById(R.id.tv_create_account_btn);
        progressBar = rootView.findViewById(R.id.progressBar);

        loginBtn.setOnClickListener(v -> loginUser() );
        //TODO: Make sure this does not create a new instance
        createAccountBtnTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call other fragment
                //remove from stack
                //todo: remove any backstack and ensure supportManager is not null
                getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.LoginCreateAccountFragmentContainer, new NewAccountFragment())
                .commit();
            }
        });
        return rootView;
    }

    void loginUser(){
        String email = Objects.requireNonNull(emailEditText.getText()).toString();
        String password = Objects.requireNonNull(passwordEditText.getText()).toString();

        boolean isValidated = validateData(email, password);
        if (!isValidated){
            //stop here if not valid
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        loginBtn.setVisibility(View.GONE);
        loginAccountInFirebase(email, password);
    }

    void loginAccountInFirebase(String email, String password){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    //login successful
                    //TODO: Add if statement if message not verified - optional
                    //Go to Main Activity
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    if (getActivity() != null){
                        getActivity().finish();
                    }
                } else {
                    //login failed
                    //TODO: Put this in a interface or class so you can share it among activites
                    progressBar.setVisibility(View.GONE);
                    loginBtn.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //TODO: Put this in a interface or class so you can share it among activites
    boolean validateData(String email, String password){

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError(Tools.invalidEmailMessage);
            return false;
        }
        //password length must be greater than six
        if (passwordEditText.length() < 6){
            passwordEditText.setError(Tools.invalidPasswordMessage);
            return false;
        }
        return true;
    }


//end
}