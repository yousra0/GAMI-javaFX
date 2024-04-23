package Controller;

import Entity.Game;
import Outil.DataBase;
import Service.Game_s;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.List;

import javafx.event.ActionEvent;


    public class GameShow {

        @FXML
        private GridPane gridgame;
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

                    // Add some print statements for debugging
                    System.out.println("Loaded FXML file: containergame.fxml");
                    System.out.println("Loaded controller: " + loader.getController());

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
        void initialize() {
            loadGames();
        }
    }



