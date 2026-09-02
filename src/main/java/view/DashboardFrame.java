/* The GUI was coded using generative AI. */
/* 
 * DashboardFrame.java
 * This class defines a basic Swing window that serves as
 * the main dashboard for the restaurant application.
 * 
 * */

package view;

import javax.swing.*;

import backend.Server;

public class DashboardFrame extends JFrame {
	private static final long serialVersionUID = 1L;

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

