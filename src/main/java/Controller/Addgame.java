package Controller;

import Entity.Game;
import Outil.DataBase;
import Service.Game_s;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Addgame {

    @FXML
    private DatePicker dateTextField;

    @FXML
    private TextField descriptionTextField;

    @FXML
    private ImageView imageviewFile;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField imageTextField;

    Connection conn= DataBase.getInstance().getConn() ;
    Game_s gameService = new Game_s(conn);

    @FXML
    void addGame(ActionEvent event) {

        // Vérifier si les champs obligatoires sont remplis
        if (nameTextField.getText().isEmpty() || descriptionTextField.getText().isEmpty() || dateTextField.getValue() == null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Veuillez remplir tous les champs obligatoires.");
            alert.show();
            return;
        }

        // Vérifier la longueur du champ nameTextField
        int nameLength = nameTextField.getText().length();
        if (nameLength < 2 || nameLength > 10) {
            afficherErreur("La taille du name doit être entre 2 et 10 caractères.");
            return;
        }

        // Vérifier la longueur du champ descriptionTextField
        int descriptionLength = descriptionTextField.getText().length();
        if (descriptionLength < 5 || descriptionLength > 50) {
            afficherErreur("La taille du contenu doit être entre 5 et 50 caractères.");
            return;
        }




        // Récupérer le chemin du fichier image
        String file = imageTextField.getText();

        // Vérifier si le fichier existe
        if (!Files.exists(Paths.get(file))) {
            afficherErreur("Le fichier spécifié n'existe pas.");
            return;
        }

        LocalDate selectedDate = dateTextField.getValue();

        // Format the selected date into a string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateString = selectedDate.format(formatter);

        Game game = new Game(nameTextField.getText(), descriptionTextField.getText(), dateString, imageTextField.getText());

        try {
            //add
            gameService.add(game);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("game ajouté avec succés");
            alert.show();

            //show
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherGame.fxml"));
            try
            {
                Parent root = loader.load();
                AfficherGame afficherGame = loader.getController();
                afficherGame.setNameTextField(nameTextField.getText());
                afficherGame.setDescriptionTextField(descriptionTextField.getText());
                afficherGame.setDateTextField(dateString);
                afficherGame.setImageTextField(imageTextField.getText());
                afficherGame.setImage(imageviewFile.getImage());

                nameTextField.getScene().setRoot(root);
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }

        }
        catch (SQLException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
        }

    }


    @FXML
    void choisirimage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            String imagePath = file.toURI().toString();
            Image image = new Image(imagePath);
            imageviewFile.setImage(image);
            imageTextField.setText(file.getAbsolutePath());
        }

    }
    private void afficherErreur(String message)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }

}
