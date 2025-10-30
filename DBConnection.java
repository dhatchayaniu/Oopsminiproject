package database;
import java.sql.*;
public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/vehiclerental";
    private static final String USER = "root"; // change if needed
    private static final String PASSWORD = "123456"; // your MySQL password
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    public static boolean addVehicle(String id, String brand, String type, double rent) {
        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO vehicles (vehicle_id, brand, type, rent_per_day) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ps.setString(2, brand);
            ps.setString(3, type);
            ps.setDouble(4, rent);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean addCustomer(String id, String name, String contact) {
        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO customers (customer_id, name, contact) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ps.setString(2, name);
            ps.setString(3, contact);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean addBooking(String customerId, String vehicleId, int days) {
        try (Connection conn = getConnection()) {
            String sql1 = "SELECT rent_per_day FROM vehicles WHERE vehicle_id=?";
            PreparedStatement ps1 = conn.prepareStatement(sql1);
            ps1.setString(1, vehicleId);
            ResultSet rs = ps1.executeQuery();
            if (!rs.next()) return false;
            double rentPerDay = rs.getDouble("rent_per_day");
            double total = rentPerDay * days;
            String sql2 = "INSERT INTO bookings (customer_id, vehicle_id, days, total_amount) VALUES (?, ?, ?, ?)";
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            ps2.setString(1, customerId);
            ps2.setString(2, vehicleId);
            ps2.setInt(3, days);
            ps2.setDouble(4, total);
            ps2.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static String viewBookings() {
        StringBuilder result = new StringBuilder();
        try (Connection conn = getConnection()) {
            String sql = "SELECT b.booking_id, c.name, v.brand, b.days, b.total_amount " +
                         "FROM bookings b JOIN customers c ON b.customer_id = c.customer_id " +
                         "JOIN vehicles v ON b.vehicle_id = v.vehicle_id";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                result.append("Booking ID: ").append(rs.getInt("booking_id"))
                      .append(", Customer: ").append(rs.getString("name"))
                      .append(", Vehicle: ").append(rs.getString("brand"))
                      .append(", Days: ").append(rs.getInt("days"))
                      .append(", Total: ").append(rs.getDouble("total_amount"))
                      .append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}
