package com.mimiko.recipesourceapp.Listeners;

import com.mimiko.recipesourceapp.Models.RecipeApiResponse;
import com.mimiko.recipesourceapp.Models.RecipeInformation;

import java.util.List;

//Used to inform the appropriate function of the spoonacular API call result
//used by RequestManager
public interface RecipeApiResponseListener {
    void fetchSuccess(List<RecipeInformation> recipeApiResponse, String message);
    void fetchError(String message);
}
