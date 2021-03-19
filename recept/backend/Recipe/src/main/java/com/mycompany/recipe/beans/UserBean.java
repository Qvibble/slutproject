/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.recipe.beans;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.mycompany.recipe.ConnectionFactory;
import com.mycompany.recipe.entities.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Base64;
import javax.ejb.Stateless;

/**
 *
 * @author jespe
 */
@Stateless
public class UserBean {

    /**
     * Skapa en anvnändare från en Basic Authorization sträng
     * 
     * @param auth
     * @return
     */
    public User createUser(String auth){      
        auth = auth.substring(6).trim();
        byte[] bytes = Base64.getDecoder().decode(auth);
        auth = new String(bytes);
        int colon = auth.indexOf(":");
        String username = auth.substring(0, colon);
        String password = auth.substring(colon+1);
        
        return new User(username, password);
    }
    
    /**
     * Kollar om användaren finns i databasen med korrekt lösenord
     * 
     * @param user
     * @return
     */
    public boolean checkUser(User user){
        try(Connection con = ConnectionFactory.getConnection()){
            /* Hämtar kommentarens innehåll, datum */
            String sql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement prepStmt = con.prepareStatement(sql);
            prepStmt.setString(1, user.getUsername());
            ResultSet userData = prepStmt.executeQuery();
            
            if(userData.next()){
                String bcryptHashString = userData.getString("hashed_password");
                BCrypt.Result result = BCrypt.verifyer().verify(user.getPassword().toCharArray(), bcryptHashString);
                return result.verified;
            }else{
                return false;
            }            
        }catch(Exception e){
             System.out.println("Error: UserBean, checkUser: " + e);
             return false;
        }
    }
    
    /**
     * Lägger till användaren i databasen om den inte redan finns
     * 
     * @param user
     * @return
     */
    public int saveUser(User user){
        /* Tar bort Base64 hashningen från lösenordet */
        byte[] hashedBytes = Base64.getDecoder().decode(user.getPassword());
        String decodedPassword = new String(hashedBytes);
        
        /* Hashar lösenorder med hjälp av Bcrypt */
        String hashedpassword = BCrypt.withDefaults().hashToString(12, decodedPassword.toCharArray());
        
        try (Connection con = ConnectionFactory.getConnection()){        
            String sql = "INSERT INTO users(username, mail, hashed_password) VALUES(?, ?, ?)";
            PreparedStatement prepStmt = con.prepareStatement(sql);
            prepStmt.setString(1, user.getUsername());
            prepStmt.setString(2, user.getMail());
            prepStmt.setString(3, hashedpassword);
            
            return prepStmt.executeUpdate();
        }catch (Exception e) {
            System.out.println("Error: CommentBean, getComments: " + e);
            return 0;
        }
    }
}
