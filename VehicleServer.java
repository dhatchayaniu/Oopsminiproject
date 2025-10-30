package server;
import java.io.*;
import java.net.*;
import java.sql.*;
import database.DBConnection;
public class VehicleServer {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private Connection conn;
    public VehicleServer() {
        try {
            serverSocket = new ServerSocket(5000);
            System.out.println("🚗 Vehicle Rental Server started on port 5000...");
            conn = DBConnection.getConnection();
            while (true) {
                clientSocket = serverSocket.accept();
                System.out.println("✅ Client connected: " + clientSocket.getInetAddress());
                new ClientHandler(clientSocket, conn).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        new VehicleServer();
    }
}
class ClientHandler extends Thread {
    private Socket socket;
    private Connection conn;
    private BufferedReader in;
    private PrintWriter out;
    public ClientHandler(Socket socket, Connection conn) {
        this.socket = socket;
        this.conn = conn;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void run() {
        try {
            String request;
            while ((request = in.readLine()) != null) {
                String[] parts = request.split(":");
                String command = parts[0].trim().toUpperCase();
                switch (command) {
                    case "ADD_VEHICLE":
                        addVehicle(parts);
                        break;
                    case "REGISTER_CUSTOMER":
                        registerCustomer(parts);
                        break;
                    case "BOOK_VEHICLE":
                        bookVehicle(parts);
                        break;
                    case "VIEW_BOOKINGS":
                        viewBookings();
                        break;
                    default:
                        out.println("❌ Unknown Command");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void addVehicle(String[] parts) {
        try {
            String id = parts[1];
            String brand = parts[2];
            String type = parts[3];
            double rent = Double.parseDouble(parts[4]);
            PreparedStatement ps = conn.prepareStatement("INSERT INTO vehicles VALUES (?, ?, ?, ?)");
            ps.setString(1, id);
            ps.setString(2, brand);
            ps.setString(3, type);
            ps.setDouble(4, rent);
            ps.executeUpdate();
            out.println("✅ Vehicle added successfully!");
        } catch (Exception e) {
            out.println("❌ Error adding vehicle: " + e.getMessage());
        }
    }
    private void registerCustomer(String[] parts) {
        try {
            String id = parts[1];
            String name = parts[2];
            String contact = parts[3];
            PreparedStatement ps = conn.prepareStatement("INSERT INTO customers VALUES (?, ?, ?)");
            ps.setString(1, id);
            ps.setString(2, name);
            ps.setString(3, contact);
            ps.executeUpdate();
            out.println("✅ Customer registered successfully!");
        } catch (Exception e) {
            out.println("❌ Error registering customer: " + e.getMessage());
        }
    }
    private void bookVehicle(String[] parts) {
        try {
            String bookingId = parts[1];
            String vehicleId = parts[2];
            String customerId = parts[3];
            int days = Integer.parseInt(parts[4]);
            PreparedStatement ps1 = conn.prepareStatement("SELECT rent_per_day FROM vehicles WHERE vehicle_id = ?");
            ps1.setString(1, vehicleId);
            ResultSet rs = ps1.executeQuery();
            if (!rs.next()) {
                out.println("❌ Vehicle not found!");
                return;
            }
            double rent = rs.getDouble("rent_per_day");
            double total = rent * days;
            PreparedStatement ps2 = conn.prepareStatement("INSERT INTO bookings VALUES (?, ?, ?, ?, ?)");
            ps2.setString(1, bookingId);
            ps2.setString(2, customerId);
            ps2.setString(3, vehicleId);
            ps2.setInt(4, days);
            ps2.setDouble(5, total);
            ps2.executeUpdate();
            out.println("✅ Booking successful! Total: ₹" + total);
        } catch (Exception e) {
            out.println("❌ Error: " + e.getMessage());
        }
    }
    private void viewBookings() {
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM bookings");
            StringBuilder sb = new StringBuilder("📘 Bookings List:\n");
            while (rs.next()) {
                sb.append("Booking ID: ").append(rs.getString(1))
                  .append(", Vehicle ID: ").append(rs.getString(2))
                  .append(", Customer ID: ").append(rs.getString(3))
                  .append(", Days: ").append(rs.getInt(4))
                  .append(", Total Rent: ₹").append(rs.getDouble(5))
                  .append("\n");
            }
            out.println(sb.toString());
        } catch (Exception e) {
            out.println("❌ Error viewing bookings: " + e.getMessage());
        }
    }
}
