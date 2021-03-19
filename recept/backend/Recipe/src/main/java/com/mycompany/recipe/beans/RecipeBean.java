/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.recipe.beans;

import com.mycompany.recipe.ConnectionFactory;
import com.mycompany.recipe.entities.Category;
import com.mycompany.recipe.entities.Ingredient;
import com.mycompany.recipe.entities.Recipe;
import com.mycompany.recipe.entities.User;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import javax.ejb.Stateless;

/**
 *
 * @author jespe
 */
@Stateless
public class RecipeBean {
    public List<Recipe> getNewRecipes(){
        List<Recipe> recipes = new ArrayList<>();
        
        /* Hämtar 9 senast tillagda recept */
        try(Connection con = ConnectionFactory.getConnection()){
            String sql = "SELECT * FROM recipes ORDER BY id DESC LIMIT 9";
            PreparedStatement prepStmt = con.prepareStatement(sql);
            ResultSet recipeData = prepStmt.executeQuery();

            while(recipeData.next()){
                int id = recipeData.getInt("id");
                String name = recipeData.getString("name");
                String image = recipeData.getString("image");
                if(!imageToB64(image).equals("")){
                    image = imageToB64(image);                    
                }

                recipes.add(new Recipe(id, name, image));
            }
            
            return recipes;
        }catch(Exception e){
            System.out.println("Error: RecipeBean, getNewRecipes: " + e);
            return null;
        }
    }

    public List<Recipe> getRandomRecipes(){
        List<Recipe> recipes = new ArrayList<>();
        
        /* Hämtar 9 slumpade recept */
        try(Connection con = ConnectionFactory.getConnection()){
            String sql = "SELECT * FROM recipes ORDER BY RAND() LIMIT 9";
            PreparedStatement prepStmt = con.prepareStatement(sql);
            ResultSet recipeData = prepStmt.executeQuery();

            while(recipeData.next()){
                int id = recipeData.getInt("id");
                String name = recipeData.getString("name");
                String image = recipeData.getString("image");
                if(!imageToB64(image).equals("")){
                    image = imageToB64(image);                    
                }
            
                recipes.add(new Recipe(id, name, image));
            }
            
            return recipes;
        }catch(Exception e){
            System.out.println("Error: RecipeBean, getRandomRecipes: " + e);
            return null;
        }
    }
    
    public List<Recipe> getLikedRecipes(String username){
        List<Recipe> recipes = new ArrayList<>();
        
        /* Hämtar användarens gillade recept */
        try(Connection con = ConnectionFactory.getConnection()){
            /* Hämtar användarens id med hjälp av användarnamnet */
            String sql = "SELECT id FROM users WHERE username = ?";
            PreparedStatement prepStmt = con.prepareStatement(sql);
            prepStmt.setString(1, username);
            ResultSet userData = prepStmt.executeQuery();     
            userData.next();

            /* Hämtar recept id */
            sql = "SELECT recipe_id FROM likes WHERE user_id = ?";
            prepStmt = con.prepareStatement(sql);
            prepStmt.setInt(1, userData.getInt("id"));
            ResultSet likedData = prepStmt.executeQuery();

            /* Hämtar alla gillade recept */
            while(likedData.next()){
                sql = "SELECT * FROM recipes WHERE id = ?";
                prepStmt = con.prepareStatement(sql);
                prepStmt.setInt(1, likedData.getInt("recipe_id"));
                ResultSet recipeData = prepStmt.executeQuery();

                while(recipeData.next()){
                    int id = recipeData.getInt("id");
                    String name = recipeData.getString("name");
                    String image = recipeData.getString("image");
                    if(!imageToB64(image).equals("")){
                        image = imageToB64(image);                    
                    }
                
                    recipes.add(new Recipe(id, name, image));
                }                
            }

            return recipes;
        }catch(Exception e){
            System.out.println("Error: RecipeBean, getLikedRecipes: " + e);
            return null;
        }
    }
    
