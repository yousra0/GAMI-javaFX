package Controller;

import Entity.*;

import Outil.DataBase;
import Service.Game_s;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class GameShow {

        @FXML
        private GridPane gridgame;
        @FXML
        private Label newestbtn;
        @FXML
        private Label topbtn;
        @FXML
        private Button rechbtn;
        @FXML
        private TextField rech;
        @FXML
        private Button mainpage;
        @FXML
        private VBox Vjeux;
        private boolean catView = false;

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
        void Top(MouseEvent event) {

        }
        @FXML
        void Newest(MouseEvent event) {

        }
        @FXML
        void again(ActionEvent event) throws IOException {
            catView = true;

            Parent root = FXMLLoader.load(getClass().getResource("/Backcat.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }

        @FXML
        void recherche(ActionEvent event) throws SQLException {
            VBox jeuxContainer = new VBox();
            Game_s sj = new Game_s();
            sj.initConnection();
            if (event.getSource() == rechbtn) {

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("");
                alert.setHeaderText("");
                //nothing found message
                if(rech.getText().isEmpty()){ //rien saisie
                    alert.setContentText("Rien Saisie !");
                    alert.showAndWait();
                }
                else if(!rech.getText().isEmpty())
                {
                    List<Game> jeuxes = sj.rechercherJeuxParNom(rech.getText());
                    for (Game j : jeuxes) {
                        Label titleLabel = new Label(j.getName());
                        titleLabel.setFont(new Font(30));
                        System.out.println(titleLabel);

                        VBox jBox = new VBox(titleLabel);
                        jBox.setAlignment(Pos.CENTER);
                        jBox.setSpacing(20);
                        jeuxContainer.getChildren().add(jBox);


                        titleLabel.setOnMouseClicked(e -> {
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Jeuxdetails.fxml"));
                                Parent root = loader.load();
                                Jeuxdetails jeuxController = loader.getController();
                                jeuxController.setJeux(j);
                                Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                                stage.setScene(new Scene(root));
                                stage.show();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        });
                    }
                    ScrollPane scrollPane = new ScrollPane(jeuxContainer);
                    scrollPane.setFitToWidth(true);

                    Vjeux.getChildren().clear();

                    Vjeux.getChildren().add(scrollPane);

                }

            }
        }

        @FXML
        void initialize() {
            loadGames();
        }
    }



