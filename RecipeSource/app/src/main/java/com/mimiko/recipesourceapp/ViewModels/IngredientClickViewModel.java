package com.mimiko.recipesourceapp.ViewModels;

import androidx.lifecycle.ViewModel;

import com.mimiko.recipesourceapp.Models.IngredientModel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class IngredientClickViewModel extends ViewModel {

    Map<String, String > clickedIngredient;

    public IngredientClickViewModel() {
        this.clickedIngredient = new HashMap<>();
    }

    public boolean updateIngredientClickState(IngredientModel ingredientModel){
        if (clickedIngredient.containsKey(ingredientModel.getIngredientID())){
            clickedIngredient.remove(ingredientModel.getIngredientID());
            return true;
        }
        clickedIngredient.put(ingredientModel.getIngredientID(), ingredientModel.getIngredientName());
        return false;
    }
    public boolean isIngredientClicked(IngredientModel ingredientModel){
         return clickedIngredient.containsKey(ingredientModel.getIngredientID());
    }

    public String ingredientsToString(){
        StringBuilder sb = new StringBuilder();
        String res = null;
        if (!clickedIngredient.isEmpty()){
            for (String value : clickedIngredient.values()){
                sb.append(value).append(",");
            }
            res = sb.toString();
            if (res.length() > 0){
                res = res.substring(0, res.length()-1);
            }
        }
        return res;
    }
}