    public List<Recipe> getUserRecipes(String username){
        List<Recipe> recipes = new ArrayList<>();
        
        /* Hämtar användarens skapade recept */
        try(Connection con = ConnectionFactory.getConnection()){
            /* Hämtar användarens id med hjälp av användarnamnet */
            String sql = "SELECT id FROM users WHERE username = ?";
            PreparedStatement prepStmt = con.prepareStatement(sql);
            prepStmt.setString(1, username);
            ResultSet userData = prepStmt.executeQuery();     
            userData.next();
            
            /* Hämtar recept id */
            sql = "SELECT recipe_id FROM user_recipes WHERE user_id = ?";
            prepStmt = con.prepareStatement(sql);
            prepStmt.setInt(1, userData.getInt("id"));
            ResultSet recipeId = prepStmt.executeQuery();
            
            /* Hämtar alla recept som användaren skapat recept */
            while(recipeId.next()){
                sql = "SELECT * FROM recipes WHERE id = ?";
                prepStmt = con.prepareStatement(sql);
                prepStmt.setInt(1, recipeId.getInt("recipe_id"));
                ResultSet recipeData = prepStmt.executeQuery();

                while(recipeData.next()){
                    int id = recipeData.getInt("id");
                    String name = recipeData.getString("name");
                    String image = recipeData.getString("image");
                    if(!imageToB64(image).equals("")){
                        image = imageToB64(image);                    
                    }                    

                    recipes.add(new Recipe(id, name, image));
                }                
            }

            return recipes;
        }catch(Exception e){
            System.out.println("Error: RecipeBean, getUserRecipes: " + e);
            return null;
        }
    }
    
    public List<Recipe> getSearchedRecipes(String searchTerm){
        List<Recipe> recipes = new ArrayList<>();

        /* Hämtar alla recept som innehåller söktermen */
        try(Connection con = ConnectionFactory.getConnection()){
            //Allt som börjard med searchTerm för och sedan allt som innehåller searchTerm
            String sql = "SELECT * FROM recipes WHERE name LIKE ? ORDER BY CASE WHEN name like ? THEN 0 ELSE 1 END, name"; 
            PreparedStatement prepStmt = con.prepareStatement(sql);
            prepStmt.setString(1, "%" + searchTerm + "%");
            prepStmt.setString(2, searchTerm + "%");
            ResultSet recipeData = prepStmt.executeQuery();

            while(recipeData.next()){
                int id = recipeData.getInt("id");
                String name = recipeData.getString("name");
                String image = recipeData.getString("image");
                if(!imageToB64(image).equals("")){
                    image = imageToB64(image);                    
                }
                
                recipes.add(new Recipe(id, name, image));
            }
            
            return recipes;
        }catch(Exception e){
            System.out.println("Error: RecipeBean, getSearchedRecipes: " + e);
            return null;
        }
    }
    
    public List<Recipe> getCategoryRecipes(String category){
        List<Recipe> recipes = new ArrayList<>();

        /* Hämtar alla recept som innehåller söktermen */
        try(Connection con = ConnectionFactory.getConnection()){
            //Allt som börjard med searchTerm för och sedan allt som innehåller searchTerm
            String sql = "SELECT * FROM recipes WHERE id = (SELECT recipe_id FROM recipe_categories WHERE category_id = (SELECT id FROM categories WHERE name = ?))"; 
            PreparedStatement prepStmt = con.prepareStatement(sql);
            prepStmt.setString(1, category);
            ResultSet recipeData = prepStmt.executeQuery();

            while(recipeData.next()){
                int id = recipeData.getInt("id");
                String name = recipeData.getString("name");
                String image = recipeData.getString("image");
                if(!imageToB64(image).equals("")){
                    image = imageToB64(image);                    
                }                               
                
                recipes.add(new Recipe(id, name, image));
            }
            
            return recipes;
        }catch(Exception e){
            System.out.println("Error: RecipeBean, getSearchedRecipes: " + e);
            return null;
        }
    }
    
