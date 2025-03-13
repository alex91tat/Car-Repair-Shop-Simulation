package carRepairShop;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Timer;

public class Service {
    private String name;
    private String description;
    private float cost;
    private List<Employee> employees;
    private List<Part> requiredParts;

    public Service(String name, String description, float cost) {
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.employees = new ArrayList<>();
        this.requiredParts = new ArrayList<>();
    }

    public static Service getServiceByName(String name) {
        String getServiceSql = "SELECT * FROM Services WHERE name = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(getServiceSql)) {

            stmt.setString(1, name);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String serviceName = rs.getString("name");
                String description = rs.getString("description");
                float cost = rs.getFloat("cost");

                Service service = new Service(serviceName, description, cost);
                return service;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean insertPartsToDatabase() {
        String getPartIdSql = "SELECT id FROM Parts WHERE name = ?";
        String getServiceIdSql = "SELECT id FROM Services WHERE name = ?";
        String insertServicePartSql = "INSERT INTO ServiceParts (service_id, part_id) VALUES (?, ?)";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement getServiceIdStmt = connection.prepareStatement(getServiceIdSql)) {

            getServiceIdStmt.setString(1, this.getName());
            ResultSet serviceResultSet = getServiceIdStmt.executeQuery();

            if (serviceResultSet.next()) {
                int serviceId = serviceResultSet.getInt("id");

                for (Part part : requiredParts) {
                    try (PreparedStatement getPartIdStmt = connection.prepareStatement(getPartIdSql)) {
                        getPartIdStmt.setString(1, part.getName());
                        ResultSet partResultSet = getPartIdStmt.executeQuery();

                        if (partResultSet.next()) {
                            int partId = partResultSet.getInt("id");

                            try (PreparedStatement insertServicePartStmt = connection.prepareStatement(insertServicePartSql)) {
                                insertServicePartStmt.setInt(1, serviceId);
                                insertServicePartStmt.setInt(2, partId);
                                insertServicePartStmt.executeUpdate();
                            }
                        } else {
                            System.out.println("Part not found: " + part.getName());
                        }
                    }
                }
                return true;
            } else {
                System.out.println("Service not found for username: " + this.getName());
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Part> getAllParts() {
        List<Part> parts = new ArrayList<>();
        String getServiceIdSql = "SELECT id FROM Services WHERE name = ?";
        String getPartIdsSql = "SELECT part_id FROM ServiceParts WHERE service_id = ?";
        String getPartDetailsSql = "SELECT * FROM Parts WHERE id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement getServiceIdStmt = connection.prepareStatement(getServiceIdSql)) {

            getServiceIdStmt.setString(1, this.getName());
            ResultSet serviceResultSet = getServiceIdStmt.executeQuery();

            if (serviceResultSet.next()) {
                int serviceId = serviceResultSet.getInt("id");

                try (PreparedStatement getPartIdsStmt = connection.prepareStatement(getPartIdsSql)) {
                    getPartIdsStmt.setInt(1, serviceId);
                    ResultSet partIdsResultSet = getPartIdsStmt.executeQuery();

                    while (partIdsResultSet.next()) {
                        int partId = partIdsResultSet.getInt("part_id");

                        try (PreparedStatement getPartDetailsStmt = connection.prepareStatement(getPartDetailsSql)) {
                            getPartDetailsStmt.setInt(1, partId);
                            ResultSet partDetailsResultSet = getPartDetailsStmt.executeQuery();

                            if (partDetailsResultSet.next()) {
                                String partName = partDetailsResultSet.getString("name");
                                int stock = partDetailsResultSet.getInt("stock_quantity");
                                float cost = partDetailsResultSet.getFloat("cost_per_unit");

                                Part part = new Part(partName, stock, cost);
                                parts.add(part);
                            }
                        }
                    }
                    if (!parts.isEmpty()) {
                        this.setRequiredParts(parts);
                    }
                }
            } else {
                System.out.println("Service not found: " + this.getName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.setRequiredParts(parts);
        return parts;
    }

    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String getServiceIdSql = "SELECT id FROM Services WHERE name = ?";
        String getEmployeeIdsSql = "SELECT employee_id FROM ServiceEmployees WHERE service_id = ?";
        String getEmployeeDetailsSql = "SELECT * FROM Employee e INNER JOIN Person p ON e.employee_id = p.id WHERE e.employee_id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement getServiceIdStmt = connection.prepareStatement(getServiceIdSql)) {

            getServiceIdStmt.setString(1, this.getName());
            ResultSet serviceResultSet = getServiceIdStmt.executeQuery();

            if (serviceResultSet.next()) {
                int serviceId = serviceResultSet.getInt("id");

                try (PreparedStatement getEmployeeIdsStmt = connection.prepareStatement(getEmployeeIdsSql)) {
                    getEmployeeIdsStmt.setInt(1, serviceId);
                    ResultSet employeeIdsResultSet = getEmployeeIdsStmt.executeQuery();

                    while (employeeIdsResultSet.next()) {
                        int employeeId = employeeIdsResultSet.getInt("employee_id");

                        try (PreparedStatement getEmployeeDetailsStmt = connection.prepareStatement(getEmployeeDetailsSql)) {
                            getEmployeeDetailsStmt.setInt(1, employeeId);
                            ResultSet employeeDetailsResultSet = getEmployeeDetailsStmt.executeQuery();

                            if (employeeDetailsResultSet.next()) {
                                String name = employeeDetailsResultSet.getString("name");
                                String phoneNumber = employeeDetailsResultSet.getString("phone_number");
                                String address = employeeDetailsResultSet.getString("address");
                                String email = employeeDetailsResultSet.getString("email");
                                String userName = employeeDetailsResultSet.getString("user_name");
                                String password = employeeDetailsResultSet.getString("password");
                                String role = employeeDetailsResultSet.getString("role");
                                float salary = employeeDetailsResultSet.getFloat("salary");
                                Date dateOfBirth = employeeDetailsResultSet.getDate("date_of_birth");
                                Date dateOfEmployment = employeeDetailsResultSet.getDate("date_of_employment");
                                String specialty = employeeDetailsResultSet.getString("speciality");

                                Employee employee = new Employee(name, phoneNumber, address, email, userName, password, role, salary, dateOfBirth, dateOfEmployment, specialty);
                                employees.add(employee);
                            }
                        }
                    }
                }
            } else {
                System.out.println("Service not found: " + this.getName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employees;
    }

    public boolean arePartsAvailable() {
        for (Part part : getAllParts()) {
            if (part.getStockQuantity() <= 0) {
                return false;
            }
        }
        return true;
    }

    public float calculateTotalCostForService() {
        float totalCost = 0;
        for (Part part : getAllParts()) {
            totalCost += part.getCostPerUnit();
        }
        return totalCost + this.cost;

    }

    public void addEmployee(Employee employee) {
        this.employees.add(employee);
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void addRequiredPart(Part part) {
        this.requiredParts.add(part);
    }

    public List<Part> getRequiredParts() {
        return requiredParts;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public float getCost() {
        return cost;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public void setRequiredParts(List<Part> requiredParts) {
        this.requiredParts = requiredParts;
    }
}
