import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
public class AdminScreen extends JFrame {
    private JFrame frame;
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;
    private ArrayList<User> userList;
    private ArrayList<Vehicle> vehicleList;

    public AdminScreen () {
        initUI();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminScreen());
    }

    private void initUI() {
        frame = new JFrame("Admin Screen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 700);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));

        JButton manageClientsBtn = new JButton("Manage Clients");
        manageClientsBtn.addActionListener(e -> {
            ManageClients manageClientsFrame = new ManageClients();
            manageClientsFrame.setVisible(true);
        });

        JButton manageVehiclesBtn = new JButton("Manage Vehicles");
        manageVehiclesBtn.addActionListener(e -> {
            ManageVehicles manageVehiclesFrame = new ManageVehicles();
            manageVehiclesFrame.setVisible(true);
        });

        JButton manageApplicationsBtn = new JButton("Manage Applications");
        manageApplicationsBtn.addActionListener(e -> {
            ManageApplicationScreen manageApplicationsFrame = new ManageApplicationScreen();
            manageApplicationsFrame.setVisible(true);
        });

        buttonPanel.add(manageClientsBtn);
        buttonPanel.add(manageVehiclesBtn);
        buttonPanel.add(manageApplicationsBtn);

        panel.add(buttonPanel, BorderLayout.NORTH);

        frame.setContentPane(panel);
        frame.setVisible(true);
    }
}