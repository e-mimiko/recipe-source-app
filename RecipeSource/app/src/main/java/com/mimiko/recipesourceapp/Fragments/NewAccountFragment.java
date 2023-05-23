package com.mimiko.recipesourceapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.mimiko.recipesourceapp.R;
import com.mimiko.recipesourceapp.Tools;

public class NewAccountFragment extends Fragment {

    EditText nameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    Button createAccountBtn;
    TextView loginBtnTextView;
    ProgressBar progressBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_new_account, container, false);
        //set views
        nameEditText = rootView.findViewById(R.id.enter_name);
        emailEditText = rootView.findViewById(R.id.enter_email);
        passwordEditText = rootView.findViewById(R.id.enter_password);
        confirmPasswordEditText = rootView.findViewById(R.id.confirm_password);
        createAccountBtn = rootView.findViewById(R.id.create_account_btn);
        loginBtnTextView = rootView.findViewById(R.id.tv_login_btn);
        progressBar = rootView.findViewById(R.id.progressBar);
        createAccountBtn.setOnClickListener(v -> createAccount());
        //call create login fragment on button click
        loginBtnTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.LoginCreateAccountFragmentContainer, new LoginFragment())
                .commit();
            }
        });

        return rootView;
    }

    //create a new account with firebase
    void createAccount(){
        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        boolean isValidated = validateData(name, email, password, confirmPassword);
        if (!isValidated){
            //stop here if not valid
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        createAccountBtn.setVisibility(View.GONE);
        createAccountInFirebase(name, email, password);
    }

    //calls the Firebase API and uses callback to ensure result is returned before continuing in the program
    void createAccountInFirebase(String name,String email, String password){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(getActivity(),
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            //create new account is complete
                            //TODO: add this string to tool class
                            Toast.makeText(getActivity(), "Successfully Created Account. Please login.", Toast.LENGTH_SHORT).show();
                            //save user name to be used in Main Page Fragment
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            UserProfileChangeRequest profileCreation = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name).build();
                            user.updateProfile(profileCreation)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                //start the login fragment if the account creation was successful
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    getActivity().getSupportFragmentManager().beginTransaction()
                                                            .replace(R.id.LoginCreateAccountFragmentContainer, new LoginFragment())
                                                            .commit();
                                                }
                                            });
                        }
                        else{
                            //failure to create new account
                            //TODO: add this string to tool class
                            progressBar.setVisibility(View.GONE);
                            createAccountBtn.setVisibility(View.VISIBLE);
                            Toast.makeText(getActivity(), "Account was not created.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    //simple validation to ensure everything is as it should be
    boolean validateData(String name, String email, String password, String confirmPassword){
        if (name.isEmpty()){
            nameEditText.setError(Tools.invalidNameMessage);
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError(Tools.invalidEmailMessage);
            return false;
        }
        //password length must be greater than six
        if (passwordEditText.length() < 6){
            passwordEditText.setError(Tools.invalidPasswordMessage);
            return false;
        }
        if (!password.equals(confirmPassword)){
            confirmPasswordEditText.setError(Tools.passwordMismatchMessage);
            return false;
        }

        return true;
    }

//end
}