package com.mimiko.recipesourceapp.Models;

import android.os.Parcelable;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//Root of the models.
//Models Package is used to create a complete serialized conversion of the recipes gotten from Spoonacular
public class RecipeInformation implements Serializable {
    @PropertyName("id")
    public int id;
    @PropertyName("title")
    public String title;
    @PropertyName("image")
    public String image;
    @PropertyName("imageType")
    public String imageType;
    @PropertyName("usedIngredientCount")
    public int usedIngredientCount;
    @PropertyName("missedIngredientCount")
    public int missedIngredientCount;
    @PropertyName("missedIngredients")
    public List<MissedIngredient> missedIngredients;
    @PropertyName("usedIngredients")
    public List<UsedIngredient> usedIngredients;
    @PropertyName("unusedIngredients")
    public List<Object> unusedIngredients;
    @PropertyName("likes")
    public int likes;
    @PropertyName("recipeInformationBulk")
    public RecipeInformationBulk recipeInformationBulk;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public int getUsedIngredientCount() {
        return usedIngredientCount;
    }

    public void setUsedIngredientCount(int usedIngredientCount) {
        this.usedIngredientCount = usedIngredientCount;
    }

    public int getMissedIngredientCount() {
        return missedIngredientCount;
    }

    public void setMissedIngredientCount(int missedIngredientCount) {
        this.missedIngredientCount = missedIngredientCount;
    }

    public List<MissedIngredient> getMissedIngredients() {
        return missedIngredients;
    }

    public void setMissedIngredients(List<MissedIngredient> missedIngredients) {
        this.missedIngredients = missedIngredients;
    }

    public List<UsedIngredient> getUsedIngredients() {
        return usedIngredients;
    }

    public void setUsedIngredients(List<UsedIngredient> usedIngredients) {
        this.usedIngredients = usedIngredients;
    }

    public List<Object> getUnusedIngredients() {
        return unusedIngredients;
    }

    public void setUnusedIngredients(List<Object> unusedIngredients) {
        this.unusedIngredients = unusedIngredients;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public RecipeInformationBulk getRecipeInformationBulk() {
        return recipeInformationBulk;
    }

    public void setRecipeInformationBulk(RecipeInformationBulk recipeInformationBulk) {
        this.recipeInformationBulk = recipeInformationBulk;
    }

    @Exclude
    public String getMissedIngredientsToString(){
        StringBuilder sb = new StringBuilder();
        String res;
        for (MissedIngredient missedIngredient : missedIngredients){
            sb.append(missedIngredient.getName()).append(", ");
        }
        res = sb.toString();
        if (res.length() > 2){
            res = res.substring(0,res.length()-2);
        }
        return res;
    }

    @Exclude
    public String getUsedIngredientsToString(){
        StringBuilder sb = new StringBuilder();
        String res;
        for (UsedIngredient usedIngredient : usedIngredients){
            sb.append(usedIngredient.getName()).append(", ");
        }
        res = sb.toString();
        if (res.length() > 2){
            res = res.substring(0,res.length()-2);
        }
        return res;
    }

    @Exclude
    public String getAllInstructions(){
        StringBuilder sb = new StringBuilder();
        String allSteps = "";
        List<AnalyzedInstruction> analyzedInstructionList = getRecipeInformationBulk().getAnalyzedInstructions();
        if (!analyzedInstructionList.isEmpty()){
            List<Step> stepsList = analyzedInstructionList.get(0).getSteps();
            if (!stepsList.isEmpty()){
                for (Step step : stepsList){
                    sb.append(step.getNumber()).append(". ").append(step.getStep()).append("\n\n");
                }
                allSteps = sb.toString();
            }
        }
        return allSteps;
    }

    //end
}



