package controller;

import dao.DBM;

public class Controller {
    public static void main(String[] args) {
        DBM db = new DBM("sa", "", "jdbc:h2:./data/db");
        db.runH2Console();
        // TODO implement app
    }
}
