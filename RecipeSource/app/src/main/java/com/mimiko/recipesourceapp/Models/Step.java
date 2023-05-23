package com.mimiko.recipesourceapp.Models;

import java.util.ArrayList;

public class Step {
    public int number;
    public String step;
    public ArrayList<Ingredient> ingredients;
    public ArrayList<Equipment> equipment;
    public Length length;

    public int getNumber() {
        return number;
    }

    public String getStep() {
        return step;
    }
}