    public List<Recipe> getRecipe(String idUsername){
        List<Recipe> recipes = new ArrayList<>();
        
        //Håller koll på om användaren gillat receptet
        boolean liked = false;

        //Dela upp strängen
        String[] s = idUsername.split("\\|");
        String recipeId = s[0];
        String currentUser = s[1];
        
        try(Connection con = ConnectionFactory.getConnection()){
            /* Hämtar namn, beskrivning, steg, bild, likes, användarnamn från id */
            String sql = "SELECT recipes.name, recipes.description, recipes.steps, recipes.image, "
                    + "(SELECT username FROM users WHERE id IN (SELECT user_id FROM user_recipes WHERE recipe_id = ?)) as \"username\", "
                    + "(SELECT COUNT(*) FROM likes WHERE recipe_id = ?) as \"likes\" FROM recipes WHERE recipes.id = ?";
            PreparedStatement prepStmt = con.prepareStatement(sql);
            prepStmt.setInt(1, Integer.parseInt(recipeId));
            prepStmt.setInt(2, Integer.parseInt(recipeId));
            prepStmt.setInt(3, Integer.parseInt(recipeId));
            ResultSet recipeData = prepStmt.executeQuery();
            
            while(recipeData.next()){
                int id = Integer.parseInt(recipeId);
                int likes = recipeData.getInt("likes");
                String usernamne = recipeData.getString("username");
                String name = recipeData.getString("name");
                String description = recipeData.getString("description");
                String steps = recipeData.getString("steps");
                String image = recipeData.getString("image");
                if(!imageToB64(image).equals("")){
                    image = imageToB64(image);                    
                }
                
                /* Hämtar om användaren gillat receptet */        
                sql = "SELECT * FROM likes WHERE recipe_id = ? AND user_id = (SELECT id FROM users WHERE username = ?)";
                prepStmt = con.prepareStatement(sql);
                prepStmt.setInt(1, id);
                prepStmt.setString(2, currentUser);
                ResultSet likedData = prepStmt.executeQuery();
                if(likedData.next()){
                    liked = true;
                }
                
                /* Hämtar alla kategorier som receptet har */
                List<Category> categories = new ArrayList<>();
                
                sql = "SELECT name FROM categories WHERE id IN (SELECT category_id FROM recipe_categories WHERE recipe_id = ?)";
                prepStmt = con.prepareStatement(sql);
                prepStmt.setInt(1, id);
                ResultSet categoryData = prepStmt.executeQuery();
                while(categoryData.next()){
                    categories.add(new Category(categoryData.getString("name").toLowerCase()));
                }
                
                /* Hämtar alla ingredienser */
                List<Ingredient> ingredients = new ArrayList<>();
                
                sql = "SELECT ingredients.name, recipe_ingredients.amount FROM ingredients, recipe_ingredients "
                        + "WHERE recipe_ingredients.recipe_id = ? AND recipe_ingredients.ingredient_id = ingredients.id";
                prepStmt = con.prepareStatement(sql);
                prepStmt.setInt(1, id);
                ResultSet ingredientData = prepStmt.executeQuery();
                while(ingredientData.next()){
                    ingredients.add(new Ingredient(ingredientData.getString("name"), ingredientData.getString("amount")));
                }
                
                recipes.add(new Recipe(id, usernamne, name, description, steps, ingredients, image, likes, categories, liked));
            }
            
            
            return recipes;
        }catch(Exception e){
            System.out.println("Error: RecipeBean, getRecipe: " + e);
            return null;
        }
    }
    
    private final String absolutePath = "C:\\Users\\Elev\\Documents\\NetBeansProjects\\Recipe\\target\\Recipe-1.0-SNAPSHOT\\";
    private final String folderName = "images";
    
