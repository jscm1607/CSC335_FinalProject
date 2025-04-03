package controller;

import model.RestaurantDB; // Importing Model class
import view.View; // Importing View class

public class Controller {
    public static void main(String[] args) {
        // FIXME: placeholder code
        View view = new View();
        // Example SQL to create and populate a test_table
        String createTableSQL = "CREATE TABLE IF NOT EXISTS test_table (" +
                                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                                "name VARCHAR(255), " +
                                "age INT);";

        String insertDataSQL = "INSERT INTO test_table (name, age) VALUES " +
                               "('Alice', 30), " +
                               "('Bob', 25), " +
                               "('Charlie', 35);";
        try {
            RestaurantDB.executeSQL(createTableSQL);
            RestaurantDB.executeSQL(insertDataSQL);
            RestaurantDB.runH2Console(); // Call the test method to set up the database
        } catch (Exception e) {
            e.printStackTrace(); // Handle any exceptions that may occur
        }
    }
}
