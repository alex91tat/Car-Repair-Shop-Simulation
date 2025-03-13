package carRepairShop;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Car {
    private String licensePlate;
    private String brand;
    private String color;
    private int horsepower;
    private int mileage;
    private int ownerId;
    private List<Service> services;
    private CarState status;

    public Car(String licensePlate, String brand, String color, int horsepower, int mileage, int ownerId) {
        this.licensePlate = licensePlate;
        this.brand = brand;
        this.color = color;
        this.horsepower = horsepower;
        this.mileage = mileage;
        this.ownerId = ownerId;
        this.services = new ArrayList<>();
        this.status = CarState.REGISTERED;
    }

    public boolean insertServicesToDatabase() {
        String getServiceIdSql = "SELECT id FROM Services WHERE name = ?";
        String getCarPlateSql = "SELECT id FROM Cars WHERE license_plate = ?";
        String insertCarServiceSql = "INSERT INTO CarServices (car_id, service_id) VALUES (?, ?)";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement getCarPlateStmt = connection.prepareStatement(getCarPlateSql)) {

            getCarPlateStmt.setString(1, this.getLicensePlate());
            ResultSet carResultSet = getCarPlateStmt.executeQuery();

            if (carResultSet.next()) {
                int carId = carResultSet.getInt("id");

                for (Service service : this.services) {
                    try (PreparedStatement getServiceIdStmt = connection.prepareStatement(getServiceIdSql)) {
                        getServiceIdStmt.setString(1, service.getName());
                        ResultSet serviceResultSet = getServiceIdStmt.executeQuery();

                        if (serviceResultSet.next()) {
                            int serviceId = serviceResultSet.getInt("id");

                            try (PreparedStatement insertCarServiceStmt = connection.prepareStatement(insertCarServiceSql)) {
                                insertCarServiceStmt.setInt(1, carId);
                                insertCarServiceStmt.setInt(2, serviceId);
                                insertCarServiceStmt.executeUpdate();
                            }
                        } else {
                            System.out.println("Service not found: " + service.getName());
                        }
                    }
                }
                return true;
            } else {
                System.out.println("Car not found for license plate: " + this.getLicensePlate());
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Car getCarByOwnerIdAndStatus(int ownerId) {
        String query = "SELECT * FROM Cars WHERE owner_id = ? AND status != 'COMPLETED'";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, ownerId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String licensePlate = resultSet.getString("license_plate");
                String brand = resultSet.getString("brand");
                String color = resultSet.getString("color");
                int horsepower = resultSet.getInt("horsepower");
                int mileage = resultSet.getInt("mileage");
                String statusString = resultSet.getString("status");

                CarState status = CarState.valueOf(statusString);

                Car car = new Car(licensePlate, brand, color, horsepower, mileage, ownerId);
                car.setStatus(status);
                return car;
            } else {
                System.out.println("No car found for owner with ID: " + ownerId);
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error: Unable to retrieve car for owner with ID: " + ownerId);
            e.printStackTrace();
            return null;
        }
    }

    public static int getCarIdByLicensePlate(String licensePlate) {
        String query = "SELECT id FROM Cars WHERE license_plate = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, licensePlate);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("id");
            } else {
                System.out.println("No car found with license plate: " + licensePlate);
                return -1;
            }
        } catch (SQLException e) {
            System.out.println("Error: Unable to retrieve car ID for license plate: " + licensePlate);
            e.printStackTrace();
            return -1;
        }
    }

    public static int getNumberOfCarsNotPickedUpForOwner(int ownerId) {
        String query = "SELECT COUNT(*) FROM Cars WHERE owner_id = ? AND status != 'COMPLETED'";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, ownerId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error: Unable to get the number of cars not picked up for the owner.");
            e.printStackTrace();
        }
        return -1;
    }

    public void getAllServices() {
        List<Service> services = new ArrayList<>();
        String getCarIdSql = "SELECT id FROM Cars WHERE license_plate = ?";
        String getServiceIdsSql = "SELECT service_id FROM CarServices WHERE car_id = ?";
        String getServiceDetailsSql = "SELECT * FROM Services WHERE id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement getCarIdStmt = connection.prepareStatement(getCarIdSql)) {

            getCarIdStmt.setString(1, this.getLicensePlate());
            ResultSet carResultSet = getCarIdStmt.executeQuery();

            if (carResultSet.next()) {
                int carId = carResultSet.getInt("id");

                try (PreparedStatement getServiceIdsStmt = connection.prepareStatement(getServiceIdsSql)) {
                    getServiceIdsStmt.setInt(1, carId);
                    ResultSet serviceIdsResultSet = getServiceIdsStmt.executeQuery();

                    while (serviceIdsResultSet.next()) {
                        int serviceId = serviceIdsResultSet.getInt("service_id");

                        try (PreparedStatement getServiceDetailsStmt = connection.prepareStatement(getServiceDetailsSql)) {
                            getServiceDetailsStmt.setInt(1, serviceId);
                            ResultSet serviceDetailsResultSet = getServiceDetailsStmt.executeQuery();

                            if (serviceDetailsResultSet.next()) {
                                String serviceName = serviceDetailsResultSet.getString("name");
                                String description = serviceDetailsResultSet.getString("description");
                                float cost = serviceDetailsResultSet.getFloat("cost");

                                Service service = new Service(serviceName, description, cost);
                                services.add(service);
                            }
                        }
                    }
                }
            } else {
                System.out.println("Car not found: " + this.getLicensePlate());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.services = services;
    }

    public boolean updateCarStatus(int ownerId, CarState newStatus) {
        this.status = newStatus;
        String sql = "UPDATE Cars SET status = ? WHERE owner_id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, newStatus.toString());
            statement.setInt(2, ownerId);

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("Error: Unable to update the car status of the owner in the database.");
            e.printStackTrace();
            return false;
        }
    }

    public void addService(Service service) {
        this.services.add(service);
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getBrand() {
        return brand;
    }

    public String getColor() {
        return color;
    }

    public int getHorsepower() {
        return horsepower;
    }

    public int getMileage() {
        return mileage;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public List<Service> getServices() {
        return services;
    }

    public CarState getStatus() {
        return status;
    }

    public void setStatus(CarState status) {
        this.status = status;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

}
