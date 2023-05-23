package com.mimiko.recipesourceapp.Adapters;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mimiko.recipesourceapp.Models.IngredientModel;
import com.mimiko.recipesourceapp.R;
import com.mimiko.recipesourceapp.ViewModels.IngredientClickViewModel;

public class IngredientsViewHolder extends RecyclerView.ViewHolder {

    private CardView cardView;
    private TextView textView;
    IngredientClickViewModel ingredientClickViewModel;


    public IngredientsViewHolder(@NonNull View itemView, IngredientClickViewModel ingredientClickViewModel) {
        super(itemView);
        this.cardView = itemView.findViewById(R.id.ingredient_card_template);
        this.textView = itemView.findViewById(R.id.ingredient_textView);
        this.ingredientClickViewModel = ingredientClickViewModel;
    }

    public void bind(IngredientModel ingredientModel) {
        textView.setText(ingredientModel.getIngredientName());
        itemView.setTag(ingredientModel.getIngredientID());
        //set click state
        boolean clicked = ingredientClickViewModel.isIngredientClicked(ingredientModel);
        cardView.setCardBackgroundColor(clicked ? Color.GRAY : Color.WHITE);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean clicked_U = ingredientClickViewModel.updateIngredientClickState(ingredientModel);
                cardView.setCardBackgroundColor(clicked_U ? Color.WHITE : Color.GRAY);
            }
        });
    }
}