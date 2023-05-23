package com.mimiko.recipesourceapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.mimiko.recipesourceapp.Listeners.MyRecipeListener;
import com.mimiko.recipesourceapp.Listeners.RecipeCardClickListener;

import com.mimiko.recipesourceapp.Adapters.RecipeRecyclerViewAdapter;
import com.mimiko.recipesourceapp.Models.RecipeInformation;
import com.mimiko.recipesourceapp.R;
import com.mimiko.recipesourceapp.Tools;
import com.mimiko.recipesourceapp.ViewModels.FragmentCommsViewModel;

import java.util.List;


public class GeneratedRecipesFragment extends Fragment {


    //My Attributes
    RecyclerView recyclerViewRecipes;
    RecipeRecyclerViewAdapter recipeAdapter;
    FragmentCommsViewModel fragmentCommsViewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get view model
        fragmentCommsViewModel = new ViewModelProvider(requireActivity()).get(FragmentCommsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_generated_recipes, container, false);

        //when response gotten, show in recycler
        recyclerViewRecipes = rootView.findViewById(R.id.generated_recipes_rv);
        recyclerViewRecipes.setHasFixedSize(true);
        recyclerViewRecipes.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        //call recycler adapter
        //uses callback methods to ensure that firebase asynchronous task is gotten
        //before the code continues executing
        recipeAdapter = new RecipeRecyclerViewAdapter(getActivity(), fragmentCommsViewModel.getRecipeList(),
                getString(R.string.add_to_my_recipes), new RecipeCardClickListener() {
            @Override
            public void onViewRecipeClick(View v, int position) {//on click, opens up fragment with more info
                //pass recipe info to the called fragment
                MaterialButton btn = (MaterialButton) v;
                if (btn.getId() == R.id.view_this_recipe_button){
                    //todo:add this to tools
                    fragmentCommsViewModel.setPosition(position);
                    fragmentCommsViewModel.setGeneratedFragment(true);
                    getActivity().getSupportFragmentManager()
                            .beginTransaction().replace(R.id.fragments_container, new RecipeInfoFragment())
                            .addToBackStack(null).commit();
                }
            }

            @Override
            public void onAddRemoveRecipeClick(View v, int position) {//adds recipe to lis
                //todo: check that its not already in there
                MaterialButton btn = (MaterialButton) v;
                if (btn.getId() == R.id.addRemove_to_my_recipes_button){
                    if (btn.getText() == getString(R.string.add_to_my_recipes)){
                        //adding an recipe to firebase
                        Tools.saveMyRecipeToFirebase(fragmentCommsViewModel.getRecipeList().get(position), new MyRecipeListener() {
                            @Override
                            public void onComplete(boolean isSuccessful) {
                                if (isSuccessful){
                                    btn.setText(getString(R.string.remove_recipe));
                                }else{
                                    //Inform user
                                    Toast.makeText(getActivity(), "Recipe was not added", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else if (btn.getText() == getString(R.string.remove_recipe)){//removing a recipe from firebase
                        RecipeInformation recipeToRemove = fragmentCommsViewModel.getRecipeList().get(position);
                        Tools.removeRecipeFromFirebase(recipeToRemove.getId(), new MyRecipeListener() {
                            @Override
                            public void onComplete(boolean isSuccessful) {
                                if (isSuccessful){
                                    btn.setText(getString(R.string.add_to_my_recipes));
                                }else{
                                    //Inform user
                                    Toast.makeText(getActivity(), "Recipe was not removed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        //
                    }
                    //todo:add toast
                }

            }
        });
        recyclerViewRecipes.setAdapter(recipeAdapter);
        return rootView;
    }
}