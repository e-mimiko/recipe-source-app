//Sources: https://www.youtube.com/watch?v=jzVmjU2PFbg&ab_channel=EasyTuto
package com.mimiko.recipesourceapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;


import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.mimiko.recipesourceapp.Fragments.AboutFragment;
import com.mimiko.recipesourceapp.Fragments.GenerateRecipeFragment;
import com.mimiko.recipesourceapp.Fragments.MainPageFragment;
import com.mimiko.recipesourceapp.Fragments.ViewPastRecipesFragment;
import com.mimiko.recipesourceapp.Listeners.BooleanListener;
import com.mimiko.recipesourceapp.Listeners.StringListener;
import com.mimiko.recipesourceapp.R;
import com.mimiko.recipesourceapp.Tools;
import com.mimiko.recipesourceapp.ViewModels.FragmentCommsViewModel;


import java.util.Objects;

import io.reactivex.rxjava3.plugins.RxJavaPlugins;

public class MainActivity extends AppCompatActivity {
    private String TAG = "Main Activity-------------->";
    private DrawerLayout drawerLayout;
    private MaterialToolbar appBar;
    Fragment generateRecipeFragment;
    Fragment mainPageFragment;
    Fragment viewPastRecipesFragment;
    Fragment aboutFragment;
    FragmentCommsViewModel fragmentCommsViewModel;
    private  FirebaseAuth firebaseAuth;
    Fragment fragment;
    int fragmentID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        // Get user name

        //check if terms and conditions filled
        Tools.hasAgreedToTerms(new BooleanListener() {
            @Override
            public void onComplete(boolean hasAgreed) {
                if (!hasAgreed){
                    Tools.getTermsAndConditions(new StringListener() {
                        @Override
                        public void onComplete(String message) {
                            if (!Objects.equals(message, "Error")){
                                callDialog(message);//if not agreed to, show dialog box
                            }
                        }
                    });
                }
            }
        });
        //call default fragment, or previously stored fragment
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, new MainPageFragment()).commit();
        }else{
            Fragment frag = getSupportFragmentManager().findFragmentById(savedInstanceState.getInt("current_fragment"));
            if (frag != null){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, frag).commit();
            }
        }

        //catch RXJava error from firebase paging adapter
        RxJavaPlugins.setErrorHandler(throwable -> {
            //todo:get rid of disposables
            Log.e(TAG, "Error thrown RxJava!");
        });
        //add view model for fragments to communicate
        fragmentCommsViewModel = new ViewModelProvider(this).get(FragmentCommsViewModel.class);

        drawerLayout = findViewById(R.id.drawer_layout);
        appBar = findViewById(R.id.app_bar);
        NavigationView navdrawer = findViewById(R.id.nav_drawer);
        //remove scroll on app action bar
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) appBar.getLayoutParams();
        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_NO_SCROLL);


        //Fragment initialization
        generateRecipeFragment = new GenerateRecipeFragment();
        viewPastRecipesFragment = new ViewPastRecipesFragment();
        mainPageFragment = new MainPageFragment();
        aboutFragment = new AboutFragment();
        //sets the action bar and navigation drawer for the fragments
        setSupportActionBar(appBar);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, appBar,
                R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navdrawer.setNavigationItemSelectedListener(navDrawerItemSelectedListener);
    }


    //Shows a dialog to user before anything else
    private void callDialog(String message){
        new AlertDialog.Builder(this)
                .setTitle("Terms And Conditions")
                .setMessage(message)
                .setPositiveButton("Agree", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Tools.setAgreedToTerms(new StringListener() {
                            @Override
                            public void onComplete(String message) {
                                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            }
                        });
                    }
                })
                .setNegativeButton("Disagree", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();//close app if user disagrees
                    }
                })
                .setCancelable(false)
                .show();
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //Main driver for the fragments, calls the fragments on click
    NavigationView.OnNavigationItemSelectedListener navDrawerItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            boolean isLogout = false;
            fragmentID = item.getItemId();//id here is from menu folder
            if (fragmentID == R.id.generate_recipe) {
                fragment = generateRecipeFragment;
                appBar.setSubtitle(R.string.generate_recipe);//change app bar subheading
            }
            else if (fragmentID == R.id.view_past_recipes) {
                fragment = viewPastRecipesFragment;
                appBar.setSubtitle(R.string.past_recipes);
            }
            else if (fragmentID == R.id.about_page) {
                fragment = aboutFragment;
                appBar.setSubtitle(R.string.about);
            }
            else if (fragmentID == R.id.logout) {
                isLogout = true;
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                firebaseAuth.signOut();
                finish();
            }else {
                fragment = mainPageFragment;
                appBar.setSubtitle(R.string.homepage);
            }
            if (!isLogout){
                //pop backstack before navigating back to ensure multiple instances are not left in background
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragments_container, fragment);
                transaction.commit();
                //close navigation drawer on the left
                drawerLayout.closeDrawer(GravityCompat.START);
            }
            return true;
        }
    };
    //end


    //save the current fragment and restore onCreate
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (fragmentID != R.id.logout){
            outState.putInt("current_fragment",fragmentID);
        }

    }
}