    public int saveRecipe(Recipe recipe){
        try(Connection con = ConnectionFactory.getConnection()){
            /* Base64 till sträng - tar bort data url */
            String base64 = recipe.getImage().substring(recipe.getImage().indexOf(",")+1);

            byte[] byteData = Base64.getDecoder().decode(base64);

            /* Slumpar namn */
            byte[] array = new byte[6]; // length is bounded by 7
            new Random().nextBytes(array);
            String generatedString = Base64.getEncoder().encodeToString(array);

            //Relativ sökväg
            String relativePath = folderName + "\\" + generatedString + ".png";
            
            try{
                Files.write(Paths.get(absolutePath, relativePath), byteData);
            }catch(IOException e){
                System.out.println("Error: RecipeBean, createImage. Filen kan inte skapas: " + e.getMessage());
                return 0;
            }
            
            //Fixar relative path efter filen skapats
            relativePath = relativePath.replace("\\", "/");
            
            String sql = "INSERT INTO recipes(name, description, steps, image) VALUES(?, ?, ?, ?)";
            PreparedStatement prepStmt = con.prepareStatement(sql);
            prepStmt.setString(1, recipe.getName());
            prepStmt.setString(2, recipe.getDescription());
            prepStmt.setString(3, recipe.getSteps());
            prepStmt.setString(4, relativePath);

            prepStmt.executeUpdate();

            /* Lägg till ingredienser i databasen om de inte redan finns */            
            for(Ingredient i : recipe.getIngredients()){
                sql = "INSERT INTO ingredients(name) SELECT * FROM (SELECT ?) AS temp\n" +
                      "WHERE NOT EXISTS (SELECT name FROM ingredients WHERE name = ?)";
                prepStmt = con.prepareStatement(sql);            
                prepStmt.setString(1, i.getName());
                prepStmt.setString(2, i.getName());                
                prepStmt.executeUpdate();
                
                /* Lägg till ingredienser för receptet */
                sql = "INSERT INTO recipe_ingredients(recipe_id, amount, ingredient_id) VALUES((SELECT id FROM recipes ORDER BY ID DESC LIMIT 1), ?, (SELECT id FROM ingredients WHERE name = ?))";
                prepStmt = con.prepareStatement(sql);
                prepStmt.setString(1, i.getAmount());
                prepStmt.setString(2, i.getName());
                prepStmt.executeUpdate();
            }
            
            /* Sätter kategori på receptet */
            for(Category c : recipe.getCategories()){
                sql = "INSERT INTO recipe_categories(recipe_id, category_id) VALUES((SELECT id FROM recipes ORDER BY ID DESC LIMIT 1), (SELECT id FROM categories WHERE name = ?))";
                prepStmt = con.prepareStatement(sql);                
                prepStmt.setString(1, c.getName());         
                prepStmt.executeUpdate();
            }
            
            sql = "INSERT INTO user_recipes(user_id, recipe_id) VALUES((SELECT id FROM users WHERE username = ?), (SELECT id FROM recipes ORDER BY ID DESC LIMIT 1))";
            prepStmt = con.prepareStatement(sql);
            prepStmt.setString(1, recipe.getUsername());            
            
            return prepStmt.executeUpdate();
        }catch(Exception e){
            System.out.println("Error: RecipeBean, saveRecipe: " + e);
            return 0;
        }
    }
    
    public int editRecipe(Recipe recipe){               
        try(Connection con = ConnectionFactory.getConnection()){
            /* Base64 till sträng - tar bort data url */
            String base64 = recipe.getImage().substring(recipe.getImage().indexOf(",")+1);

            byte[] byteData = Base64.getDecoder().decode(base64);

            /* Slumpar namn */
            byte[] array = new byte[6]; // length is bounded by 7
            new Random().nextBytes(array);
            String generatedString = Base64.getEncoder().encodeToString(array);

            //Relativ sökväg
            String relativePath = folderName + "\\" + generatedString + ".png";
            
            try{
                Files.write(Paths.get(absolutePath, relativePath), byteData);
            }catch(IOException e){
                System.out.println("Error: RecipeBean, createImage. Filen kan inte skapas: " + e.getMessage());
                return 0;
            }
            
            //Fixar relative path efter filen skapats
            relativePath = relativePath.replace("\\", "/");
            
            String sql = "UPDATE recipes SET name = ?, description = ?, steps = ?, image = ? WHERE id = ?";
            PreparedStatement prepStmt = con.prepareStatement(sql);
            prepStmt.setString(1, recipe.getName());
            prepStmt.setString(2, recipe.getDescription());
            prepStmt.setString(3, recipe.getSteps());
            prepStmt.setString(4, relativePath);
            prepStmt.setInt(5, recipe.getId());
            prepStmt.executeUpdate();
            
            /* Tömmer taqbellen på allt som har med receptet att göra för att sedan fylla den på nytt */
            sql = "DELETE FROM recipe_ingredients WHERE recipe_id = ?";
            prepStmt = con.prepareStatement(sql);
            prepStmt.setInt(1, recipe.getId());
            prepStmt.executeUpdate();
            
            sql = "DELETE FROM recipe_categories WHERE recipe_id = ?";
            prepStmt = con.prepareStatement(sql);
            prepStmt.setInt(1, recipe.getId());
            prepStmt.executeUpdate();
            
            /* Sätter in det nya i tabellerna */
            for(Category c : recipe.getCategories()){
                sql = "INSERT INTO recipe_categories(recipe_id, category_id) VALUES(?, (SELECT id FROM categories WHERE name = ?))";
                prepStmt = con.prepareStatement(sql);
                prepStmt.setInt(1, recipe.getId());
                prepStmt.setString(2, c.getName());
                prepStmt.executeUpdate();
            }
            
            for(Ingredient i : recipe.getIngredients()){
                /* Lägger till ingredienserna om de inte redan finns i databasen */
                sql = "INSERT INTO ingredients(name) SELECT * FROM (SELECT ?) AS temp\n" +
                      "WHERE NOT EXISTS (SELECT name FROM ingredients WHERE name = ?)";
                prepStmt = con.prepareStatement(sql);            
                prepStmt.setString(1, i.getName());
                prepStmt.setString(2, i.getName());                
                prepStmt.executeUpdate();
                
                /* Kopplar samman recept och ingredienser */
                sql = "INSERT INTO recipe_ingredients(recipe_id, amount, ingredient_id) VALUES(?, ?, (SELECT id FROM ingredients WHERE name = ?))";
                prepStmt = con.prepareStatement(sql);
                prepStmt.setInt(1, recipe.getId());
                prepStmt.setString(2, i.getAmount());
                prepStmt.setString(3, i.getName());
                prepStmt.executeUpdate();                
            }

            return 1;
        }catch(Exception e){
            System.out.println("Error: RecipeBean, editRecipe: " + e);
            return 0;
        }
    }
    
