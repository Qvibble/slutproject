/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.recipe.resources;

import com.google.gson.Gson;
import com.mycompany.recipe.beans.CommentBean;
import com.mycompany.recipe.entities.Comment;
import java.util.List;
import javax.ejb.EJB;
import javax.management.openmbean.SimpleType;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 *
 * @author jespe
 */
@Path("comments")
public class CommentResource {
    @EJB
    CommentBean commentBean;
    
    /**
     * Hämtar alla kommentarer från ett recept med hjälp av receptets id.
     * 
     * @param recipeId
     * @return List of Comment objects
     */
    @GET
    @Path("get")
    public Response getRecipeComments(@HeaderParam("RecipeId")String recipeId) throws InterruptedException{
        //Sl.eep för att kommentaren inte hinner sparas
        Thread.sleep(200);
        /* Lista med alla kommenterer från receptet*/
        List<Comment> comments = commentBean.getComments(Integer.parseInt(recipeId));
        /* Om något gick fel */
        if(comments == null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        /* Om det inte finns kommentarer */
        }else if(comments.isEmpty()){
            return Response.status(Response.Status.NO_CONTENT).build();
        /* Om det finns kommentarer */
        }else{
            return Response.ok(comments).build();
        }
    }
    
    @POST
    @Path("save")
    public Response saveComment(String commentData){
        Gson gson = new Gson();
        Comment comment = gson.fromJson(commentData, Comment.class);                

        /* Om det inte gick att spara */
        if(commentBean.saveComment(comment) == 0){
            return Response.status(Response.Status.BAD_REQUEST).build();
        /* Om det gick att spara */
        }else{
            return Response.status(Response.Status.CREATED).build();            
        }
    }
}
