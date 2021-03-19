/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.recipe.entities;

/**
 *
 * @author jespe
 */
public class Comment {
    private String content;
    private String date;
    private String username;
    private int recipeId;

    public Comment(String content, String date, String username, int recipeId){
        this.content = content;
        this.date = date;
        this.username = username;
        this.recipeId = recipeId;
    }
    
    public Comment(String content, String date, String username){
        this.content = content;
        this.date = date;
        this.username = username;
    }
    
    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    
    
}
