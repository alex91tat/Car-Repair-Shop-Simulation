package carRepairShop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/carrepairshop";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "postgres";

    public static Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            //System.out.println("Database connection established");
            return con;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return null;
        }
    }

    public static boolean doesUsernameExist(String username) {
        String query = "SELECT COUNT(*) FROM Person WHERE user_name = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            resultSet.next();
            return resultSet.getInt(1) > 0;
        } catch (SQLException e) {
            System.out.println("Error: Unable to check username availability.");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean doesCarExist(String licensePlate) {
        String query = "SELECT COUNT(*) FROM Cars WHERE license_plate = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, licensePlate);
            ResultSet resultSet = statement.executeQuery();

            resultSet.next();
            return resultSet.getInt(1) > 0;
        } catch (SQLException e) {
            System.out.println("Error: Unable to check license plate availability.");
            e.printStackTrace();
            return false;
        }
    }

    public static List<Service> getAllServicesFromDB() {
        List<Service> services = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Services")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                services.add(new Service(rs.getString("name"), rs.getString("description"),
                        rs.getFloat("cost")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return services;
    }

    public static int getNumberOfCarsNotPickedUp() {
        String query = "SELECT COUNT(*) FROM Cars WHERE status != 'COMPLETED'";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error: Unable to get the number of cars not picked up.");
            e.printStackTrace();
        }
        return 0;
    }

    public void closeConnection(Connection con) {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
            System.exit(0);
        }
    }

    public static boolean insertPerson(Person person) {
        String sql = "INSERT INTO Person (name, phone_number, address, email, user_name, password, role) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, person.getName());
            statement.setString(2, person.getPhoneNumber());
            statement.setString(3, person.getAddress());
            statement.setString(4, person.getEmail());
            statement.setString(5, person.getUserName());
            statement.setString(6, person.getPasswordHash());
            statement.setString(7, person.getRole());

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.out.println("Error: Unable to insert person into the database.");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean insertCar(Car car) {
        String sql = "INSERT INTO Cars (license_plate, brand, color, horsepower, mileage, owner_id, status)"
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, car.getLicensePlate());
            statement.setString(2, car.getBrand());
            statement.setString(3, car.getColor());
            statement.setInt(4, car.getHorsepower());
            statement.setInt(5, car.getMileage());
            statement.setInt(6, car.getOwnerId());
            statement.setString(7, car.getStatus().toString());

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.out.println("Error: Unable to insert car into the database.");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean insertPayment(Payment payment) {
        String sql = "INSERT INTO Payments (car_id, amount, payment_date)" + "VALUES (?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, payment.getCarId());
            statement.setFloat(2, payment.getAmount());
            statement.setDate(3, payment.getPaymentDate());

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.out.println("Error: Unable to insert payment into the database.");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean insertOwner(Owner owner) {
        String getId = "SELECT id FROM Person WHERE user_name = ?";
        String sql = "INSERT INTO Owner (owner_id, nr_of_cars, amount_of_money) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement getOwnerId = connection.prepareStatement(getId);
             PreparedStatement insertOwner = connection.prepareStatement(sql)) {


            getOwnerId.setString(1, owner.getUserName());
            ResultSet set = getOwnerId.executeQuery();

            if (set.next()) {
                int id = set.getInt("id");

                insertOwner.setInt(1, id);
                insertOwner.setInt(2, owner.getNrOfCars());
                insertOwner.setFloat(3, owner.getAmountOfMoney());

                int rowsInserted = insertOwner.executeUpdate();
                return rowsInserted > 0;
            } else {
                System.out.println("Username not found.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean insertEmployee(Employee employee) {
        String getId = "SELECT id FROM Person WHERE user_name = ?";
        String sql = "INSERT INTO Employee (employee_id, salary, date_of_birth, date_of_employment, speciality) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement getEmployeeId = connection.prepareStatement(getId);
             PreparedStatement insertEmployee = connection.prepareStatement(sql)) {


            getEmployeeId.setString(1, employee.getUserName());
            ResultSet set = getEmployeeId.executeQuery();

            if (set.next()) {
                int id = set.getInt("id");

                insertEmployee.setInt(1, id);
                insertEmployee.setFloat(2, employee.getSalary());
                insertEmployee.setDate(3, employee.getDateOfBirth());
                insertEmployee.setDate(4, employee.getDateOfEmployment());
                insertEmployee.setString(5, employee.getSpecialty());

                int rowsInserted = insertEmployee.executeUpdate();
                return rowsInserted > 0;
            } else {
                System.out.println("Username not found.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean insertService(Service service) {
        String sql = "INSERT INTO Services (name, description, cost)" + "VALUES (?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, service.getName());
            statement.setString(2, service.getDescription());
            statement.setFloat(3, service.getCost());

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.out.println("Error: Unable to insert service into the database.");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean insertPart(Part part) {
        String sql = "INSERT INTO Parts (name, stock_quantity, cost_per_unit)" + "VALUES (?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, part.getName());
            statement.setInt(2, part.getStockQuantity());
            statement.setFloat(3, part.getCostPerUnit());

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.out.println("Error: Unable to insert part into the database.");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean insertLog(Log log) {
        String sql = "INSERT INTO Logs (person_id) VALUES (?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, log.getPersonId());

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.out.println("Error: Unable to insert log into the database.");
            e.printStackTrace();
            return false;
        }
    }

    public static    boolean insertNotification(Notification notification) {
        String sql = "INSERT INTO Notifications (owner_id, car_id, message, is_sent) VALUES (?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, notification.getOwnerId());
            statement.setInt(2, notification.getCarId());
            statement.setString(3, notification.getMessage());
            statement.setBoolean(4, notification.isSent());

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.out.println("Error: Unable to insert notification into the database.");
            e.printStackTrace();
            return false;
        }
    }

}
