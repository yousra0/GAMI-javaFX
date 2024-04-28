package Controller;

import Entity.CategorieJeux;
import Entity.Game;
import Outil.DataBase;
import Service.CategorieJeux_s;
import Service.Game_s;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private TextField lienTextField;

    @FXML
    private TextField imageTextField;
    @FXML
    private ComboBox<String> combo;

    Connection conn = DataBase.getInstance().getConn();
    Game_s gameService = new Game_s(conn);
    ///jdida *******
    CategorieJeux_s categorieJeuxService = new CategorieJeux_s(conn);

    @FXML
    void addGame(ActionEvent event) {

        // Vérifier si une catégorie a été sélectionnée dans le ComboBox
        if (combo.getValue() == null) {
            afficherErreur("Veuillez sélectionner une catégorie.");
            return;
        }

        // Vérifier si les champs obligatoires sont remplis
        if (nameTextField.getText().isEmpty() || descriptionTextField.getText().isEmpty() || dateTextField.getValue() == null || lienTextField.getText().isEmpty()) {
            afficherErreur("Veuillez remplir tous les champs obligatoires.");
            return;
        }

        // Vérifier la longueur du champ nameTextField
        int nameLength = nameTextField.getText().length();
        if (nameLength < 2 || nameLength > 10) {
            afficherErreur("La taille du nom doit être entre 2 et 10 caractères.");
            return;
        }
        // Vérifier si le lien est valide
        String lien = lienTextField.getText();
        if (!isValidURL(lien)) {
            afficherErreur("Veuillez saisir un lien valide.");
            return;
        }

        // Vérifier la longueur du champ descriptionTextField
        int descriptionLength = descriptionTextField.getText().length();
        if (descriptionLength < 5 || descriptionLength > 50) {
            afficherErreur("La taille du contenu doit être entre 5 et 50 caractères.");
            return;
        }

        // Récupérer le chemin du fichier image
        String imagePath = imageTextField.getText();

        // Vérifier si le fichier image existe
        if (!Files.exists(Paths.get(imagePath))) {
            afficherErreur("Le fichier spécifié n'existe pas.");
            return;
        }

        LocalDate selectedDate = dateTextField.getValue();

        Game game = new Game(nameTextField.getText(), descriptionTextField.getText(), selectedDate, imageTextField.getText(),lienTextField.getText());

        try {
            // Ajouter le jeu
            gameService.add(game);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Jeu ajouté avec succès.");
            alert.show();

            // Afficher la vue des jeux
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherGame.fxml"));
            try {
                Parent root = loader.load();
                AfficherGame afficherGame = loader.getController();
                afficherGame.setNameTextField(nameTextField.getText());
                afficherGame.setDescriptionTextField(descriptionTextField.getText());
                afficherGame.setDateTextField(selectedDate.toString());
                afficherGame.setImageTextField(imageTextField.getText());
                afficherGame.setImage(imageviewFile.getImage());

                nameTextField.getScene().setRoot(root);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
        }


    }

    // Fonction pour vérifier si une chaîne correspond à un lien URL valide
    private boolean isValidURL(String url) {
        // Expression régulière pour vérifier si la chaîne est un lien URL valide
        String regex = "^(https?|ftp)://[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)+([/?].*)?$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
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

    private void afficherErreur(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }
    ///////////////////jdidaaa *****2
    @FXML
    void initialize() {
        try {
            // Appeler la méthode getAllCategories pour récupérer toutes les catégories
            List<CategorieJeux> categories = categorieJeuxService.getAllCategories();

            // Créer une liste pour stocker les noms des catégories
            List<String> categoryNames = new ArrayList<>();

            // Ajouter les noms des catégories à la liste
            for (CategorieJeux category : categories) {
                categoryNames.add(category.getNomCat());
            }

            // Peupler le ComboBox avec les noms des catégories récupérées
            combo.getItems().addAll(categoryNames);
        } catch (SQLException e) {
            // Gérer l'exception en fonction de vos besoins
            e.printStackTrace();
        }
        // Limiter la sélection de dates futures dans le DatePicker
        dateTextField.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                // Désactiver la sélection des dates futures
                setDisable(date.isAfter(LocalDate.now()));
            }
        });
    }


}
