import java.awt.*;
import javax.swing.*;

public class ClientScreen extends JFrame {
    private User currentClient;

    public ClientScreen(User client) {
        super("Client Screen");
        this.currentClient = client;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        JButton applyBtn = new JButton("Apply to Reserve Vehicle");
        JButton checkBtn = new JButton("Check Applications");
        JButton logoutBtn = new JButton("Logout");
        JButton editBtn = new JButton("Edit My Info");

        buttonPanel.add(applyBtn);
        buttonPanel.add(checkBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(logoutBtn);
        add(buttonPanel, BorderLayout.CENTER);

        // Action listeners (logic to be implemented)
        applyBtn.addActionListener(e -> onApply());
        checkBtn.addActionListener(e -> onCheckApplications());
        logoutBtn.addActionListener(e -> onLogout());
        editBtn.addActionListener(e -> onEditInfo());

        setVisible(true);
    }

    private void onApply() {
        ApplicationScreen appScreen = new ApplicationScreen(currentClient);
        appScreen.setVisible(true);
        

    }

    private void onCheckApplications() {
    CheckApplicationsScreen checkScreen = new CheckApplicationsScreen(currentClient);
    checkScreen.setVisible(true);
    }

    // Dummy transaction list loader
    private java.util.ArrayList<Transaction> loadTransactionList() {
        // TODO: Replace with real file I/O
        return new java.util.ArrayList<>();
    }
    

    private void onLogout() {
        JOptionPane.showMessageDialog(this, "Logout logic goes here.");
        // TODO: Return to login screen
        dispose();
    }

    private void onEditInfo() {
        ClientEditForm form = new ClientEditForm(this, currentClient);
        User updated = form.showDialog();
        if (updated != null) {
            currentClient = updated;
            JOptionPane.showMessageDialog(this, "Info updated!");

            // TODO: Save changes to persistent storage
        }
    }

    // Simple dialog for editing client info
    static class ClientEditForm extends JDialog {
        private JTextField nameField;
        private JTextField emailField;
        private JTextField contactField;
        private JTextField userNameField;
        private JTextField passwordField;
        private User result = null;

        public ClientEditForm(Frame owner, User client) {
            super(owner, true);
            setTitle("Edit My Info");
            setLayout(new BorderLayout());

            JPanel form = new JPanel(new GridLayout(5, 2, 8, 8));
            form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            form.add(new JLabel("Name:"));
            nameField = new JTextField(client.getName());
            form.add(nameField);
            form.add(new JLabel("Email:"));
            emailField = new JTextField(client.getEmail());
            form.add(emailField);
            form.add(new JLabel("Contact:"));
            contactField = new JTextField(client.getContact());
            form.add(contactField);
            form.add(new JLabel("Username:"));
            userNameField = new JTextField(client.userName);
            form.add(userNameField);
            form.add(new JLabel("Password:"));
            passwordField = new JTextField(client.password);
            form.add(passwordField);

            JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton ok = new JButton("OK");
            JButton cancel = new JButton("Cancel");
            buttons.add(ok);
            buttons.add(cancel);

            ok.addActionListener(e -> {
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();
                String contact = contactField.getText().trim();
                String userName = userNameField.getText().trim();
                String password = passwordField.getText().trim();
                if (name.isEmpty() || email.isEmpty() || contact.isEmpty() || userName.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "All fields are required", "Validation", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                User c = new Client(name, contact, email, userName, password);
                c.permission = "Client";
                result = c;
                setVisible(false);
            });
            cancel.addActionListener(e -> {
                result = null;
                setVisible(false);
            });

            add(form, BorderLayout.CENTER);
            add(buttons, BorderLayout.SOUTH);
            pack();
            setLocationRelativeTo(owner);
        }

        public User showDialog() {
            setVisible(true);
            return result;
        }
    }

    public static void main(String[] args) {
        // For demo/testing: create a dummy client
        User demoClient = new Client("John Doe", "1234567890", "john@example.com", "johndoe", "password");
        demoClient.permission = "Client";
        SwingUtilities.invokeLater(() -> {
            ClientScreen ui = new ClientScreen(demoClient);
            ui.setVisible(true);
        });
    }
}
