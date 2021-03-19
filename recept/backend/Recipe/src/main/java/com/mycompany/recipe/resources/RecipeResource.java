/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.recipe.resources;

import com.google.gson.Gson;
import com.mycompany.recipe.beans.RecipeBean;
import com.mycompany.recipe.entities.Recipe;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 *
 * @author jespe
 */
@Path("recipe")
public class RecipeResource {
    @EJB
    RecipeBean recipeBean;
    
    @GET
    @Path("new")
    public Response getNewRecipes(){
         List<Recipe> r = recipeBean.getNewRecipes();
        
        /* Om det gick att hämta recept */
        if(r != null){
            return Response.ok(r).build();     
        /* Om det inte gick att hämta recept */
        }else if(r.isEmpty()){
            return Response.status(Response.Status.NO_CONTENT).build();
        }else{
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
    
    @GET
    @Path("random")
    public Response getRandomRecipes(){
         List<Recipe> r = recipeBean.getRandomRecipes();
                 
        /* Om det gick att hämta recept */
        if(r != null){
            return Response.ok(r).build(); 
        /* Om det inte gick att hämta recept */
        }else if(r.isEmpty()){
            return Response.status(Response.Status.NO_CONTENT).build();
        }else{
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
    
    @GET
    @Path("liked")
    public Response getLikedRecipes(@HeaderParam("Username")String username){
         List<Recipe> r = recipeBean.getLikedRecipes(username);
        
        /* Om det gick att hämta recept */
        if(r != null){
            return Response.ok(r).build(); 
        /* Om det inte gick att hämta recept */
        }else if(r.isEmpty()){
            return Response.status(Response.Status.NO_CONTENT).build();
        }else{
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
    
    @GET
    @Path("user")
    public Response getUserRecipes(@HeaderParam("Username")String username){
         List<Recipe> r = recipeBean.getUserRecipes(username);
        
        /* Om det gick att hämta recept */
        if(r != null){
            return Response.ok(r).build(); 
        /* Om det inte gick att hämta recept */
        }else if(r.isEmpty()){
            return Response.status(Response.Status.NO_CONTENT).build();
        }else{
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
    
    @GET
    @Path("search")
    public Response getSearchedRecipes(@HeaderParam("SearchTerm")String searchTerm){
        List<Recipe> r = recipeBean.getSearchedRecipes(searchTerm);
            
        /* Om det gick att hämta recept */
        if(r != null){
            return Response.ok(r).build(); 
        /* Om det inte gick att hämta recept */
        }else if(r.isEmpty()){
            return Response.status(Response.Status.NO_CONTENT).build();
        }else{
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
    
    @GET
    @Path("get")
    public Response getRecipe(@HeaderParam("IdUsername")String idUsername)throws InterruptedException{
        /* Om det gick att hämta recept */
        Thread.sleep(100);
        List<Recipe> r = recipeBean.getRecipe(idUsername);
        
        if(r != null){
            return Response.ok(r).build(); 
        /* Om det inte gick att hämta recept */
        }else if(r.isEmpty()){
            return Response.status(Response.Status.NO_CONTENT).build();
        }else{
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
    
    @GET
    @Path("category")
    public Response getCategoryRecipe(@HeaderParam("Category")String category){
        /* Om det gick att hämta recept */
        List<Recipe> r = recipeBean.getCategoryRecipes(category);
        
        if(r != null){
            return Response.ok(r).build(); 
        /* Om det inte gick att hämta recept */
        }else if(r.isEmpty()){
            return Response.status(Response.Status.NO_CONTENT).build();
        }else{
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
    
    /**
     * Sparar receptet i databasen och vem som skapat det
     * 
     * Tar emot användarnamn,{receptJSON}
     * 
     * @param recipeData
     * @return
     */
    @POST
    @Path("create")
    public Response createRecipe(String recipeData){     
        Gson gson = new Gson();
        Recipe recipe = gson.fromJson(recipeData, Recipe.class);

        /* Om det inte gick att spara */
        if(recipeBean.saveRecipe(recipe) == 0){
            return Response.status(Response.Status.BAD_REQUEST).build();
        /* Om det gick att spara */
        }else{
            return Response.status(Response.Status.CREATED).build();
        }        
    }
    
    /**
     * 
     * 
     * @param recipeData
     * @return
     */
    @PUT
    @Path("edit")
    public Response editRecipe(String recipeData){
        Gson gson = new Gson();
        Recipe recipe = gson.fromJson(recipeData, Recipe.class);
        
        /* Om det inte gick att ändra */
        if(recipeBean.editRecipe(recipe) == 0){
            return Response.status(Response.Status.BAD_REQUEST).build();
        /* Om det gick att spara */
        }else{
            return Response.ok().build();
        }  
    }
    
    @DELETE
    @Path("delete")
    public Response removeRecipe(@HeaderParam("RecipeId")String recipeId){  
        /* Om det inte gick att ändra */
        if(recipeBean.removeRecipe(recipeId) == 0){
            return Response.status(Response.Status.BAD_REQUEST).build();
        /* Om det gick att spara */
        }else{
            return Response.ok().build();
        }  
    }

    @POST
    @Path("like")
    public Response likeRecipe(String idUsername){
        //recipeIdUsernerm set ut ex. 5|Användare
        //Delas med "|"
        
        /*Om det inte gick att lägga till / ta bort en like*/
        if(recipeBean.addLike(idUsername) == 0){
            return Response.status(Response.Status.BAD_REQUEST).build();
        /* Om det gick att ändra */
        }else{
            return Response.ok().build();
        }
    }
    @DELETE
    @Path("like")
    public Response removeLikeRecipe(@HeaderParam("IdUsername")String idUsername){
        //recipeIdUsernerm set ut ex. 5|Användare
        //Delas med "|"

        /*Om det inte gick att lägga till / ta bort en like*/
        if(recipeBean.removeLike(idUsername) == 0){
            return Response.status(Response.Status.BAD_REQUEST).build();
        /* Om det gick att ändra */
        }else{
            return Response.ok().build();
        }
    }
}