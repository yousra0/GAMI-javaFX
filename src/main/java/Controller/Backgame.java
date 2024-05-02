package Controller;

import Entity.Game;
import Outil.DataBase;
import Service.Game_s;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class Backgame {

    @FXML
    private VBox Vjeux;

    @FXML
    private Button catjeuxbtn;

    @FXML
    private Label errormsg;

    @FXML
    private GridPane gridgame;

    @FXML
    private AnchorPane jeuxback;

    @FXML
    private Button jeuxbtn;

    @FXML
    private AnchorPane jeuxform;
    private final Connection conn = DataBase.getInstance().getConn();
    private void loadGames() {
        try {
            // Use the constructor of Game that takes the database connection
            Game_s tS = new Game_s(conn);
            // Load the list of Games
            List<Game> resL = tS.show();

            // Clear the existing content of the GridPane
            gridgame.getChildren().clear();


            int row = 1;
            int col = 0;

            // Iterate through the list of Tournois
            for (Game tournois : resL) {
                col++;
                // Load ContainerTournois.fxml for each Tournois
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/containergameBack.fxml"));
                Parent interfaceRoot = loader.load();

                // Add some print statements for debugging
                System.out.println("Loaded FXML file: containergameBack.fxml");
                System.out.println("Loaded controller: " + loader.getController());

                ContainergameBack item = loader.getController();
                item.setTournois(tournois);

                // Add the loaded item to the GridPane
                gridgame.add(interfaceRoot, col, row);
                gridgame.setHgap(20); // Set the horizontal gap between items
                gridgame.setVgap(20); // Set the vertical gap between items

                // Adjust row and column indices
                if (col == 3) {
                    col = 0;
                    row++;
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            // Handle exceptions appropriately
        }

    }

    @FXML
    void Newest(MouseEvent event) {

    }

    @FXML
    void affcat(ActionEvent event) {

    }

    @FXML
    void affjeuxpage(ActionEvent event) {

    }

    @FXML
    void goaddgame(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/addgame.fxml"));
        try {
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @FXML
    void initialize() {
        loadGames();
    }

}
