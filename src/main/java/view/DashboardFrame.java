package view;

import model.Server;

import javax.swing.*;

public class DashboardFrame extends JFrame {

    public DashboardFrame(Server server) {
        setTitle("Restaurant Dashboard");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // center window

        RestaurantManagerPanel panel = new RestaurantManagerPanel(server);
        add(panel);

        setVisible(true);
    }
}

