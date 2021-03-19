/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.recipe.entities;

import java.util.List;

/**
 *
 * @author jespe
 */
public class Recipe {
    private String username;
    private String name;
    private String description;
    private String steps;
    private List<Ingredient> ingredients;
    private String image;
    private int likes;
    private int id;
    private List<Category> categories;
    private boolean liked;
    
    public Recipe(int id, String name, String image){
        this.id = id;
        this.name = name;
        this.image = image;
    }
    
    public Recipe(int id, String username, String name, String description, String steps, List<Ingredient> ingredients, String image, int likes, List<Category> categories){
        this.id = id;
        this.username = username;
        this.name = name;
        this.description = description;
        this.steps = steps;
        this.ingredients = ingredients;
        this.image = image;
        this.likes = likes;
        this.categories = categories;
    }
    
    public Recipe(int id, String username, String name, String description, String steps, List<Ingredient> ingredients, String image, int likes, List<Category> categories, boolean liked){
        this.id = id;
        this.username = username;
        this.name = name;
        this.description = description;
        this.steps = steps;
        this.ingredients = ingredients;
        this.image = image;
        this.likes = likes;
        this.categories = categories;
        this.liked = liked;
    }
    
    public Recipe(String username, String name, String description, String steps, List<Ingredient> ingredients, String image, int likes, List<Category> categories){
        this.username = username;
        this.name = name;
        this.description = description;
        this.steps = steps;
        this.ingredients = ingredients;
        this.image = image;
        this.likes = likes;
        this.categories = categories;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
    
    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public String getImage() {
        return image;
    }
    
    public void setImage(String image) {
        this.image = image;
    }
}
