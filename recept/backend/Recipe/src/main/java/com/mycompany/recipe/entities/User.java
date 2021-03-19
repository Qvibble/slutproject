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
public class User {
    private String username;
    private String password;
    private String mail;
    
    public User(String username, String password){
        this.username = username;
        this.password = password;
    }
    
    public User(String username, String password, String mail){
        this.username = username;
        this.password = password;
        this.mail = mail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
    
    
}
