package com.mimiko.recipesourceapp.Models;

import java.util.ArrayList;
import java.util.List;

public class RecipeApiResponse {
    public List<RecipeInformation> recipesInformation;

    public RecipeApiResponse(List<RecipeInformation> recipesInformation) {
        this.recipesInformation = recipesInformation;
    }

    public List<RecipeInformation> getRecipesInformation() {
        return recipesInformation;
    }

    public void setRecipesInformation(List<RecipeInformation> recipesInformation) {
        this.recipesInformation = recipesInformation;
    }

    public String getIDsFromRecipeInformation(){
        int i;
        StringBuilder ids = new StringBuilder("");
        for (i = 0; i < recipesInformation.size()-1;i++){
            ids.append(recipesInformation.get(i).id);
            ids.append(",");
        }
        ids.append(recipesInformation.get(i).id);
        return String.valueOf(ids);
    }
}
