package Controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import Entity.Game;
import Outil.DataBase;
import Service.Game_s;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class FrontJeuxController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private VBox Vjeux;

    @FXML
    private Label best;

    @FXML
    private GridPane gridgame;

    @FXML
    private Button mainpage;

    @FXML
    private Label newestbtn;

    @FXML
    private Label note;

    @FXML
    private TextField rech;

    @FXML
    private Button rechbtn;

    @FXML
    private Label topbtn;
    private final Connection conn = DataBase.getInstance().getConn();
    private final Game_s gamesS = new Game_s(conn);

    private void loadGames() {
        try {
            // Use the constructor of Game that takes the database connection
            Game_s tS = new Game_s(conn);
            // Load the list of Tournois
            List<Game> resL = tS.show();

            // Clear the existing content of the GridPane
            gridgame.getChildren().clear();


            int row = 1;
            int col = 0;

            // Iterate through the list of Tournois
            for (Game tournois : resL) {
                col++;
                // Load ContainerTournois.fxml for each Tournois
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/containergame.fxml"));
                Parent interfaceRoot = loader.load();
                Containergame item = loader.getController();
                item.setTournois(tournois);


                // Add the loaded item to the GridPane
                gridgame.add(interfaceRoot, col, row);
                gridgame.setHgap(20); // Set the horizontal gap between items
                gridgame.setVgap(20); // Set the vertical gap between items

                // Adjust row and column indices
                if (col == 4) {
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
    void Top(MouseEvent event) {

    }

    @FXML
    void again(ActionEvent event) {

    }

    @FXML
    void recherche(ActionEvent event) {

    }

    @FXML
    void initialize() {
        loadGames();

    }

}
