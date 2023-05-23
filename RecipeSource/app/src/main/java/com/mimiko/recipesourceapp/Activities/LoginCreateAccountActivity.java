package com.mimiko.recipesourceapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.mimiko.recipesourceapp.Fragments.LoginFragment;
import com.mimiko.recipesourceapp.Fragments.NewAccountFragment;
import com.mimiko.recipesourceapp.R;
import com.mimiko.recipesourceapp.Tools;

import java.util.Objects;

public class LoginCreateAccountActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_createaccount);


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.LoginCreateAccountFragmentContainer, new LoginFragment());
        transaction.commit();

    }




//end
}