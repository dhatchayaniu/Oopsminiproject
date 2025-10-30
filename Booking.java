package model;
public class Booking {
    private int bookingId;
    private String customerId;
    private String vehicleId;
    private int days;
    private double totalAmount;
    public Booking(String customerId, String vehicleId, int days, double totalAmount) {
        this.customerId = customerId;
        this.vehicleId = vehicleId;
        this.days = days;
        this.totalAmount = totalAmount;
    }
    public int getBookingId() { return bookingId; }
    public String getCustomerId() { return customerId; }
    public String getVehicleId() { return vehicleId; }
    public int getDays() { return days; }
    public double getTotalAmount() { return totalAmount; }
}
