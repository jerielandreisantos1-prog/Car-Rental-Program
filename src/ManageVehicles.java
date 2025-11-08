import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ManageVehicles extends JFrame {
    private DefaultTableModel tableModel;
    private JTable table;
    private ArrayList<Vehicle> vehicles;

    public ManageVehicles() {
        super("Manage Vehicles");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Top buttons
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton removeBtn = new JButton("Remove");
        topPanel.add(addBtn);
        topPanel.add(editBtn);
        topPanel.add(removeBtn);

        // Table
        String[] cols = {"Type", "Price", "Available"};
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

        // Load vehicles from file
        vehicles = VehicleFileManager.loadVehicleList(VehicleListManager.fileName);
        refreshTable();

        // Button actions
        addBtn.addActionListener(e -> onAdd());
        editBtn.addActionListener(e -> onEdit());
        removeBtn.addActionListener(e -> onRemove());
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Vehicle v : vehicles) {
            tableModel.addRow(new Object[]{v.getType(), v.getPrice(), v.getAvailability()});
        }
    }

    private void onAdd() {
        VehicleForm form = new VehicleForm(this, null);
        Vehicle v = form.showDialog();
        if (v != null) {
            vehicles.add(v);
            VehicleFileManager.saveVehicleList(vehicles, VehicleListManager.fileName);
            refreshTable();
        }
    }

    private void onEdit() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a vehicle to edit", "No selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Vehicle existing = vehicles.get(row);
        VehicleForm form = new VehicleForm(this, existing);
        Vehicle updated = form.showDialog();
        if (updated != null) {
            vehicles.set(row, updated);
            VehicleFileManager.saveVehicleList(vehicles, VehicleListManager.fileName);
            refreshTable();
        }
    }

    private void onRemove() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a vehicle to remove", "No selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int choice = JOptionPane.showConfirmDialog(this, "Remove selected vehicle?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            vehicles.remove(row);
            VehicleFileManager.saveVehicleList(vehicles, VehicleListManager.fileName);
            refreshTable();
        }
    }

    // Simple dialog for add/edit
    static class VehicleForm extends JDialog {
        private JTextField typeField;
        private JTextField priceField;
        private JCheckBox availableBox;
        private Vehicle result = null;

        public VehicleForm(Frame owner, Vehicle existing) {
            super(owner, true);
            setTitle(existing == null ? "Add Vehicle" : "Edit Vehicle");
            setLayout(new BorderLayout());

            JPanel form = new JPanel(new GridLayout(3, 2, 8, 8));
            form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            form.add(new JLabel("Type:"));
            typeField = new JTextField();
            form.add(typeField);
            form.add(new JLabel("Price:"));
            priceField = new JTextField();
            form.add(priceField);
            form.add(new JLabel("Available:"));
            availableBox = new JCheckBox();
            form.add(availableBox);

            if (existing != null) {
                typeField.setText(existing.getType());
                priceField.setText(existing.getPrice());
                availableBox.setSelected(existing.getAvailability());
            }

            JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton ok = new JButton("OK");
            JButton cancel = new JButton("Cancel");
            buttons.add(ok);
            buttons.add(cancel);

            ok.addActionListener(e -> {
                String type = typeField.getText().trim();
                String price = priceField.getText().trim();
                boolean avail = availableBox.isSelected();
                if (type.isEmpty() || price.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Type and Price are required", "Validation", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                result = new Vehicle(type, price, avail);
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

        public Vehicle showDialog() {
            setVisible(true);
            return result;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ManageVehicles ui = new ManageVehicles();
            ui.setVisible(true);
        });
    }
}
