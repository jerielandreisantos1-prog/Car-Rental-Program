import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;


public class LoginScreen {

    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginScreen() {
        initUI();
    }

    private void initUI() {
        frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(380, 180);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(userLabel, gbc);

        usernameField = new JTextField(18);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(usernameField, gbc);

        JLabel passLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passLabel, gbc);

        passwordField = new JPasswordField(18);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(passwordField, gbc);

        JButton loginBtn = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(loginBtn, gbc);

        JButton exitBtn = new JButton("Exit");
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(exitBtn, gbc);

        loginBtn.addActionListener(e -> performLogin());
        exitBtn.addActionListener(e -> System.exit(0));

        frame.getContentPane().add(panel);
        frame.getRootPane().setDefaultButton(loginBtn);
        frame.setVisible(true);
    }

    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter both username and password.", "Missing fields", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ArrayList<User> users = null;
        try {
            users = FileManager.loadUserList("userList");
        } catch (Throwable t) {
            // FileManager.loadUserList prints a stacktrace on failure; show a friendly dialog.
            JOptionPane.showMessageDialog(frame, "Unable to load users; check console for details.", "Load error", JOptionPane.ERROR_MESSAGE);
            t.printStackTrace();
            return;
        }

        if (users == null || users.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No users found (userList file missing or empty).", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (User u : users) {
            if (u != null && username.equals(u.userName) && password.equals(u.password)) {
                JOptionPane.showMessageDialog(frame, "Login successful. Welcome, " + u.name + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
                if ("Admin".equalsIgnoreCase(u.permission)) {
                    AdminScreen adminScreen = new AdminScreen();
                    adminScreen.setVisible(true);
                } else if ("Client".equalsIgnoreCase(u.permission)) {
                    ClientScreen clientScreen = new ClientScreen(u);
                    clientScreen.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Unknown permission: " + u.permission, "Error", JOptionPane.ERROR_MESSAGE);
                }
                return;
            }
        }

        JOptionPane.showMessageDialog(frame, "Invalid username or password.", "Login failed", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginScreen());
    }
}
