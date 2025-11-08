import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ManageClients extends JFrame {
    private DefaultTableModel tableModel;
    private JTable table;
    private ArrayList<User> clients;

    public ManageClients() {
        super("Manage Clients");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

                JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                JButton addBtn = new JButton("Add");
                JButton editBtn = new JButton("Edit");
                JButton removeBtn = new JButton("Remove");
                topPanel.add(addBtn);
                topPanel.add(editBtn);
                topPanel.add(removeBtn);
        
                String[] cols = {"Name", "Email", "Contact"};
                tableModel = new DefaultTableModel(cols, 0) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
                table = new JTable(tableModel);
                JScrollPane scroll = new JScrollPane(table);
        
                getContentPane().setLayout(new BorderLayout());
                getContentPane().add(topPanel, BorderLayout.NORTH);
                getContentPane().add(scroll, BorderLayout.CENTER);
        
                // Load clients from file (filter for permission == "Client")
                ArrayList<User> allUsers = FileManager.loadUserList(UserListManager.filename);
                clients = new ArrayList<>();
                for (User u : allUsers) {
                    if ("Client".equals(u.permission)) {
                        clients.add(u);
                    }
                }
                refreshTable();
        
                addBtn.addActionListener(e -> onAdd());
                editBtn.addActionListener(e -> onEdit());
                removeBtn.addActionListener(e -> onRemove());
            }
        
            private void refreshTable() {
                tableModel.setRowCount(0);
                for (User c : clients) {
                    tableModel.addRow(new Object[]{c.getName(), c.getEmail(), c.getContact()});
                }
            }
        
            private void onAdd() {
                ClientForm form = new ClientForm(this, null);
                User c = form.showDialog();
                if (c != null) {
                    clients.add(c);
                    saveAllClients();
                    refreshTable();
                }
            }
        
            private void onEdit() {
                int row = table.getSelectedRow();
                if (row < 0) {
                    JOptionPane.showMessageDialog(this, "Select a client to edit", "No selection", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                User existing = clients.get(row);
                ClientForm form = new ClientForm(this, existing);
                User updated = form.showDialog();
                if (updated != null) {
                    clients.set(row, updated);
                    saveAllClients();
                    refreshTable();
                }
            }
        
            private void onRemove() {
                int row = table.getSelectedRow();
                if (row < 0) {
                    JOptionPane.showMessageDialog(this, "Select a client to remove", "No selection", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int choice = JOptionPane.showConfirmDialog(this, "Remove selected client?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    clients.remove(row);
                    saveAllClients();
                    refreshTable();
                }
            }
        
            // Save all users (clients + others) back to file
            private void saveAllClients() {
                ArrayList<User> allUsers = FileManager.loadUserList(UserListManager.filename);
                ArrayList<User> newList = new ArrayList<>();
                // Keep non-clients
                for (User u : allUsers) {
                    if (!"Client".equals(u.permission)) {
                        newList.add(u);
                    }
                }
                // Add all clients
                newList.addAll(clients);
                FileManager.saveUserList(newList, UserListManager.filename);
            }
        
            // Simple dialog for add/edit
            static class ClientForm extends JDialog {
                private JTextField nameField;
                private JTextField emailField;
                private JTextField contactField;
                private JTextField userNameField;
                private JTextField passwordField;
                private User result = null;
        
                public ClientForm(Frame owner, User existing) {
                    super(owner, true);
                    setTitle(existing == null ? "Add Client" : "Edit Client");
                    setLayout(new BorderLayout());
        
                    JPanel form = new JPanel(new GridLayout(5, 2, 8, 8));
                    form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                    form.add(new JLabel("Name:"));
                    nameField = new JTextField();
                    form.add(nameField);
                    form.add(new JLabel("Email:"));
                    emailField = new JTextField();
                    form.add(emailField);
                    form.add(new JLabel("Contact:"));
                    contactField = new JTextField();
                    form.add(contactField);
                    form.add(new JLabel("Username:"));
                    userNameField = new JTextField();
                    form.add(userNameField);
                    form.add(new JLabel("Password:"));
                    passwordField = new JTextField();
                    form.add(passwordField);
        
                    if (existing != null) {
                        nameField.setText(existing.getName());
                        emailField.setText(existing.getEmail());
                        contactField.setText(existing.getContact());
                        userNameField.setText(existing.userName);
                        passwordField.setText(existing.password);
                    }
        
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
                SwingUtilities.invokeLater(() -> {
                    ManageClients ui = new ManageClients();
                    ui.setVisible(true);
                });
            }
        }
