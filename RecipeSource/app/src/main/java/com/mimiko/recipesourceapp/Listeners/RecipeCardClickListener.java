package com.mimiko.recipesourceapp.Listeners;

import android.view.View;

//sets the callbacks for the View Recipe and Add Recipe buttons for the recycler views
public interface RecipeCardClickListener {
    void onViewRecipeClick(View v, int position);
    void onAddRemoveRecipeClick(View v, int position);

}
