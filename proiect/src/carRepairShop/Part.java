package carRepairShop;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class Part {
    private String name;
    private int stockQuantity;
    private float costPerUnit;
    private List<Service> services;

    public Part(String name, int stockQuantity, float costPerUnit) {
        this.name = name;
        this.stockQuantity = stockQuantity;
        this.costPerUnit = costPerUnit;
        this.services = new ArrayList<>();
    }

    public List<Service> getAllServices() {
        List<Service> services = new ArrayList<>();
        String getPartIdSql = "SELECT id FROM Parts WHERE name = ?";
        String getServiceIdsSql = "SELECT service_id FROM ServiceParts WHERE part_id = ?";
        String getServiceDetailsSql = "SELECT * FROM Services WHERE id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement getPartIdStmt = connection.prepareStatement(getPartIdSql)) {

            getPartIdStmt.setString(1, this.getName());
            ResultSet partResultSet = getPartIdStmt.executeQuery();

            if (partResultSet.next()) {
                int partId = partResultSet.getInt("id");

                try (PreparedStatement getServiceIdsStmt = connection.prepareStatement(getServiceIdsSql)) {
                    getServiceIdsStmt.setInt(1, partId);
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
                System.out.println("Part not found: " + this.getName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return services;
    }

    public boolean updateStockQuantityInDB(int newStockQuantity) {
        String sql = "UPDATE Parts SET stock_quantity = ? WHERE name = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, newStockQuantity);
            statement.setString(2, this.name);

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("Error: Unable to update the stock quantity for that part.");
            e.printStackTrace();
            return false;
        }
    }

    public void addService(Service service) {
        this.services.add(service);
    }

    public List<Service> getServices() {
        return services;
    }

    public String getName() {
        return name;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public float getCostPerUnit() {
        return costPerUnit;
    }

    public void setStockQuantity(int newStock) {
        this.stockQuantity = newStock;
    }
}
