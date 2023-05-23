package com.mimiko.recipesourceapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.paging.PagingConfig;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.firebase.firestore.Query;
import com.mimiko.recipesourceapp.Models.IngredientModel;
import com.mimiko.recipesourceapp.R;
import com.mimiko.recipesourceapp.ViewModels.IngredientClickViewModel;

import java.util.HashSet;

import io.reactivex.rxjava3.disposables.Disposable;

public class GenerateRecipeFirestoreAdapter {

    Context context;
    private LifecycleOwner lifecycleOwner;
    private RecyclerView recyclerView;
    private FirestorePagingAdapter<IngredientModel, IngredientsViewHolder> adapter;
    private HashSet<String> clickedIds;
    private IngredientClickViewModel ingredientClickViewModel;
    private Disposable disposable;

    public GenerateRecipeFirestoreAdapter() {
    }

    public GenerateRecipeFirestoreAdapter(Context context, LifecycleOwner lifecycleOwner, RecyclerView recyclerView,
                                           IngredientClickViewModel ingredientClickViewModel) {
        this.context = context;
        this.lifecycleOwner = lifecycleOwner;
        this.recyclerView = recyclerView;
        this.ingredientClickViewModel = ingredientClickViewModel;
    }

    public void load(Query query, PagingConfig pagingConfig, IngredientModel ingredientModel) {
        FirestorePagingOptions<IngredientModel> options = new FirestorePagingOptions.Builder<IngredientModel>()
                .setLifecycleOwner(lifecycleOwner)
                .setQuery(query, pagingConfig, IngredientModel.class).build();//todo:ensure to pass in getlifecycleowner() in fragment not context/getactivity


        adapter = new FirestorePagingAdapter<IngredientModel, IngredientsViewHolder>(options) {

            @NonNull
            @Override
            public IngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.ingredient_card_layout, parent, false);

                return new IngredientsViewHolder(view, ingredientClickViewModel);
            }

            @Override
            protected void onBindViewHolder(@NonNull IngredientsViewHolder holder, int position, @NonNull IngredientModel model) {
                holder.bind(model);
            }

        };
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(context, FlexDirection.ROW);
        recyclerView.setLayoutManager(flexboxLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void startListening(){
        if (adapter != null){
            adapter.startListening();
        }
    }

    public void stopListening() {
        if (adapter != null) {
            adapter.stopListening();
        }
    }
}
