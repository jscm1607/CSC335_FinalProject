/* The GUI was coded using generative AI. */
/* 
 * RegisterFrame.java
 * This class defines a basic Swing window that serves as
 * the registration for the restaurant application. This includes
 * username and password input validation.
 * 
 * */

package view;

import javax.swing.*;

import backend.Server;
import backend.ServerDAO;

import java.awt.*;
import java.awt.event.ActionEvent;

public class RegisterFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTextField usernameField;
    private JPasswordField passwordField;
    private ServerDAO serverDAO;

    public RegisterFrame() {
        super("Create Server Account");

        this.serverDAO = new ServerDAO();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null); // center window
        setLayout(new GridLayout(4, 1, 10, 10));

        add(createLabelPanel("New Username:"));
        usernameField = new JTextField();
        add(usernameField);

        add(createLabelPanel("New Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        JButton registerButton = new JButton("Create Account");
        registerButton.addActionListener(this::handleRegister);
        add(registerButton);

        setVisible(true);
    }

    private JPanel createLabelPanel(String labelText) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(labelText);
        panel.add(label, BorderLayout.WEST);
        return panel;
    }

    private void handleRegister(ActionEvent e) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and password cannot be empty.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Username validation
        if (username.length() < 6) {
            JOptionPane.showMessageDialog(this, "Username must be at least 6 characters long.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Password validation
        if (password.length() < 8) {
            JOptionPane.showMessageDialog(this, "Password must be at least 8 characters long.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!password.matches(".*\\d.*")) {
            JOptionPane.showMessageDialog(this, "Password must contain at least one number.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!password.matches(".*[^a-zA-Z0-9].*")) {
            JOptionPane.showMessageDialog(this, "Password must contain at least one special character (non-alphabetic).", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (serverDAO.select(username) != null) {
            JOptionPane.showMessageDialog(this, "Username already exists.", "Account Creation Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        
        Server newServer = new Server(0, username, password);
        serverDAO.insert(newServer); // pass to the db object

        JOptionPane.showMessageDialog(this, "Account created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        dispose();
        new LoginFrame();
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(RegisterFrame::new);
    }
}
