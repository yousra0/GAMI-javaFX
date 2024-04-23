package tn.esprit;

import Outil.DataBase;
import Service.order_s;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // Create a database connection
            DataBase db = DataBase.getInstance();
            // Get the connection
            Connection cnx = db.getConn();

            // Create an instance of order_s and pass the connection
            order_s orderService = new order_s(cnx);

            // Test addOrder method
            testAddOrder(orderService);

            // Test deleteOrder method
            testDeleteOrder(orderService);

            // Test getAllOrders method
            testGetAllOrders(orderService);

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Method to test the addOrder method
    private static void testAddOrder(order_s orderService) throws SQLException {
        // Add a new order
        int orderId = 1001; // Sample order ID
        orderService.addOrder(orderId);

        System.out.println("Order added successfully!");
    }

    // Method to test the deleteOrder method
    private static void testDeleteOrder(order_s orderService) throws SQLException {
        // Specify the ID of the order to delete
        int orderIdToDelete = 1001; // Assuming the ID of the order you want to delete is 1001

        // Delete the order
        orderService.deleteOrder(orderIdToDelete);

        System.out.println("Order deleted successfully!");
    }

    // Method to test the getAllOrders method
    private static void testGetAllOrders(order_s orderService) throws SQLException {
        // Retrieve all orders
        List<Integer> orderList = orderService.getAllOrders();

        // Print information of each order
        for (int orderId : orderList) {
            System.out.println("Order ID: " + orderId);
            System.out.println("----------------------------------------");
        }
    }
}
