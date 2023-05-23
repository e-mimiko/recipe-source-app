package com.mimiko.recipesourceapp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.mimiko.recipesourceapp.Listeners.RecipeApiResponseListener;
import com.mimiko.recipesourceapp.Models.RecipeApiResponse;
import com.mimiko.recipesourceapp.Models.RecipeBulkApiResponse;
import com.mimiko.recipesourceapp.Models.RecipeInformation;
import com.mimiko.recipesourceapp.Models.RecipeInformationBulk;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;


//Source: https://www.youtube.com/watch?v=6-891CSz6v0
//Calls the Spoonacular API for ingredients
public class RequestManager {
    final String TAG = "<-------------RequestManager -------->";
    Context context;
    String requestedIngredients;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.spoonacular.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    //constructor
    public RequestManager(Context context, String requestedIngredients) {
        this.context = context;
        this.requestedIngredients = requestedIngredients;
    }

    //calls the spoonacular api for recipe by ingredients
    public void getRecipesByIngredients(RecipeApiResponseListener listener){
        if (requestedIngredients == null || requestedIngredients.isEmpty()){
            return;
        }
        CallsToAPI callRecipesByIngredients = retrofit.create(CallsToAPI.class);
        //TODO:Remove magic number
        Call<List<RecipeInformation>> call =  callRecipesByIngredients.callRecipesByIngredient(requestedIngredients, "20", context.getString(R.string.api_key) );
        call.enqueue(new Callback<List<RecipeInformation>>() {
            @Override
            public void onResponse(Call<List<RecipeInformation>> call, Response<List<RecipeInformation>> response) {
                if (!response.isSuccessful()){
                    listener.fetchError(response.message());
                    Log.d(TAG, response.message());
                    return;
                }
                if (response.body() != null){
                    if (response.body().isEmpty()){
                        listener.fetchError("No Recipes Found");
                        return;
                    }
                    //if successful, get more information about the recipes
                    getRecipeInformationBulk(response, listener);
                }
            }
            @Override
            public void onFailure(Call<List<RecipeInformation>> call, Throwable t) {
                listener.fetchError(t.getMessage());
            }
        });
    }


    //Function to get the information Bulk from the Spoonacular API
    public void getRecipeInformationBulk(Response<List<RecipeInformation>> recipeApiResponse, RecipeApiResponseListener listener){
        RecipeApiResponse apiResponse = new RecipeApiResponse(recipeApiResponse.body());
        CallsToAPI callRecipeInfoBulk = retrofit.create(CallsToAPI.class);
        Call<List<RecipeInformationBulk>> call = callRecipeInfoBulk
                .callRecipeInformationBulk(apiResponse.getIDsFromRecipeInformation(), context.getString(R.string.api_key) );
        call.enqueue(new Callback<List<RecipeInformationBulk>>() {
            @Override
            public void onResponse(Call<List<RecipeInformationBulk>> call, Response<List<RecipeInformationBulk>> response) {
                if (!response.isSuccessful()){
//                    listener.fetchError(response.message());//this is the response from the activity, match only recipeapi
                    return;
                }
                Log.d(TAG, String.valueOf(response.body().size()));
                attachRecipeInformationBulk(response, recipeApiResponse,  listener);
//              listener.fetchSuccess(recipeInformation, response.message());
            }

            @Override
            public void onFailure(Call<List<RecipeInformationBulk>> call, Throwable t) {
                listener.fetchError(t.getMessage());
            }
        });
    }

    //Queries to Spoonacular
    private interface CallsToAPI{
        @GET("recipes/findByIngredients")
        Call<List<RecipeInformation>> callRecipesByIngredient(
                @Query("ingredients") String ingredients,
                @Query("number") String number,
                @Query("apiKey") String apiKey
        );

        @GET("recipes/informationBulk")
        Call<List<RecipeInformationBulk>> callRecipeInformationBulk(
                @Query("ids") String ids,
                @Query("apiKey") String apiKey
        );
    }


    //Converter to add each recipe information to the recipeInformation class
    private void attachRecipeInformationBulk(Response<List<RecipeInformationBulk>> bulkResponse, Response<List<RecipeInformation>> recipeResponse, RecipeApiResponseListener listener){
        //attach each function recipe info bulk to its individual recipe
        for (int i = 0; i < recipeResponse.body().size(); i++){
            if (recipeResponse.body().get(i).id == bulkResponse.body().get(i).id){
                recipeResponse.body().get(i).recipeInformationBulk = bulkResponse.body().get(i);
                //todo:safeguard against non match
            }
        }
        listener.fetchSuccess(recipeResponse.body(), recipeResponse.message());//return the recipes
    }



}
