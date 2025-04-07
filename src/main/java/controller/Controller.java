package controller;

import dao.DatabaseManager;
import view.View; // Importing View class

public class Controller {
    public static void main(String[] args) {
        // FIXME: placeholder code
        View view = new View();
        DatabaseManager.runH2Console();
    }
}
