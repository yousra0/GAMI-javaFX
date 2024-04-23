package Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class order_s {

    private Connection cnx;

    public order_s(Connection cnx) {
        this.cnx = cnx;
    }

    // Method to add an order
    public void addOrder(int orderId) throws SQLException {
        String query = "INSERT INTO `order`(`id`) VALUES (?)";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setInt(1, orderId);
            statement.executeUpdate();
        }
    }

    // Method to retrieve all orders
    public List<Integer> getAllOrders() throws SQLException {
        List<Integer> orderList = new ArrayList<>();
        String query = "SELECT `id` FROM `order`";
        try (PreparedStatement statement = cnx.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                orderList.add(resultSet.getInt("id"));
            }
        }
        return orderList;
    }

    // Method to delete an order
    // Method to delete an order
    public void deleteOrder(int orderId) throws SQLException {
        try {
            cnx.setAutoCommit(false); // Set auto-commit to false

            String query = "DELETE FROM `order` WHERE `id`=?";
            try (PreparedStatement statement = cnx.prepareStatement(query)) {
                statement.setInt(1, orderId);
                statement.executeUpdate();
            }

            // Delete entries from order_product table
            String deleteOrderProductQuery = "DELETE FROM order_product WHERE order_id=?";
            try (PreparedStatement statement = cnx.prepareStatement(deleteOrderProductQuery)) {
                statement.setInt(1, orderId);
                statement.executeUpdate();
            }

            cnx.commit(); // Commit changes
            cnx.setAutoCommit(true); // Set auto-commit back to true
        } catch (SQLException e) {
            cnx.rollback(); // Rollback changes in case of exception
            e.printStackTrace();
            throw e;
        }
    }


    // Method to update an order (not needed for a single-field table)
}
