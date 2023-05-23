package com.mimiko.recipesourceapp.Models;

import java.util.ArrayList;

public class MissedIngredient {
    public int id;
    public double amount;
    public String unit;
    public String unitLong;
    public String unitShort;
    public String aisle;
    public String name;
    public String original;
    public String originalName;
    public ArrayList<String> meta;
    public String extendedName;
    public String image;

    public String getName() {
        return name;
    }
}