    public int removeRecipe(String recipeId){
        try(Connection con = ConnectionFactory.getConnection()){
            String sql = "DELETE FROM recipes WHERE id = ?";
            PreparedStatement prepStmt = con.prepareStatement(sql);
            prepStmt.setString(1, recipeId);
            
            //Fler saker ska tas bort
            //Ta bort likes, vem som skapat recept etc

            return prepStmt.executeUpdate();
        }catch(Exception e){
            System.out.println("Error: RecipeBean, removeRecipe: " + e);
            return 0;
        }
    }
    
    public int addLike(String idUsername){
        //0 = id, 1 = användarnamn
        String[] s = idUsername.split("\\|");
        String recipeId = s[0];
        String currentUser = s[1];
        
        try(Connection con = ConnectionFactory.getConnection()){
            String sql = "INSERT INTO likes(user_id, recipe_id) VALUES((SELECT id FROM users WHERE username = ?), ?)";
            PreparedStatement prepStmt = con.prepareStatement(sql);
            prepStmt.setString(1, currentUser);
            prepStmt.setInt(2, Integer.parseInt(recipeId));
            
            return prepStmt.executeUpdate();
        }catch(Exception e){
            System.out.println("Error: RecipeBean, addLike: " + e);
            return 0;
        }        
    }
    
    public int removeLike(String idUsername){
        //0 = id, 1 = användarnamn
        String[] s = idUsername.split("\\|");
        String recipeId = s[0];
        String currentUser = s[1];
        
        try(Connection con = ConnectionFactory.getConnection()){
            String sql = "DELETE FROM likes WHERE user_id = (SELECT id FROM users WHERE username = ?) AND recipe_id = ?";
            PreparedStatement prepStmt = con.prepareStatement(sql);
            prepStmt.setString(1, currentUser);
            prepStmt.setInt(2, Integer.parseInt(recipeId));

            return prepStmt.executeUpdate();
        }catch(Exception e){
            System.out.println("Error: RecipeBean, removeLike: " + e);
            return 0;
        }        
    }
    
    public String imageToB64(String relativePath){
        if(Files.exists(Paths.get(absolutePath, relativePath))){
            //ClassLoader classLoader = getClass().getClassLoader();
            //File file = new File(classLoader.getResource(absolutePath + relativePath).getFile());
            String base64 = "";
            try{
                byte[] content = Files.readAllBytes(Paths.get(absolutePath + relativePath));
                base64 = Base64.getEncoder().encodeToString(content);
            }catch(IOException e){
                System.out.println("Could not read file: " + e);
            }
            return base64;
            
        }else{
            return "";
        }     
    }
}
