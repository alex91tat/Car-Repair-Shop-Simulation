package carRepairShop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Person {
    protected String name;
    protected String phoneNumber;
    protected String address;
    protected String email;
    protected String userName;
    protected String passwordHash;
    protected String role;

    public Person(String name, String phoneNumber, String address, String email, String userName, String passwordHash, String role) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.email = email;
        this.userName = userName;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public static Person getPersonById(int id) {
        String sql = "SELECT * FROM Person WHERE id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String phoneNumber = resultSet.getString("phone_number");
                String address = resultSet.getString("address");
                String email = resultSet.getString("email");
                String userName = resultSet.getString("user_name");
                String password = resultSet.getString("password");
                String role = resultSet.getString("role");

                return new Person(name, phoneNumber, address, email, userName, password, role);
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error: Unable to retrieve person from the database.");
            e.printStackTrace();
            return null;
        }
    }

    public static Person getPersonByUserName(String username) {
        String sql = "SELECT * FROM Person WHERE user_name = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String phoneNumber = resultSet.getString("phone_number");
                String address = resultSet.getString("address");
                String email = resultSet.getString("email");
                String userName = resultSet.getString("user_name");
                String password = resultSet.getString("password");
                String role = resultSet.getString("role");

                return new Person(name, phoneNumber, address, email, userName, password, role);
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error: Unable to retrieve person from the database.");
            e.printStackTrace();
            return null;
        }
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getUserName() {
        return userName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getRole() {
        return role;
    }
}
