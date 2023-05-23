package com.mimiko.recipesourceapp.ViewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.mimiko.recipesourceapp.Models.IngredientModel;
import com.mimiko.recipesourceapp.Models.RecipeInformation;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FragmentCommsViewModel extends ViewModel {

    //used to communicate between fragments
    boolean isGeneratedFragment;

    Set<String> matchedRecipes;
    List<RecipeInformation> recipeList;
    List<RecipeInformation> myRecipesList;
    private int position;
    private int myRecipesPosition;

    public FragmentCommsViewModel() {
        this.recipeList = new ArrayList<>();
        this.position = -1;
        this.myRecipesList = new ArrayList<>();
        this.myRecipesPosition = -1;
        this.isGeneratedFragment = false;
        this.matchedRecipes = new HashSet<>();
    }

    //For Generated Recipes Fragment

    public List<RecipeInformation> getRecipeList() {
        return recipeList;
    }

    public void setRecipeList(List<RecipeInformation> recipeList) {
        this.recipeList = recipeList;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }


    //For View Recipe's Fragment
    public List<RecipeInformation> getMyRecipesList() {
        return myRecipesList;
    }

    public void setMyRecipesList(List<RecipeInformation> myRecipesList) {
        this.myRecipesList = myRecipesList;
    }

    public int getMyRecipesPosition() {
        return myRecipesPosition;
    }

    public void setMyRecipesPosition(int myRecipesPosition) {
        this.myRecipesPosition = myRecipesPosition;
    }

    public boolean isGeneratedFragment() {
        return isGeneratedFragment;
    }

    public void setGeneratedFragment(boolean generatedFragment) {
        isGeneratedFragment = generatedFragment;
    }

    public Set<String> getMatchedRecipes() {
        return matchedRecipes;
    }

    public void setMatchedRecipes(Set<String> matchedRecipes) {
        this.matchedRecipes = matchedRecipes;
    }

    public void crossCheckLists(){
        
    }
}
