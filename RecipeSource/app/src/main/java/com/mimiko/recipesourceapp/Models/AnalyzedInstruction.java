package com.mimiko.recipesourceapp.Models;

import com.google.firebase.firestore.PropertyName;

import java.util.ArrayList;

public class AnalyzedInstruction {
    public String name;
    public ArrayList<Step> steps;
    public String getName() {
        return name;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
    }
}
