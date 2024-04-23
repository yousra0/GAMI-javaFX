package Controller;

import Entity.order;
import Entity.order_product;
import Entity.product;
import Outil.DataBase;
import Service.order_s;
import Service.product_s;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderController {

    @FXML
    private TilePane orderTilePane;

    @FXML
    private Button addToOrderButton;

    @FXML
    private Button deleteSelectedOrdersButton; // New delete button

    @FXML
    private Label messageLabel;

    @FXML
    private ListView<String> orderList;

    private List<order> orders = new ArrayList<>();
    private product_s productService;
    private List<CheckBox> checkBoxes = new ArrayList<>(); // List to store checkboxes

    @FXML
    public void initialize() {
        try {
            Connection connection = DataBase.getInstance().getConn();
            productService = new product_s(connection);

            // Fetch and display orders
            fetchOrders();
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error initializing controller.");
        }
    }

    @FXML
    public void addToOrderClicked() {
        // Existing code...
    }

    @FXML
    public void deleteOrder() {
        try {
            List<order> selectedOrders = new ArrayList<>();

            // Collect selected orders
            for (CheckBox checkBox : checkBoxes) {
                if (checkBox.isSelected()) {
                    selectedOrders.add((order) checkBox.getUserData());
                }
            }

            // Debugging statement
            System.out.println("Selected Orders to delete: " + selectedOrders);

            order_s orderService = new order_s(DataBase.getInstance().getConn());

            // Delete selected orders
            for (order selectedOrder : selectedOrders) {
                orderService.deleteOrder(selectedOrder.getId());
            }

            // Refresh the order list after deletion
            fetchOrders();
            messageLabel.setText("Selected orders deleted successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error deleting orders.");
        }
    }






    private void fetchOrders() {
        try {
            Connection connection = DataBase.getInstance().getConn();

            // Query to fetch orders along with their associated products using the join table
            String query = "SELECT o.id as order_id, p.* FROM `order` o " +
                    "JOIN order_product op ON o.id = op.order_id " +
                    "JOIN product p ON op.product_id = p.id";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            orders.clear(); // Clear existing orders

            // Map to store products for each order
            Map<Integer, List<product>> orderProductsMap = new HashMap<>();

            while (resultSet.next()) {
                int orderId = resultSet.getInt("order_id");
                int productId = resultSet.getInt("id");

                // Create product object from the resultSet
                product fetchedProduct = new product(
                        productId,
                        resultSet.getString("reference"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getInt("price"),
                        resultSet.getString("image"),
                        resultSet.getInt("rating"),
                        resultSet.getInt("user_id")
                );

                // Add product to the list of products for the corresponding order
                orderProductsMap.computeIfAbsent(orderId, k -> new ArrayList<>()).add(fetchedProduct);
            }

            // Create order objects with their associated products
            for (Map.Entry<Integer, List<product>> entry : orderProductsMap.entrySet()) {
                int orderId = entry.getKey();
                List<product> products = entry.getValue();

                order newOrder = new order(orderId, products);
                orders.add(newOrder);
            }

            statement.close();
            updateOrderTilePane();

        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error fetching orders.");
        }
    }

    @FXML
    public void editOrder(order order) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/editOrder.fxml"));
            Parent editOrderView = loader.load();

            EditOrderController editOrderController = loader.getController();

            // Pass the selected order and connection to the editOrderController
            editOrderController.setOrder(order, DataBase.getInstance().getConn());

            // Get the current stage and set the new scene
            Stage stage = (Stage) orderTilePane.getScene().getWindow();
            stage.setScene(new Scene(editOrderView));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setText("Error loading edit view.");
        }
    }


    private void updateOrderTilePane() {
        orderTilePane.getChildren().clear(); // Clear existing tiles

        for (order order : orders) {
            javafx.scene.Node orderTile = createOrderTile(order);
            if (orderTile != null) {
                orderTilePane.getChildren().add(orderTile);
            }
        }
    }

    private javafx.scene.Node createOrderTile(order order) {
        List<product> products = order.getProducts();

        // Create a VBox to hold the product details
        VBox productDetails = new VBox();
        productDetails.setSpacing(5);
        productDetails.setPadding(new Insets(10));
        productDetails.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5px;");

        // Create a CheckBox for each order
        CheckBox checkBox = new CheckBox("Select");
        checkBox.setUserData(order); // Set the order as user data for the checkbox
        checkBoxes.add(checkBox); // Add checkbox to the list

        // Add checkbox to the VBox
        productDetails.getChildren().add(checkBox);

        // Create an Edit button
        Button editButton = new Button("Edit");
        editButton.setOnAction(e -> editOrder(order)); // Set the action to call editOrder method

        // Add Edit button to the VBox
        productDetails.getChildren().add(editButton);

        if (products != null && !products.isEmpty()) {
            for (product product : products) {
                // Add product details to the VBox
                Label nameLabel = new Label("Name: " + product.getName());
                Label idLabel = new Label("ID: " + product.getId());
                Label referenceLabel = new Label("Reference: " + product.getReference());
                Label descriptionLabel = new Label("Description: " + product.getDescription());
                Label priceLabel = new Label("Price: " + product.getPrice());
                Label imageLabel = new Label("Image: " + product.getImage());
                Label ratingLabel = new Label("Rating: " + product.getRating());

                // Add labels to the VBox
                productDetails.getChildren().addAll(nameLabel, idLabel, referenceLabel, descriptionLabel, priceLabel, imageLabel, ratingLabel);
            }
        } else {
            // Display a message if no products are associated
            Label noProductLabel = new Label("No products associated");
            noProductLabel.setStyle("-fx-padding: 10px; -fx-border-color: #ccc; -fx-border-radius: 5px;");
            productDetails.getChildren().add(noProductLabel);
        }

        return productDetails;
    }

}
