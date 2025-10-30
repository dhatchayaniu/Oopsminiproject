package model;
public class Vehicle {
    private String vehicleId;
    private String brand;
    private String type;
    private double rentPerDay;
    public Vehicle(String vehicleId, String brand, String type, double rentPerDay) {
        this.vehicleId = vehicleId;
        this.brand = brand;
        this.type = type;
        this.rentPerDay = rentPerDay;
    }
    public String getVehicleId() { return vehicleId; }
    public String getBrand() { return brand; }
    public String getType() { return type; }
    public double getRentPerDay() { return rentPerDay; }
}
