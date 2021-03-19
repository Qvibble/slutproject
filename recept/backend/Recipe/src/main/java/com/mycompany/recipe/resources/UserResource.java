/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.recipe.resources;

import com.google.gson.Gson;
import com.mycompany.recipe.beans.UserBean;
import com.mycompany.recipe.entities.User;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 *
 * @author jespe
 */
  @Path("user")
public class UserResource {
      @EJB
      UserBean userBean;
      
    /**
     * Kollar om användaren finns och om lösenorder matchar det i databasen.
     * 
     * @param authorization
     * @return
     */
    @GET
    public Response verifyUser(@HeaderParam("Authorization")String authorization){        
        User user = userBean.createUser(authorization);
                
        /* Om avnändaren finns och lösenord stämmer */
        if(userBean.checkUser(user)){
            return Response.status(Response.Status.OK).build();
        /* Om fel lösenord/användarnamn */
        }else{
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }    
    } 
    
    /**
     * Om användaren inte redan finns i databasen så läggs den till
     * 
     * @param userData
     * @return
     */
    @POST
    public Response saveUser(String userData){
        Gson gson = new Gson();
        User user = gson.fromJson(userData, User.class);
        
        /* Om det inte gick att spara */
        if(userBean.saveUser(user) == 0){
            return Response.status(Response.Status.BAD_REQUEST).build();
        /* Om det gick att spara */
        }else{
            return Response.status(Response.Status.CREATED).build();
        }        
    }
}
