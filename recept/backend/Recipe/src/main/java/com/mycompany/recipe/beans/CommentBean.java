/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.recipe.beans;

import com.mycompany.recipe.ConnectionFactory;
import com.mycompany.recipe.entities.Comment;
import com.mysql.jdbc.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.ejb.Stateless;

/**
 *
 * @author jespe
 */
@Stateless
public class CommentBean {        

    /**
     * Hämtar alla kommentarer från databasen med hjälp av receptets id  
     * 
     * @param recipeId
     * @return
     */
    public List<Comment> getComments(int recipeId){
        List comments = new ArrayList();

        try(Connection con = ConnectionFactory.getConnection()){
            /* Hämtar kommentarens innehåll, datum */
            String sql = "SELECT users.username, comments.content, comments.date, comments.id FROM users, comments "
                    + "WHERE comments.id IN (SELECT comment_id FROM recipe_comments WHERE recipe_id = ?) AND users.id IN (SELECT user_id FROM comment_user WHERE comment_id = comments.id) "
                    + "ORDER BY id DESC";
            PreparedStatement prepStmt = con.prepareStatement(sql);
            prepStmt = con.prepareStatement(sql);
            prepStmt.setInt(1, recipeId);
            ResultSet commentData = prepStmt.executeQuery();
                             
            while(commentData.next()){                
                /* Lägger till kommentarerna + användarnamnet i listan */
                comments.add(new Comment(commentData.getString("content"), commentData.getString("date"), commentData.getString("username")));
            }
        }catch(Exception e){
            System.out.println("Error: CommentBean, getComments: " + e);
        }
        
        return comments;
    }
    
    /**
     * Spara kommentaren i databasen från content + datum
     * 
     * @param comment
     * @return
     */
    public int saveComment(Comment comment){
        /* Skapar datum formater som kommentaren ska ha */
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd - HH:mm");
        LocalDateTime now = LocalDateTime.now();
        
        try(Connection con = ConnectionFactory.getConnection()){
            /* Lägger in kommentaren i databasen med content + date */
            String sql = "INSERT INTO comments(content, date) VALUES(?, ?)";
            PreparedStatement prepStmt = con.prepareStatement(sql);
            prepStmt.setString(1, comment.getContent());
            prepStmt.setString(2, dtf.format(now));
            prepStmt.executeUpdate();
            
            //Hämta den skapade kommentarens id ^^^^
            sql = "SELECT id FROM comments ORDER BY id DESC LIMIT 1";
            prepStmt = con.prepareStatement(sql);
            ResultSet commentData = prepStmt.executeQuery();
            commentData.next();
            
            /* Hämta användaren som skapade kommentarens id */
            sql = "SELECT id FROM users WHERE username = ?";
            prepStmt = con.prepareStatement(sql);
            prepStmt.setString(1, comment.getUsername());
            ResultSet userData = prepStmt.executeQuery();
            userData.next();
            
            /* Lägger till kommentarens id + användarens id i realationstabellen */
            sql = "INSERT INTO comment_user(comment_id, user_id) VALUES(?, ?)";
            prepStmt = con.prepareStatement(sql);
            prepStmt.setInt(1, commentData.getInt("id"));
            prepStmt.setInt(2, userData.getInt("id"));
            prepStmt.executeUpdate();
            
            /* Lägger till kommentarens id + receptets id i relationstabellen */
            sql = "INSERT INTO recipe_comments(recipe_id, comment_id) VALUES(?, ?)";
            prepStmt = con.prepareStatement(sql);
            prepStmt.setInt(1, comment.getRecipeId());
            prepStmt.setInt(2, commentData.getInt("id"));
            
            return prepStmt.executeUpdate();
        }catch(Exception e){
            System.out.println("Error: CommentBean, saveComments: " + e);
            return 0;
        }        
    }
}
