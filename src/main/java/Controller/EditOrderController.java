package Controller;

import Entity.order;
import Entity.product;
import Service.product_s;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class EditOrderController {

    @FXML
    private Label titleLabel;

    @FXML
    private ListView<String> productsListView;

    @FXML
    private TextField totalPriceTextField;

    private order currentOrder;
    private product_s productService;

    private Connection cnx;

    public void initialize() {
        // Initialize your components here
    }

    @FXML
    public void saveOrder() {
        // Handle save order logic here
    }

    public void setOrder(order order, Connection cnx) {
        this.cnx = cnx; // Assign connection
        titleLabel.setText("Edit Order: " + order.getId());

        // Fetch products for the current order and update UI
        try {
            productService = new product_s(cnx);
            List<product> products = productService.getProductsByOrderId(order.getId());

            // Populate the productsListView with product details
            productsListView.getItems().clear(); // Clear previous items

            for (product prod : products) {
                String item = prod.getName() + " - $" + prod.getPrice();
                productsListView.getItems().add(item);
            }

            // Calculate and set total price
            double totalPrice = products.stream().mapToDouble(product::getPrice).sum();
            totalPriceTextField.setText(String.format("%.2f", totalPrice));

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle error
        }
    }

}
