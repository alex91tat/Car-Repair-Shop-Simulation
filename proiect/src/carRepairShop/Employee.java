package carRepairShop;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Employee extends Person {
    private float salary;
    private Date dateOfBirth;
    private Date dateOfEmployment;
    private String specialty;
    private List<Service> services;

    public Employee(String name, String phoneNumber, String address, String email, String userName, String password,
                    String role, float salary, Date dateOfBirth, Date dateOfEmployment, String specialty) {
        super(name, phoneNumber, address, email, userName, password, role);
        this.salary = salary;
        this.dateOfBirth = dateOfBirth;
        this.dateOfEmployment = dateOfEmployment;
        this.specialty = specialty;
        services = new ArrayList<>();
    }

    public boolean insertServicesToDatabase() {
        String getServiceIdSql = "SELECT id FROM Services WHERE name = ?";
        String getEmployeeIdSql = "SELECT id FROM Person WHERE user_name = ?";
        String insertServiceEmployeeSql = "INSERT INTO ServiceEmployees (service_id, employee_id) VALUES (?, ?)";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement getEmployeeIdStmt = connection.prepareStatement(getEmployeeIdSql)) {

            getEmployeeIdStmt.setString(1, this.getUserName());
            ResultSet employeeResultSet = getEmployeeIdStmt.executeQuery();

            if (employeeResultSet.next()) {
                int employeeId = employeeResultSet.getInt("id");

                for (Service service : services) {
                    try (PreparedStatement getServiceIdStmt = connection.prepareStatement(getServiceIdSql)) {
                        getServiceIdStmt.setString(1, service.getName());
                        ResultSet serviceResultSet = getServiceIdStmt.executeQuery();

                        if (serviceResultSet.next()) {
                            int serviceId = serviceResultSet.getInt("id");

                            try (PreparedStatement insertServiceEmployeeStmt = connection.prepareStatement(insertServiceEmployeeSql)) {
                                insertServiceEmployeeStmt.setInt(1, serviceId);
                                insertServiceEmployeeStmt.setInt(2, employeeId);
                                insertServiceEmployeeStmt.executeUpdate();
                            }
                        } else {
                            System.out.println("Service not found: " + service.getName());
                        }
                    }
                }
                return true;
            } else {
                System.out.println("Employee not found for username: " + this.getUserName());
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /*public static Employee getEmployeeByUserName(String username) {
        String getPersonSql = "SELECT * FROM Person WHERE user_name = ?";
        String getEmployeeDetailsSql = "SELECT * FROM Employee WHERE user_name = ?";
        String getServicesSql =
                "SELECT s.id, s.name FROM Services s " +
                        "JOIN ServiceEmployees se ON s.id = se.service_id " +
                        "JOIN Person p ON se.employee_id = p.id " +
                        "WHERE p.user_name = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement getPersonStmt = connection.prepareStatement(getPersonSql);
             PreparedStatement getEmployeeDetailsStmt = connection.prepareStatement(getEmployeeDetailsSql);
             PreparedStatement getServicesStmt = connection.prepareStatement(getServicesSql)) {

            getPersonStmt.setString(1, username);
            ResultSet personResultSet = getPersonStmt.executeQuery();

            if (personResultSet.next()) {
                String name = personResultSet.getString("name");
                String phoneNumber = personResultSet.getString("phone_number");
                String address = personResultSet.getString("address");
                String email = personResultSet.getString("email");
                String userName = personResultSet.getString("user_name");
                String password = personResultSet.getString("password");
                String role = personResultSet.getString("role");

                getEmployeeDetailsStmt.setString(1, username);
                ResultSet employeeResultSet = getEmployeeDetailsStmt.executeQuery();

                if (employeeResultSet.next()) {
                    float salary = employeeResultSet.getFloat("salary");
                    Date dateOfBirth = employeeResultSet.getDate("date_of_birth");
                    Date dateOfEmployment = employeeResultSet.getDate("date_of_employment");
                    String specialty = employeeResultSet.getString("specialty");

                    Employee employee = new Employee(name, phoneNumber, address, email, userName, password, role,
                            salary, dateOfBirth, dateOfEmployment, specialty);

                    return employee;
                }
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }*/

    public static Employee getEmployeeByUserName(String username) {
        String getPersonSql = "SELECT * FROM Person WHERE user_name = ?";
        String getEmployeeDetailsSql = "SELECT * FROM Employee WHERE employee_id = ?";

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
                    float salary = employeeResultSet.getFloat("salary");
                    Date dateOfBirth = employeeResultSet.getDate("date_of_birth");
                    Date dateOfEmployment = employeeResultSet.getDate("date_of_employment");
                    String specialty = employeeResultSet.getString("speciality");

                    Employee employee = new Employee(name, phoneNumber, address, email, userName, password, role,
                            salary, dateOfBirth, dateOfEmployment, specialty);

                    return employee;
                }
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getEmployeeIdByName(String name) {
        String sql = "SELECT id FROM Person WHERE name = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("id");
            } else {
                System.out.println("Employee not found for name: " + name);
                return -1;
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving employee ID by name.");
            e.printStackTrace();
            return -1;
        }
    }

    public List<Service> getAllServicesForEmployee(int employeeId) {
        List<Service> services = new ArrayList<>();
        String getServiceIdsSql = "SELECT service_id FROM ServiceEmployees WHERE employee_id = ?";
        String getServiceDetailsSql = "SELECT * FROM Services WHERE id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement getServiceIdsStmt = connection.prepareStatement(getServiceIdsSql)) {

            getServiceIdsStmt.setInt(1, employeeId);
            ResultSet serviceIdsResultSet = getServiceIdsStmt.executeQuery();

            while (serviceIdsResultSet.next()) {
                int serviceId = serviceIdsResultSet.getInt("service_id");

                try (PreparedStatement getServiceDetailsStmt = connection.prepareStatement(getServiceDetailsSql)) {
                    getServiceDetailsStmt.setInt(1, serviceId);
                    ResultSet serviceDetailsResultSet = getServiceDetailsStmt.executeQuery();

                    if (serviceDetailsResultSet.next()) {
                        String name = serviceDetailsResultSet.getString("name");
                        String description = serviceDetailsResultSet.getString("description");
                        float cost = serviceDetailsResultSet.getFloat("cost");

                        Service service = new Service(name, description, cost);
                        services.add(service);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return services;
    }

    public boolean updatePartsStockForService(String serviceName, int stockQuantity, String partName) {
        int employeeId = this.getEmployeeIdByName(this.getName());
        if (employeeId == -1) {
            return false;
        }

        List<Service> services = this.getAllServicesForEmployee(employeeId);
        for (Service service : services) {
            if (service.getName().equals(serviceName)) {
                List<Part> parts = service.getAllParts();
                for (Part part : parts) {
                    if (part.getName().equals(partName)) {
                        boolean ok = part.updateStockQuantityInDB(stockQuantity);
                        if (ok) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
            }
        }

        return false;
    }

    public float getSalary() {
        return salary;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public Date getDateOfEmployment() {
        return dateOfEmployment;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void addService(Service service) {
        services.add(service);
    }

    public List<Service> getServices() {
        return services;
    }
}
