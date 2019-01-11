package com.notaprogrammer.baking.model;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import static com.notaprogrammer.baking.model.Recipe.Ingredient.LABEL;
import static com.notaprogrammer.baking.model.Recipe.Ingredient.NEW_LINE;
import static com.notaprogrammer.baking.model.Recipe.Ingredient.SUFFIX;

public class Recipe {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("ingredients")
    private List<Ingredient> ingredients;

    @SerializedName("steps")
    private List<Step> steps;

    @SerializedName("servings")
    private int servings;

    @SerializedName("image")
    private String image;


    public Recipe() { /* Empty constructor for Gson */ }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public static Recipe parseJsonObject(String response) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(response, Recipe.class);
    }

    public static List<Recipe> parseJsonList(String response){
        return new Gson().fromJson(response, new TypeToken<List<Recipe>>(){}.getType());
    }

    public String toJsonString(){
        return new Gson().toJson(this);
    }

    public String getIngredientCardDetail() {

        if(ingredients == null || ingredients.size() == 0){
            return "";
        }

        StringBuilder ingredientsStringBuilder = new StringBuilder();

        ingredientsStringBuilder.append("<strong>").append(name).append(LABEL).append("</strong>");

        for (Ingredient ingredient: ingredients) {
            ingredientsStringBuilder.append(NEW_LINE).append(SUFFIX).append( ingredient.getReadableString());
        }

        return ingredientsStringBuilder.toString();
    }

    public static class Ingredient {

        static final String NEW_LINE = "<br />";
        static final String LABEL = " Ingredient Card ";
        static final String SUFFIX = "•";

        private static final String SPACING = " ";
        private static final String OPEN = "(";
        private static final String CLOSE = ")";

        @SerializedName("quantity")
        private double quantity;

        @SerializedName("measure")
        private String measure;

        @SerializedName("ingredient")
        private String ingredient;

        public Ingredient() { /* Empty constructor for Gson */ }

        public double getQuantity() {
            return quantity;
        }

        public void setQuantity(double quantity) {
            this.quantity = quantity;
        }

        public String getMeasure() {
            return measure;
        }

        public void setMeasure(String measure) {
            this.measure = measure;
        }

        public String getIngredient() {
            return ingredient;
        }

        public void setIngredient(String ingredient) {
            this.ingredient = ingredient;
        }

        public String toJsonString(){
            return new Gson().toJson(this);
        }

        private String getReadableQuantity(){
            if((quantity-(int)quantity)!=0){
                return String.valueOf(quantity);
            }else{
                return String.valueOf((int)quantity);
            }
        }

        private String getReadableMeasure(){
            if(TextUtils.isEmpty(measure)){
                return "";
            }else{
                return measure.toLowerCase();
            }
        }

        public String getReadableString(){


            return ingredient + SPACING + OPEN + getReadableQuantity() + getReadableMeasure() + CLOSE;
        }
    }

    public static class Step {

        @SerializedName("id")
        private int id;

        @SerializedName("shortDescription")
        private String shortDescription;

        @SerializedName("description")
        private  String description;

        @SerializedName("videoURL")
        private String videoUrl;

        @SerializedName("thumbnailURL")
        private String thumbnailUrl;

        public Step() { /* Empty constructor for Gson */  }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getShortDescription() {
            return shortDescription;
        }

        public void setShortDescription(String shortDescription) {
            this.shortDescription = shortDescription;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public String getThumbnailUrl() {
            return thumbnailUrl;
        }

        public void setThumbnailUrl(String thumbnailUrl) {
            this.thumbnailUrl = thumbnailUrl;
        }

        public static Step parseJSON(String response) {
            Gson gson = new GsonBuilder().create();
            return gson.fromJson(response, Step.class);
        }

        public String toJsonString(){
            return new Gson().toJson(this);
        }

    }
}
