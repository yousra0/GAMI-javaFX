package Controller;

import Outil.DataBase;
import Service.Game_s;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class AfficherGame {

    @FXML
    private TextField dateTextField;

    @FXML
    private TextField descriptionTextField;

    @FXML
    private TextField imageTextField;

    @FXML
    private ImageView imageviewFile;

    @FXML
    private TextField nameTextField;

    public void setNameTextField(String nameTextField) {
        this.nameTextField.setText(nameTextField);
    }
    public void setDescriptionTextField(String descriptionText) {
        this.descriptionTextField.setText(descriptionText);
    }
    public void setDateTextField(String dateTextField) {
        this.dateTextField.setText(dateTextField);
    }
    public void setImageTextField(String imageTextField) {
        this.imageTextField.setText(imageTextField);
    }

    public void setImage(Image image)
    {
        imageviewFile.setImage(image);
    }


    @FXML
    void initialize() {
        String imagePath = imageTextField.getText();
        if (!imagePath.isEmpty()) {
            File file = new File(imagePath);
            Image image = new Image(file.toURI().toString());
            imageviewFile.setImage(image);
        }
    }
    @FXML
    private void retournerAajouterGame() {
        Stage stage = (Stage) nameTextField.getScene().getWindow(); // Récupérer la fenêtre actuelle
        stage.close(); // Fermer la fenêtre actuelle

        // Ouvrir la page AjouterGame.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/addgame.fxml"));
        try {
            Parent root = loader.load();
            Stage ajouterPostStage = new Stage();
            ajouterPostStage.setScene(new Scene(root));
            ajouterPostStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    Connection conn= DataBase.getInstance().getConn() ;
    Game_s gameService = new Game_s(conn);
    private void afficherErreur(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }

    private void afficherInformation(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.show();
    }
    @FXML
    private void supprimerGame() {
        // Récupérer le titre du game à supprimer
        String name = nameTextField.getText();

        try {
            // Supprimer le jeu
            gameService.deleteByName(name);
            afficherInformation("game a été supprimé avec succès.");
        } catch (SQLException e) {
            afficherErreur("Une erreur s'est produite lors de la suppression du game : " + e.getMessage());
        }
    }

    public void showall(ActionEvent actionEvent) {
        Stage stage = (Stage) nameTextField.getScene().getWindow(); // Récupérer la fenêtre actuelle
        stage.close(); // Fermer la fenêtre actuelle

        // Ouvrir la page showGame.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Backgame.fxml"));
        try {
            Parent root = loader.load();
            Stage aa = new Stage();
            aa.setScene(new Scene(root));
            aa.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
