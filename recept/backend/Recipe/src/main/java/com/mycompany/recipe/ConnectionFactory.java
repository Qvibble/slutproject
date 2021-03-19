/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.recipe;

import java.sql.SQLException;
import com.mysql.jdbc.Connection;
import java.sql.DriverManager;

/**
 *
 * @author jespe
 */
public class ConnectionFactory {
    public static Connection getConnection() throws ClassNotFoundException, SQLException{
        String user = "recipe";
        String password = "recipe123";
        String url = "jdbc:mysql://localhost/recipe";
        Class.forName("com.mysql.jdbc.Driver");
        return (Connection)DriverManager.getConnection(url, user, password);
    }
}
