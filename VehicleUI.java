package client;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
public class VehicleUI extends JFrame{
    private VehicleClient client;
    public VehicleUI(){
        try {
            client = new VehicleClient("localhost", 5000);
        } catch (Exception e){
            JOptionPane.showMessageDialog(this, "❌ Could not connect to server: " + e.getMessage());
            System.exit(0);
        }
        setTitle("🚗 Vehicle Rental Booking System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Add Vehicle", createAddVehiclePanel());
        tabs.addTab("Register Customer", createRegisterCustomerPanel());
        tabs.addTab("Book Vehicle", createBookVehiclePanel());
        tabs.addTab("View Bookings", createViewBookingsPanel());
        add(tabs, BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel createAddVehiclePanel() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));

        JTextField id = new JTextField();
        JTextField brand = new JTextField();
        JTextField type = new JTextField();
        JTextField rent = new JTextField();
        JButton addBtn = new JButton("Add Vehicle");

        panel.add(new JLabel("Vehicle ID:"));
        panel.add(id);
        panel.add(new JLabel("Brand:"));
        panel.add(brand);
        panel.add(new JLabel("Type:"));
        panel.add(type);
        panel.add(new JLabel("Rent per Day:"));
        panel.add(rent);
        panel.add(new JLabel(""));
        panel.add(addBtn);

        addBtn.addActionListener(e -> {
            String msg = "ADD_VEHICLE:" + id.getText() + ":" + brand.getText() + ":" + type.getText() + ":" + rent.getText();
            JOptionPane.showMessageDialog(this, client.sendRequest(msg));
        });

        return panel;
    }

    private JPanel createRegisterCustomerPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));

        JTextField id = new JTextField();
        JTextField name = new JTextField();
        JTextField contact = new JTextField();
        JButton registerBtn = new JButton("Register Customer");
        panel.add(new JLabel("Customer ID:"));
        panel.add(id);
        panel.add(new JLabel("Name:"));
        panel.add(name);
        panel.add(new JLabel("Contact:"));
        panel.add(contact);
        panel.add(new JLabel(""));
        panel.add(registerBtn);
        registerBtn.addActionListener(e -> {
            String msg = "REGISTER_CUSTOMER:" + id.getText() + ":" + name.getText() + ":" + contact.getText();
            JOptionPane.showMessageDialog(this, client.sendRequest(msg));
        });
        return panel;
    }
    private JPanel createBookVehiclePanel() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        JTextField bookingId = new JTextField();
        JTextField vehicleId = new JTextField();
        JTextField customerId = new JTextField();
        JTextField days = new JTextField();
        JButton bookBtn = new JButton("Book Vehicle");
        panel.add(new JLabel("Booking ID:"));
        panel.add(bookingId);
        panel.add(new JLabel("Vehicle ID:"));
        panel.add(vehicleId);
        panel.add(new JLabel("Customer ID:"));
        panel.add(customerId);
        panel.add(new JLabel("No. of Days:"));
        panel.add(days);
        panel.add(new JLabel(""));
        panel.add(bookBtn);

        bookBtn.addActionListener(e -> {
        	String msg = "BOOK_VEHICLE:" + bookingId.getText() + ":" + vehicleId.getText() + ":" + customerId.getText() + ":" + days.getText();
        	JOptionPane.showMessageDialog(this, client.sendRequest(msg));
        });
        return panel;
    }
    private JPanel createViewBookingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea outputArea = new JTextArea();
        outputArea.setEditable(false);
        JButton viewBtn = new JButton("View All Bookings");

        panel.add(new JScrollPane(outputArea), BorderLayout.CENTER);
        panel.add(viewBtn, BorderLayout.SOUTH);

        viewBtn.addActionListener(e -> {
            String response = client.sendRequest("VIEW_BOOKINGS");
            outputArea.setText(response);
        });

        return panel;
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(VehicleUI::new);
    }
}

class VehicleClient{
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public VehicleClient(String host, int port) throws IOException {
        socket = new Socket(host, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println("Connected to Vehicle Rental Server ✅");
    }

    public String sendRequest(String msg) {
        try {
            out.println(msg);
            return in.readLine();
        } catch (IOException e) {
            return "❌ Error communicating with server: " + e.getMessage();
        }
    }
}
