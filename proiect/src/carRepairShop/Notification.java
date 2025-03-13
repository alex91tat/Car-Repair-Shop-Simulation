package carRepairShop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalTime;

public class Notification {
    private int ownerId;
    private int carId;
    private String message;
    private boolean isSent;
    private LocalTime dateTime;

    public Notification(int ownerId, int carId, String message) {
        this.ownerId = ownerId;
        this.carId = carId;
        this.message = message;
        this.isSent = false;
        this.dateTime = LocalTime.now();
    }

    public boolean updateIsSentInDatabase(boolean sent) {
        String sql = "UPDATE Notifications SET is_sent = ? WHERE owner_id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setBoolean(1, sent);
            statement.setInt(2, this.ownerId);

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("Error: Unable to update is_sent column from the Notifications table in the database.");
            e.printStackTrace();
            return false;
        }
    }

    public void setIsSent(boolean isSent) {
        this.isSent = isSent;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public int getCarId() {
        return carId;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSent() {
        return isSent;
    }

    public LocalTime getTime() {
        return dateTime;
    }
}
