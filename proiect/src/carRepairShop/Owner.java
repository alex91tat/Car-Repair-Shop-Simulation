package carRepairShop;

import java.sql.*;

public class Owner extends Person {
    private int nrOfCars;
    private float amountOfMoney;

    public Owner(String name, String phoneNumber, String address, String email, String username, String password, String role, int nrOfCars, float amountOfMoney) {
        super(name, phoneNumber, address, email, username, password, role);
        this.nrOfCars = nrOfCars;
        this.amountOfMoney = amountOfMoney;
    }

    public static Owner getOwnerById(int ownerId) {
        String sql = "SELECT * FROM Owner WHERE owner_id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, ownerId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int nrOfCars = resultSet.getInt("nr_of_cars");
                float amountOfMoney = resultSet.getFloat("amount_of_money");

                Person person = Person.getPersonById(ownerId);
                return new Owner(person.getName(), person.getPhoneNumber(), person.getAddress(), person.getEmail(),
                        person.getUserName(), person.getPasswordHash(), person.getRole(), nrOfCars, amountOfMoney);
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error: Unable to retrieve owner from the database.");
            e.printStackTrace();
            return null;
        }
    }

    public static Owner getOwnerByUserName(String username) {
        String getPersonSql = "SELECT * FROM Person WHERE user_name = ?";
        String getEmployeeDetailsSql = "SELECT * FROM Owner WHERE owner_id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement getPersonStmt = connection.prepareStatement(getPersonSql);
             PreparedStatement getEmployeeDetailsStmt = connection.prepareStatement(getEmployeeDetailsSql)) {

            getPersonStmt.setString(1, username);
            ResultSet personResultSet = getPersonStmt.executeQuery();

            if (personResultSet.next()) {
                int personId = personResultSet.getInt("id");
                String name = personResultSet.getString("name");
                String phoneNumber = personResultSet.getString("phone_number");
                String address = personResultSet.getString("address");
                String email = personResultSet.getString("email");
                String userName = personResultSet.getString("user_name");
                String password = personResultSet.getString("password");
                String role = personResultSet.getString("role");

                getEmployeeDetailsStmt.setInt(1, personId);
                ResultSet employeeResultSet = getEmployeeDetailsStmt.executeQuery();

                if (employeeResultSet.next()) {
                    int nrOfCars = employeeResultSet.getInt("nr_of_cars");
                    float amountOfMoney = employeeResultSet.getFloat("amount_of_money");

                    Owner owner = new Owner(name, phoneNumber, address, email, userName, password, role,
                            nrOfCars, amountOfMoney);

                    return owner;
                }
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getOwnerIdByName(String name) {
        String sql = "SELECT id FROM Person WHERE name = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("id");
            } else {
                System.out.println("Owner not found for name: " + name);
                return -1;
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving owner ID by name.");
            e.printStackTrace();
            return -1;
        }
    }

    public boolean updateAmountOfMoneyInDB(int id, float newAmount) {
        String sql = "UPDATE Owner SET amount_of_money = ? WHERE owner_id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setFloat(1, newAmount);
            statement.setInt(2, id);

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("Error: Unable to update amount of money of the owner in the database.");
            e.printStackTrace();
            return false;
        }
    }

    public int getNrOfCars() {
        return nrOfCars;
    }

    public float getAmountOfMoney() {
        return amountOfMoney;
    }

    public void setAmountOfMoney(float amountOfMoney) {
        this.amountOfMoney = amountOfMoney;
    }

}
