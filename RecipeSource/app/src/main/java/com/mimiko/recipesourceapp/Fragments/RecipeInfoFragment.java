package com.mimiko.recipesourceapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mimiko.recipesourceapp.Models.RecipeInformation;
import com.mimiko.recipesourceapp.R;
import com.mimiko.recipesourceapp.ViewModels.FragmentCommsViewModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeInfoFragment extends Fragment {
    //This fragment sets the different views based on a recipeInformation object passed in.
    FragmentCommsViewModel fragmentCommsViewModel;
    RecipeInformation recipeToDisplay;
    ImageView recipeImage;
    TextView recipeTitle, servingsTextView, inMinutesTextView, usedIngredientNumberTextView,
            missedIngredientNumberTextView, instructionsStepsTextView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentCommsViewModel = new ViewModelProvider(requireActivity()).get(FragmentCommsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recipe_info, container, false);
        recipeImage = rootView.findViewById(R.id.recipeImage);
        recipeTitle = rootView.findViewById(R.id.recipeTitle);
        servingsTextView = rootView.findViewById(R.id.servingsTextView);
        inMinutesTextView = rootView.findViewById(R.id.inMinutesTextView);
        usedIngredientNumberTextView = rootView.findViewById(R.id.usedIngredientNumberTextView);
        missedIngredientNumberTextView = rootView.findViewById(R.id.missedIngredientNumberTextView);
        instructionsStepsTextView = rootView.findViewById(R.id.instructionsStepsTextView);


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        //uses the view model to pass and get information from other fragment.
        //todo: ensure this is the best place to set it. Maybe on restart is better?
        if (fragmentCommsViewModel.isGeneratedFragment()){
            recipeToDisplay = fragmentCommsViewModel.getRecipeList().get(fragmentCommsViewModel.getPosition());
        }else{
            recipeToDisplay = fragmentCommsViewModel.getMyRecipesList().get(fragmentCommsViewModel.getMyRecipesPosition());
        }
        setRecipeImage();
        setRecipeTitle();
        setRecipeServings();
        setRecipeReadyInMinutes();
        setUsedIngredient();
        setMissedIngredient();
        setRecipeInstructions();
    }

    //todo: get layout and set visibility to gone when a view isnt avaialable/i.e is empty
    //also add view for source name and link

    //function to set Recipe name
    private void setRecipeImage(){
        Picasso.get().load(recipeToDisplay.getImage()).placeholder(R.drawable.recipe_image_placeholder)
                .into(recipeImage);
    }
    //function to set Recipe title
    private void setRecipeTitle(){
        recipeTitle.setText(recipeToDisplay.getTitle());
    }
    //function to set the number of servings
    private void setRecipeServings(){
        servingsTextView.setText(getString(R.string.servings_amount, recipeToDisplay.getRecipeInformationBulk().getServings()));
    }
    //function to set how long a recipe takes to make
    private void setRecipeReadyInMinutes(){
        inMinutesTextView.setText(getString(R.string.recipe_ready_time, recipeToDisplay.getRecipeInformationBulk().getReadyInMinutes()));
    }
    //function to set ingredients used
    private void setUsedIngredient(){
        String usedIngredient = recipeToDisplay.getUsedIngredientsToString();
        if (usedIngredient.isEmpty()){
            usedIngredient = "None";
        }
        usedIngredientNumberTextView.setText(usedIngredient);
    }
    //function to set missed ingredients
    private void setMissedIngredient(){
        String missedIngredient = recipeToDisplay.getMissedIngredientsToString();
        if (missedIngredient.isEmpty()){
            missedIngredient = "None";
        }
        missedIngredientNumberTextView.setText(missedIngredient);
    }
    //function to set set the recipe steps
    private void setRecipeInstructions(){
        String steps = recipeToDisplay.getAllInstructions();//retuns a stringed text of all the steps
        if (steps.isEmpty()){
            //show source location
            //remove visibility
        }
        else{
            instructionsStepsTextView.setText(steps);
        }
    }



    //---end
}