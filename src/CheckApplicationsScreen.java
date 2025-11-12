import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CheckApplicationsScreen extends JFrame {
    private User currentClient;
    private JTable table;
    private DefaultTableModel tableModel;

    public CheckApplicationsScreen(User client) {
        super("My Applications");
        this.currentClient = client;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 350);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        

        String[] cols = {"Transaction ID", "Vehicle", "Start Date", "End Date", "Price", "Status"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        refreshTable();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        ArrayList<Transaction> transactions = TransactionListManager.getList();
        for (Transaction txn : transactions) {
                if (txn.user != null && currentClient != null && txn.user.userName != null && txn.user.userName.equals(currentClient.userName)) {
                tableModel.addRow(new Object[]{
                    txn.getTransactionID(),
                    txn.getVehicleType(),
                    txn.getDateStart(),
                    txn.getDateEnd(),
                    txn.getPrice(),
                    txn.getStatus()
                });
            }
        }
    }
}
