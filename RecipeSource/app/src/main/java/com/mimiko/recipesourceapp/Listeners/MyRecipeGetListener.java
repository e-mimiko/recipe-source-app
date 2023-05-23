package com.mimiko.recipesourceapp.Listeners;

import com.mimiko.recipesourceapp.Models.RecipeInformation;

import java.util.List;


//used to get recipe to be used by View Past Recipes fragment
public interface MyRecipeGetListener {
    void onComplete(List<RecipeInformation> myRecipes);
}
