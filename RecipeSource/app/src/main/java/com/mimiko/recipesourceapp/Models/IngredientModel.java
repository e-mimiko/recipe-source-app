package com.mimiko.recipesourceapp.Models;

import java.util.Objects;

public class IngredientModel {
     private String ingredientName;
     private String ingredientID;

     public IngredientModel() {
     }

     public IngredientModel(String ingredientName, String ingredientID) {
          this.ingredientName = ingredientName;
          this.ingredientID = ingredientID;
     }

     public String getIngredientName() {
          return ingredientName;
     }

     public void setIngredientName(String ingredientName) {
          this.ingredientName = ingredientName;
     }

     public String getIngredientID() {
          return ingredientID;
     }

     public void setIngredientID(String ingredientID) {
          this.ingredientID = ingredientID;
     }
}
