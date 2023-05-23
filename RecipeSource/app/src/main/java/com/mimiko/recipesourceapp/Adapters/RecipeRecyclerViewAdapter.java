package com.mimiko.recipesourceapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.mimiko.recipesourceapp.Listeners.RecipeCardClickListener;
import com.mimiko.recipesourceapp.Models.RecipeInformation;
import com.mimiko.recipesourceapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeRecyclerViewAdapter extends RecyclerView.Adapter<RecipeRecyclerViewHolder>{

    Context context;
    List<RecipeInformation> recipeList;
    RecipeCardClickListener recipeCardClickListener;
    String leftButton, rightButton;

    //todo: check if this works if you take this out and just call the view model directly from here
    public RecipeRecyclerViewAdapter(Context context, List<RecipeInformation> recipeList,
                                     String rightButton, RecipeCardClickListener recipeCardClickListener) {
        this.context = context;
        this.recipeList = recipeList;
        this.recipeCardClickListener = recipeCardClickListener;
        this.rightButton = rightButton;
    }

    @NonNull
    @Override
    public RecipeRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recipe_card_layout, parent, false);
        return new RecipeRecyclerViewHolder(view, recipeCardClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeRecyclerViewHolder holder, int position) {
        holder.recipe_title.setText(recipeList.get(position).title);
        holder.recipe_title.setSelected(true);
        Picasso.get().load(recipeList.get(position).image).placeholder(R.drawable.recipe_image_placeholder)
               .into(holder.food_image);
        holder.recipe_time.setText(context.getString(R.string.recipe_ready_time, recipeList.get(position).recipeInformationBulk.readyInMinutes));
        holder.used_ingredients.setText(context.getString(R.string.recipe_used_ingredients, recipeList.get(position).getUsedIngredientsToString()));
        holder.missed_ingredients.setText(context.getString(R.string.recipe_missed_ingredients, recipeList.get(position).getMissedIngredientsToString()));
        holder.addRemoveRecipe_button.setText(rightButton);
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

}

class RecipeRecyclerViewHolder extends RecyclerView.ViewHolder {

    MaterialCardView card_container;
    TextView recipe_title, recipe_time, used_ingredients, missed_ingredients;
    MaterialButton view_recipe_button, addRemoveRecipe_button;
    ImageView food_image;
    public RecipeRecyclerViewHolder(@NonNull View itemView, RecipeCardClickListener recipeCardClickListener) {
        super(itemView);
        card_container = itemView.findViewById(R.id.recipe_card_container);
        recipe_title = itemView.findViewById(R.id.recipe_title_textView);
        recipe_time = itemView.findViewById(R.id.recipe_time_textView);
        used_ingredients = itemView.findViewById(R.id.ingredients_used_textView);
        missed_ingredients = itemView.findViewById(R.id.ingredients_missed_textView);
        view_recipe_button = itemView.findViewById(R.id.view_this_recipe_button);
        addRemoveRecipe_button = itemView.findViewById(R.id.addRemove_to_my_recipes_button);
        food_image = itemView.findViewById(R.id.recipe_imageView);

        //set listener
        view_recipe_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION && recipeCardClickListener != null){
                    recipeCardClickListener.onViewRecipeClick(v, position);
                }
            }
        });
        addRemoveRecipe_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION && recipeCardClickListener != null){
                    recipeCardClickListener.onAddRemoveRecipeClick(v, position);
                }
            }
        });
    }

}