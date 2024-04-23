package Controller;

import Outil.DataBase;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class show_products {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private VBox productsVBox;

    @FXML
    private Button addToOrderButton;

    private List<CheckBox> checkBoxes = new ArrayList<>();

    @FXML
    public void initialize() {
        // Fetch and display products
        fetchProducts();
    }

    public void fetchProducts() {
        try {
            Connection connection = DataBase.getInstance().getConn();

            String query = "SELECT * FROM product";
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                int price = resultSet.getInt("price");

                CheckBox checkBox = new CheckBox(name + " - " + description + " - Price: " + price);
                checkBoxes.add(checkBox);
                productsVBox.getChildren().add(checkBox);
            }

            statement.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void addToOrder() {
        try {
            // Insert order into the database
            Connection connection = DataBase.getInstance().getConn();
            String insertOrderQuery = "INSERT INTO `order` () VALUES ()";

            PreparedStatement preparedStatement = connection.prepareStatement(insertOrderQuery, Statement.RETURN_GENERATED_KEYS);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating order failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int orderId = generatedKeys.getInt(1);

                    // Insert selected products into order_product table
                    for (CheckBox checkBox : checkBoxes) {
                        if (checkBox.isSelected()) {
                            String[] productDetails = checkBox.getText().split(" - ");
                            String productName = productDetails[0];
                            int productPrice = Integer.parseInt(productDetails[2].split(": ")[1]);

                            // Fetch product ID based on product name
                            int productId;
                            String fetchProductIdQuery = "SELECT id FROM product WHERE name = ?";
                            PreparedStatement fetchProductIdStatement = connection.prepareStatement(fetchProductIdQuery);
                            fetchProductIdStatement.setString(1, productName);
                            ResultSet productIdResultSet = fetchProductIdStatement.executeQuery();

                            if (productIdResultSet.next()) {
                                productId = productIdResultSet.getInt("id");

                                String insertOrderProductQuery = "INSERT INTO order_product (order_id, product_id) VALUES (?, ?)";
                                PreparedStatement orderProductStatement = connection.prepareStatement(insertOrderProductQuery);
                                orderProductStatement.setInt(1, orderId);
                                orderProductStatement.setInt(2, productId);

                                orderProductStatement.executeUpdate();
                            }
                        }
                    }
                } else {
                    throw new SQLException("Creating order failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle error gracefully, show a meaningful message to the user
            // Maybe display an alert or log the error
        }
    }

    @FXML
    public void showOrderView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/order.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
