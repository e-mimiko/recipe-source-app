package com.mimiko.recipesourceapp.Listeners;

//listens and returns a boolean
//used for terms of use agreement callback
public interface BooleanListener {
    void onComplete(boolean hasAgreed);
}
