 import java.awt.*;
        import java.awt.event.*;
        import java.time.LocalDate;
        import java.time.format.DateTimeFormatter;
        import java.time.temporal.ChronoUnit;
        import java.util.ArrayList;
        import javax.swing.*;
        import javax.swing.table.DefaultTableModel;

        public class ApplicationScreen extends JFrame {
            private JTextField startDateField;
            private JTextField endDateField;
            private DefaultTableModel tableModel;
            private JTable vehicleTable;
            private ArrayList<Vehicle> vehicles;
            private ArrayList<Transaction> transactions;
            private User currentClient;

            public ApplicationScreen(User client) {
                super("Apply to Rent Vehicle");
                this.currentClient = client;
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                setSize(800, 400);
                setLocationRelativeTo(null);
                setLayout(new BorderLayout());

                // Top panel for date selection
                JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                topPanel.add(new JLabel("Start Date (yyyyMMdd):"));
                startDateField = new JTextField(10);
                topPanel.add(startDateField);
                topPanel.add(new JLabel("End Date (yyyyMMdd):"));
                endDateField = new JTextField(10);
                topPanel.add(endDateField);
                JButton filterBtn = new JButton("Check Availability");
                topPanel.add(filterBtn);
                add(topPanel, BorderLayout.NORTH);

                // Table for vehicles
                String[] cols = {"Type", "Price per Day", "Available"};
                tableModel = new DefaultTableModel(cols, 0) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
                vehicleTable = new JTable(tableModel);
                JScrollPane scroll = new JScrollPane(vehicleTable);
                add(scroll, BorderLayout.CENTER);

                // Load vehicles and transactions
                vehicles = VehicleFileManager.loadVehicleList("vehicleList");
                transactions = TransactionListManager.getList();
               
                refreshTable();

                filterBtn.addActionListener(e -> refreshTable());
                vehicleTable.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() == 2) {
                            int row = vehicleTable.getSelectedRow();
                            if (row >= 0) {
                                Vehicle v = vehicles.get(row);
                                String startDateStr = startDateField.getText().trim();
                                String endDateStr = endDateField.getText().trim();
                                if (startDateStr.isEmpty() || endDateStr.isEmpty()) {
                                    JOptionPane.showMessageDialog(ApplicationScreen.this, "Please enter both start and end dates.", "Missing fields", JOptionPane.WARNING_MESSAGE);
                                    return;
                                }
                                LocalDate startDate, endDate;
                                try {
                                    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd");
                                    startDate = LocalDate.parse(startDateStr, fmt);
                                    endDate = LocalDate.parse(endDateStr, fmt);
                                } catch (Exception ex) {
                                    JOptionPane.showMessageDialog(ApplicationScreen.this, "Invalid date format. Use yyyyMMdd.", "Date error", JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                                if (!v.availabilityCheck(startDate, endDate, transactions)) {
                                    JOptionPane.showMessageDialog(ApplicationScreen.this, "Vehicle is not available for the selected dates.", "Unavailable", JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                                long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;
                                double pricePerDay = Double.parseDouble(v.getPrice());
                                double totalPrice = pricePerDay * days;
                                int confirm = JOptionPane.showConfirmDialog(ApplicationScreen.this,
                                        "Apply for vehicle '" + v.getType() + "' from " + startDate + " to " + endDate +
                                        "?\nTotal price: " + totalPrice,
                                        "Confirm Application", JOptionPane.YES_NO_OPTION);
                                if (confirm == JOptionPane.YES_OPTION) {
                                    String transactionID = "TXN" + System.currentTimeMillis();
                                    Transaction txn = new Transaction(startDateStr, endDateStr, v, totalPrice, transactionID, currentClient);
                                    v.setAvailability(false);
                                    VehicleFileManager.saveVehicleList(vehicles, "vehicleList");
                                    TransactionListManager.add(txn);
                                    JOptionPane.showMessageDialog(ApplicationScreen.this, "Application submitted! Transaction ID: " + transactionID);
                                    refreshTable();
                                }
                            }
                        }
                    }
                });
            }

            private void refreshTable() {
                tableModel.setRowCount(0);
                String startDateStr = startDateField.getText().trim();
                String endDateStr = endDateField.getText().trim();
                LocalDate startDate = null, endDate = null;
                try {
                    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd");
                    startDate = LocalDate.parse(startDateStr, fmt);
                    endDate = LocalDate.parse(endDateStr, fmt);
                } catch (Exception ex) {
                    // Ignore parse errors, show all vehicles
                }
                for (Vehicle v : vehicles) {
                    boolean available = true;
                    if (startDate != null && endDate != null) {
                        available = v.availabilityCheck(startDate, endDate, transactions);
                    } else {
                        available = v.getAvailability();
                    }
                    tableModel.addRow(new Object[]{v.getType(), v.getPrice(), available ? "Yes" : "No"});
                }
            }

            public static void main(String[] args) {
                // For demo/testing: create a dummy client
                User demoClient = new Client("John Doe", "1234567890", "john@example.com", "johndoe", "password");
                demoClient.permission = "Client";
                SwingUtilities.invokeLater(() -> {
                    ApplicationScreen ui = new ApplicationScreen(demoClient);
                    ui.setVisible(true);
                });
            }
        }
