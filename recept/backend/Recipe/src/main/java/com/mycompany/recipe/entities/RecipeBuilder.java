/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.recipe.entities;

/**
 *
 * @author Elev
 */
public class RecipeBuilder {
    private String name;

    public RecipeBuilder setName(String name) {
        this.name = name;
        
        return this;
    }
}
