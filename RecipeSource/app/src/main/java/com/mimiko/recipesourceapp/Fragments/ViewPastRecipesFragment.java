package com.mimiko.recipesourceapp.Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.mimiko.recipesourceapp.Adapters.RecipeRecyclerViewAdapter;
import com.mimiko.recipesourceapp.Listeners.MyRecipeGetListener;
import com.mimiko.recipesourceapp.Listeners.MyRecipeListener;
import com.mimiko.recipesourceapp.Listeners.RecipeCardClickListener;
import com.mimiko.recipesourceapp.Models.RecipeInformation;
import com.mimiko.recipesourceapp.R;
import com.mimiko.recipesourceapp.Tools;
import com.mimiko.recipesourceapp.ViewModels.FragmentCommsViewModel;

import java.util.List;

public class ViewPastRecipesFragment extends Fragment {
    static final String TAG = "View Past Recipes Fragment------->";
    RecyclerView recyclerViewRecipes;
    TextView emptyView;
    String remove_recipe;
    RecipeRecyclerViewAdapter recipeAdapter;
    FragmentCommsViewModel fragmentCommsViewModel;

    public ViewPastRecipesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentCommsViewModel = new ViewModelProvider(requireActivity()).get(FragmentCommsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_view_past_recipes, container, false);
        recyclerViewRecipes = rootView.findViewById(R.id.past_recipes_rv);
        recyclerViewRecipes.setHasFixedSize(true);
        recyclerViewRecipes.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recyclerViewRecipes.setItemAnimator(null);
        emptyView = rootView.findViewById(R.id.tv_empty_ingredient);

        return rootView;
    }



    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    public void onStart() {
        super.onStart();
        //get recipeList from Firebase
        Tools.getMyRecipesToFirebase(new MyRecipeGetListener() {
            @Override
            public void onComplete(List<RecipeInformation> myRecipes) {
                updateUI(myRecipes);
            }

        });
    }


    //function to check if a list of recipes is empty
    //will dislay an empty textview instead
    boolean isEmptyRecipeList(List<RecipeInformation> recipeList){
        if (recipeList == null || recipeList.isEmpty()){
            Log.e(TAG, "No Recipes Found");
            //hide recycler view and show textview with empty??
            recyclerViewRecipes.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            return true;
        }
        return false;
    }

    //uses the view model to populate the recycler view and adapter
    //adapter uses callbacks to ensure firebase calls are well timed
    void updateUI(List<RecipeInformation> recipeList){
        boolean isEmptyList = isEmptyRecipeList(recipeList);
        if (!isEmptyList){
            emptyView.setVisibility(View.GONE);
            recyclerViewRecipes.setVisibility(View.VISIBLE);
            //update fragment view model
            fragmentCommsViewModel.setMyRecipesList(recipeList);
            if(isAdded()){
                remove_recipe = getActivity().getString(R.string.remove_recipe);
            }
            //set recyclerview
            recipeAdapter = new RecipeRecyclerViewAdapter(getActivity(), recipeList,
                    remove_recipe, new RecipeCardClickListener() {
                @Override
                public void onViewRecipeClick(View v, int position) {
                    //pass recipe info to the called fragment
                    MaterialButton btn = (MaterialButton) v;
                    if (btn.getId() == R.id.view_this_recipe_button){
                        fragmentCommsViewModel.setMyRecipesPosition(position);
                        fragmentCommsViewModel.setGeneratedFragment(false);
                        if(isAdded()) {
                            getActivity().getSupportFragmentManager()
                                    .beginTransaction().replace(R.id.fragments_container, new RecipeInfoFragment())
                                    .addToBackStack(null).commit();
                        }
                    }
                }

                @Override
                public void onAddRemoveRecipeClick(View v, int position) {
                    //todo: check that its not already in there
                    MaterialButton btn = (MaterialButton) v;
                             if (btn.getText() == getString(R.string.remove_recipe)){
                            RecipeInformation recipeToRemove = fragmentCommsViewModel.getMyRecipesList().get(position);
                            Tools.removeRecipeFromFirebase(recipeToRemove.getId(), new MyRecipeListener() {
                                @Override
                                public void onComplete(boolean isSuccessful) {
                                    if (isSuccessful){
                                        Toast.makeText(getActivity(), "Recipe Removed", Toast.LENGTH_SHORT).show();
                                        fragmentCommsViewModel.getMyRecipesList().remove(position);
                                        recipeAdapter.notifyItemChanged(position);
                                        isEmptyRecipeList(recipeList);
                                    }else{
                                        //Inform user
                                        Toast.makeText(getActivity(), "Recipe was not removed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            //
                        }
                    }
            });
            //todo: is this the right place to set the adapter. Check if onstart better
            recyclerViewRecipes.setAdapter(recipeAdapter);
        }
    }


    //end
}