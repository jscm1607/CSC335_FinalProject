
package view;

import dao.ServerDAO;
import model.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginFrame extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField usernameField;
    private JPasswordField passwordField;
    private ServerDAO serverDAO;

    public LoginFrame() {
        super("Server Login");
        this.serverDAO = new ServerDAO();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null); // center on screen
        setLayout(new GridLayout(4, 1, 10, 10));

        add(createLabelPanel("Username:"));
        usernameField = new JTextField();
        add(usernameField);

        add(createLabelPanel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(this::handleLogin);
        add(loginButton);

        setVisible(true);
    }

    private JPanel createLabelPanel(String labelText) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(labelText);
        panel.add(label, BorderLayout.WEST);
        return panel;
    }

    private void handleLogin(ActionEvent e) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        Server server = serverDAO.select(username);
        if (server != null && server.getPassword().equals(password)) {
            JOptionPane.showMessageDialog(this, "Login successful! Welcome " + username);
            dispose();
            new DashboardFrame(server);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}
