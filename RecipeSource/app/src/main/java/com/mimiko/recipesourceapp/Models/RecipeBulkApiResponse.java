package com.mimiko.recipesourceapp.Models;

import java.util.ArrayList;

public class RecipeBulkApiResponse {
    public ArrayList<RecipeInformationBulk> recipesDetails;

    public ArrayList<RecipeInformationBulk> getRecipesDetails() {
        return recipesDetails;
    }

    public void setRecipesDetails(ArrayList<RecipeInformationBulk> recipesDetails) {
        this.recipesDetails = recipesDetails;
    }
}
