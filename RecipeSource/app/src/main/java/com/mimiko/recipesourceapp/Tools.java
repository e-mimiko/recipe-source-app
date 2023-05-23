package com.mimiko.recipesourceapp;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mimiko.recipesourceapp.Listeners.BooleanListener;
import com.mimiko.recipesourceapp.Listeners.MyRecipeGetListener;
import com.mimiko.recipesourceapp.Listeners.MyRecipeListener;
import com.mimiko.recipesourceapp.Listeners.StringListener;
import com.mimiko.recipesourceapp.Models.RecipeInformation;
import com.mimiko.recipesourceapp.ViewModels.FragmentCommsViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Tools {
    static final public String TAG = "Tools---------------->";
    static final public String invalidEmailMessage = "Email is Invalid";
    static final public String invalidPasswordMessage = "Password must be longer than 6 letter";
    static final public String passwordMismatchMessage = "Password does not match";
    static final public String invalidNameMessage = "Provide a valid Name";



    //Function returns the collection for user's saved Recipes
    public static CollectionReference getCollectionOfSavedRecipes(){

        //TODO:assert current user is not null
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("Users")
                .document(currentUser.getUid()).collection("MyRecipes");
    }

    //Function returns the collection for Ingredients Database
    public static CollectionReference getCollectionReferenceForIngredientsDatabase(){
        //TODO:assert current user is not null
        return FirebaseFirestore.getInstance().collection("IngredientsDatabase");
    }

    //Function checks if Terms of Use were agreed.
    public static void hasAgreedToTerms(BooleanListener listener){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore.getInstance().collection("Users")
                .document(currentUser.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String ob = (String) documentSnapshot.get("Agreed To Terms and Conditions");
                        if (Objects.equals(ob, "Yes")){
                            listener.onComplete(true);
                        }else{
                            listener.onComplete(false);
                        }
                    }
                });
    }
    //Function sets Terms of Use were agreement.
    public static void setAgreedToTerms(StringListener listener){
        Map<String, String> agree = new HashMap<>();
        agree.put("Agreed To Terms and Conditions", "Yes");
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore.getInstance().collection("Users")
                .document(currentUser.getUid()).set(agree).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        listener.onComplete("Thank you for agreeing to our Terms and Conditions");
                    }
                });
    }
    //Function gets Terms of Use as a string to be displayed.
    public static void getTermsAndConditions(StringListener listener){
        FirebaseFirestore.getInstance().collection("About").document("TermsConditions")
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map<String,Object> map = documentSnapshot.getData();
                        if (map != null){
                            listener.onComplete((String) map.get("Terms of Use"));
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onComplete("Error");
                    }
                });
    }


    //Function saves a recipe to Firebase
    public static void saveMyRecipeToFirebase(RecipeInformation recipeInformation, MyRecipeListener listener){
        CollectionReference collectionReference = getCollectionOfSavedRecipes();
        collectionReference.document(String.valueOf(recipeInformation.getId())).set(recipeInformation).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                listener.onComplete(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onComplete(false);
            }
        });
    }
    //Function gets a recipe to Firebase
    public static void getMyRecipesToFirebase(MyRecipeGetListener myRecipeGetListener){
        List<RecipeInformation> recipeInformationList = new ArrayList<>();
        CollectionReference collectionReference = getCollectionOfSavedRecipes();
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots){
                    recipeInformationList.add(snapshot.toObject(RecipeInformation.class));
                }
                myRecipeGetListener.onComplete(recipeInformationList);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, e.getMessage());
                myRecipeGetListener.onComplete(null);
            }
        });
    }

    //Function removes a recipe to Firebase
    public static void removeRecipeFromFirebase(int recipeId, MyRecipeListener listener){
        CollectionReference collectionReference = Tools.getCollectionOfSavedRecipes();
        collectionReference.document(String.valueOf(recipeId)).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                listener.onComplete(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onComplete(false);
            }
        });
    }


    //end
}