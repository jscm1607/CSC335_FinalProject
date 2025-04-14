package view;

import dao.DBM;
import dao.ServerDAO;
import model.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class RegisterFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private DBM db;
    private ServerDAO serverDAO;

    public RegisterFrame() {
        super("Create Server Account");

        this.db = new DBM("sa", "", "jdbc:h2:./data/db");
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

        if (serverDAO.select(username, db) != null) {
            JOptionPane.showMessageDialog(this, "Username already exists.", "Account Creation Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Server newServer = new Server(0, username, password);
        serverDAO.insert(newServer, db); // pass to the db object

        JOptionPane.showMessageDialog(this, "Account created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        dispose();
        new LoginFrame();
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(RegisterFrame::new);
    }
}
