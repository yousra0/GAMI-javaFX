package Service;

import Entity.order_product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class order_product_s {

    private Connection cnx;

    public order_product_s(Connection cnx) {
        this.cnx = cnx;
    }

    // Method to add a product to an order
    public void addProductToOrder(order_product orderProduct) throws SQLException {
        String query = "INSERT INTO `order_product`(`order_id`, `product_id`) VALUES (?, ?)";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setInt(1, orderProduct.getOrderId());
            statement.setInt(2, orderProduct.getProductId());
            statement.executeUpdate();
        }
    }

    // Method to remove a product from an order
    public void removeProductFromOrder(int orderId, int productId) throws SQLException {
        String query = "DELETE FROM `order_product` WHERE `order_id`=? AND `product_id`=?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setInt(1, orderId);
            statement.setInt(2, productId);
            statement.executeUpdate();
        }
    }

    // You can add more methods as needed
}
