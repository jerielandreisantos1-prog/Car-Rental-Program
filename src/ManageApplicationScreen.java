import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class ManageApplicationScreen extends JFrame {
    private DefaultTableModel tableModel;
    private JTable table;
        private ArrayList<Transaction> transactions = TransactionListManager.getList();

    public ManageApplicationScreen() {
        super("Manage Applications");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        String[] cols = {"Transaction ID", "User", "Vehicle", "Start Date", "End Date", "Status", "Action"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Only the Action column is editable
                return column == 6;
            }
        };
        table = new JTable(tableModel);
        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

            transactions = TransactionListManager.getList();
        refreshTable();

        table.getColumn("Action").setCellEditor(new DefaultCellEditor(new JComboBox<>(new String[]{"Confirm Payment", "Set Ongoing", "Set Completed", "Set Payment Overdue"})));
        table.getColumn("Action").setCellRenderer(new TableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                return new JLabel(value == null ? "" : value.toString());
            }
        });

        table.getModel().addTableModelListener(e -> {
            int row = e.getFirstRow();
            int col = e.getColumn();
            if (col == 6 && row >= 0) {
                String action = (String) table.getValueAt(row, 6);
                Transaction txn = transactions.get(row);
                if (action != null) {
                    handleAction(txn, action);
                    saveTransactionList(transactions);
                    refreshTable();
                }
            }
        });
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Transaction t : transactions) {
            String userName = t.user != null ? t.user.userName : "Unknown";
            String vehicleName = t.vehicleReserved != null ? t.vehicleReserved.getType() : "";
            String startDate = t.status.startDate != null ? t.status.startDate.toString() : "";
            String endDate = t.status.endDate != null ? t.status.endDate.toString() : "";
            String status = t.status.getStatus();
            tableModel.addRow(new Object[]{t.transactionID, userName, vehicleName, startDate, endDate, status, ""});
        }
    }

    private void handleAction(Transaction txn, String action) {
        if (action.equals("Confirm Payment")) {
            txn.status.successfulPayment = true;
            txn.status.updateStatus();
            if (txn.status.state.equals("Confirmed") || txn.status.state.equals("Ongoing")) {
                txn.vehicleReserved.setAvailability(false);
            }
        } else if (action.equals("Set Ongoing")) {
            txn.status.state = "Ongoing";
            txn.vehicleReserved.setAvailability(false);
        } else if (action.equals("Set Completed")) {
            txn.status.state = "Completed";
            txn.vehicleReserved.setAvailability(true);
        } else if (action.equals("Set Payment Overdue")) {
            txn.status.state = "Payment Overdue";
        }
       
        TransactionListManager.TransactionFileManager.saveTransactionList(transactions, "transactionList");
    }

    // Dummy transaction list loader/saver
    private ArrayList<Transaction> loadTransactionList() {
        // TODO: Replace with real file I/O
        return new ArrayList<>();
    }
    private void saveTransactionList(ArrayList<Transaction> txns) {
       
    }
    private ArrayList<Vehicle> loadVehicleList() {
        // TODO: Replace with real file I/O
        return new ArrayList<>();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ManageApplicationScreen ui = new ManageApplicationScreen();
            ui.setVisible(true);
        });
    }
}
