package com.mimiko.recipesourceapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagingConfig;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.mimiko.recipesourceapp.Adapters.GenerateRecipeFirestoreAdapter;
import com.mimiko.recipesourceapp.ViewModels.FragmentCommsViewModel;
import com.mimiko.recipesourceapp.ViewModels.IngredientClickViewModel;
import com.mimiko.recipesourceapp.Listeners.RecipeApiResponseListener;
import com.mimiko.recipesourceapp.Models.IngredientModel;
import com.mimiko.recipesourceapp.Models.RecipeInformation;
import com.mimiko.recipesourceapp.R;
import com.mimiko.recipesourceapp.RequestManager;
import com.mimiko.recipesourceapp.Tools;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class GenerateRecipeFragment extends Fragment {

    final String TAG = "GenerateRecipeFragment";

    MaterialButton generate_recipe_button;
    TextInputEditText filter_button;
    ProgressBar progressBar;

    //For API on recycler view
    RequestManager manager;
    FirebaseFirestore database;
    RecyclerView recyclerViewIngredients;
    GenerateRecipeFirestoreAdapter firebaseAdapter;
    IngredientModel ingredientTemplate;
    IngredientClickViewModel ingredientClickViewModel;
    FragmentCommsViewModel fragmentCommsViewModel;
    PagingConfig pagingConfig;

    Query baseQuery;
    static private HashSet<String> ingredientModelIds;

    public GenerateRecipeFragment()  {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAdapter.startListening();//firebase adapter start
    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseAdapter.stopListening();//firebase adapter stop
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_generate_recipe, container, false);
        //set view to layout
        filter_button = rootView.findViewById(R.id.ingredients_searchView);
        generate_recipe_button = rootView.findViewById(R.id.generateRecipe_btn);
        progressBar = rootView.findViewById(R.id.progressBar);
        recyclerViewIngredients = rootView.findViewById(R.id.ingredients_to_search_recyclerView);

        //set other class instances
        ingredientClickViewModel = new ViewModelProvider(this).get(IngredientClickViewModel.class);//view model for ingredient clicks in recyclerview
        fragmentCommsViewModel = new ViewModelProvider(requireActivity()).get(FragmentCommsViewModel.class);//view model for fragment communication
        ingredientModelIds = new HashSet<>();//POJO for ingredient database on firebase
        pagingConfig = new PagingConfig(50, 10, false);//used by adapter for paging

        //get database instance
        database = FirebaseFirestore.getInstance();

        //recycler adapter
        baseQuery = Tools.getCollectionReferenceForIngredientsDatabase().orderBy("ingredientName");
        firebaseAdapter = new GenerateRecipeFirestoreAdapter(getActivity(),getViewLifecycleOwner(),
                recyclerViewIngredients, ingredientClickViewModel);
        firebaseAdapter.load(baseQuery,pagingConfig,ingredientTemplate);

        //set onClick buttons
        generate_recipe_button.setOnClickListener(generateRecipeButtonClick);
        filter_button.addTextChangedListener(searchListener);
        return rootView;
    }

    //API related Functions

    //function to create a new request manager and pass in the callback interface
    private final View.OnClickListener generateRecipeButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            progressBar.setVisibility(View.VISIBLE);
            generate_recipe_button.setVisibility(View.GONE);
            String recipes = ingredientClickViewModel.ingredientsToString();
            if (recipes != null) {
                if (!recipes.isEmpty()) {
                    manager = new RequestManager(getContext(), recipes);
                    manager.getRecipesByIngredients(recipeResponseListener);
                }else{
                    progressBar.setVisibility(View.GONE);
                    generate_recipe_button.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "Select An Ingredient", Toast.LENGTH_SHORT).show();
                }
            }else{
                progressBar.setVisibility(View.GONE);
                generate_recipe_button.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), "Select An Ingredient", Toast.LENGTH_SHORT).show();
            }
        }
    };
    //callback function to get back the list of recipes created and pass them to the
    //generated fragment to handle the view.
    private final RecipeApiResponseListener recipeResponseListener = new RecipeApiResponseListener() {
        @Override
        public void fetchSuccess(List<RecipeInformation> recipes, String message) {
            //store recipe in view model
            fragmentCommsViewModel.setRecipeList(recipes);
            //on success, call second fragment and pass list of recipes
            if (isAdded()){
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragments_container, new GeneratedRecipesFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        }

        @Override
        public void fetchError(String message) {
            Log.d(TAG, message);
            if (isAdded()){
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
            //shows the button again if error so user can click again
            progressBar.setVisibility(View.GONE);
            generate_recipe_button.setVisibility(View.VISIBLE);
        }
    };

    //for real time searching of the ingredients database in firebase
    //responds to each letter entered and returns the list of ingredients matching the string.
    private final TextWatcher searchListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String endCode = "\uF7FF"; // value here is the last Unicode character that exists
            String filter_words = String.valueOf(s);
            if (filter_words.isEmpty()){
                baseQuery = Tools.getCollectionReferenceForIngredientsDatabase().orderBy("ingredientName");
            }else{
                //filter word and create new query
                baseQuery = Tools.getCollectionReferenceForIngredientsDatabase()
                        .whereGreaterThanOrEqualTo("ingredientName", filter_words)
                        .whereLessThanOrEqualTo("ingredientName",filter_words+endCode );
            }
            firebaseAdapter = new GenerateRecipeFirestoreAdapter(getActivity(),getViewLifecycleOwner(),
                    recyclerViewIngredients, ingredientClickViewModel);
            firebaseAdapter.load(baseQuery,pagingConfig,ingredientTemplate);
        }
    };


    //end
}