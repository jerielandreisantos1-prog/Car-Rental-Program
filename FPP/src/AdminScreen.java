import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
public class AdminScreen{
    
    private JFrame frame;
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;
    private ArrayList<User> userList;
    private ArrayList<Vehicle> vehicleList;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminScreen().initUI());
    }
    
    private void initUI() {
        frame = new JFrame("Admin Screen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton manageClientsBtn = new JButton("Manage Clients");
        manageClientsBtn.addActionListener(e -> manageClientsUI());

        JButton manageVehiclesBtn = new JButton("Manage Vehicles");
        manageVehiclesBtn.addActionListener(e -> manageVehiclesUI());

        JButton manageApplicationsBtn = new JButton("Manage Applications");
        manageApplicationsBtn.addActionListener(e -> manageApplicationsUI());

        buttonPanel.add(manageClientsBtn);
        buttonPanel.add(manageVehiclesBtn);
        buttonPanel.add(manageApplicationsBtn);

        panel.add(buttonPanel, BorderLayout.NORTH);

        frame.setContentPane(panel);
        frame.setVisible(true);
    }
    private void manageClientsUI(){
        JPanel manageClientsPanel = new JPanel(new BorderLayout());
        JButton addClientBtn = new JButton("Add Client");
        JButton editClientBtn = new JButton("Edit Client");
        JButton backBtn = new JButton("Back");
        

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(addClientBtn);
        buttonPanel.add(editClientBtn);
        buttonPanel.add(backBtn);

        String[] columnNames = { "Client Name", "Client Email", "Contact No."};
        userList = UserListManager.main();
        
        Object[][] data = new Object[userList.size()][3];

        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            data[i][0] = user.getName(); // Assuming User has a getId() method
            data[i][1] = user.getName(); // Assuming User has a getName() method
            data[i][2] = user.getEmail(); // Assuming User has a getEmail() method
        }

        JTable clientsTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(clientsTable);

        manageClientsPanel.add(buttonPanel, BorderLayout.NORTH);
        manageClientsPanel.add(scrollPane, BorderLayout.CENTER);

        frame.setContentPane(manageClientsPanel);
        frame.revalidate();
        frame.repaint();
        addClientBtn.addActionListener(e -> addClientUI());
        
    }
    private void addClientUI(){
        {
            JPanel panel = new JPanel(new BorderLayout());
            JPanel form = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(6, 6, 6, 6);
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridx = 0;
            gbc.gridy = 0;

            JLabel nameLabel = new JLabel("Name:");
            JTextField nameField = new JTextField(20);
            form.add(nameLabel, gbc);
            gbc.gridx = 1;
            form.add(nameField, gbc);

            gbc.gridx = 0;
            gbc.gridy++;
            JLabel contactLabel = new JLabel("Contact:");
            JTextField contactField = new JTextField(20);
            form.add(contactLabel, gbc);
            gbc.gridx = 1;
            form.add(contactField, gbc);

            gbc.gridx = 0;
            gbc.gridy++;
            JLabel emailLabel = new JLabel("Email:");
            JTextField emailField = new JTextField(20);
            form.add(emailLabel, gbc);
            gbc.gridx = 1;
            form.add(emailField, gbc);

            gbc.gridx = 0;
            gbc.gridy++;
            JLabel usernameLabel = new JLabel("Username:");
            JTextField usernameField = new JTextField(20);
            form.add(usernameLabel, gbc);
            gbc.gridx = 1;
            form.add(usernameField, gbc);

            gbc.gridx = 0;
            gbc.gridy++;
            JLabel passwordLabel = new JLabel("Password:");
            JPasswordField passwordField = new JPasswordField(20);
            form.add(passwordLabel, gbc);
            gbc.gridx = 1;
            form.add(passwordField, gbc);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            JButton saveBtn = new JButton("Save");
            JButton backBtn = new JButton("Back");

            saveBtn.addActionListener(e -> {
                String name = nameField.getText().trim();
                String contact = contactField.getText().trim();
                String email = emailField.getText().trim();
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword());

                if (name.isEmpty() || contact.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill all fields.", "Validation", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // If you have a persistence or manager class, add the new user there.
                // This UI only collects input; attempt to add to in-memory list if available.
                boolean added = false;
                if (userList != null) {
                    try {
                        // Try a common constructor (name, contact, email, username, password)
                        java.lang.reflect.Constructor<?> cons = User.class.getConstructor(String.class, String.class, String.class, String.class, String.class);
                        Object u = cons.newInstance(name, contact, email, username, password);
                        userList.add((User) u);
                        added = true;
                    } catch (Exception ex) {
                        // If User constructor is different or not available, skip adding here.
                    }
                }

                JOptionPane.showMessageDialog(frame, "Client saved" + (added ? " to memory." : "."));
                manageClientsUI();
            });

            backBtn.addActionListener(e -> manageClientsUI());

            buttonPanel.add(saveBtn);
            buttonPanel.add(backBtn);

            panel.add(form, BorderLayout.CENTER);
            panel.add(buttonPanel, BorderLayout.SOUTH);

            frame.setContentPane(panel);
            frame.revalidate();
            frame.repaint();
        }
    }
    private void manageVehiclesUI(){

    }
    private void manageApplicationsUI(){

    }



}