package nalu.gami2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import entity.user;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import service.user_s;
import outil.database;
import java.io.File;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {

    private File selectedFile; // Field to store the selected file


    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField paysField;

    @FXML  private DatePicker picker;

    @FXML private TextField pprofileField;

    private final user_s userService;

    public SignUpController() {
        this.userService = new user_s(database.getInstance().getConn());
    }

    @FXML
    private void handleSignUp() {
        try {
            if (selectedFile == null) {
            throw new SQLException("Selectionner une image.");
         }
            if (selectedFile == null) {
                throw new SQLException("Selectionner une image.");
            }
            String formattedDate;

            // Créez un objet user à partir des champs de saisie
            user newUser = new user();
            newUser.setEmail(emailField.getText());
            newUser.setPassword(passwordField.getText());
            newUser.setNom(nomField.getText());
            newUser.setPrenom(prenomField.getText());
            newUser.setPays(paysField.getText());
            LocalDate selectedDate = picker.getValue();
            if (selectedDate == null) {
                throw new SQLException("Selectionner une date.");
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            // Format the selected date as a string
            formattedDate = selectedDate.format(formatter);

            newUser.setDatenai(formattedDate);
            newUser.setPprofile(selectedFile.toURI().toString());
            newUser.setRoles("[\"ROLE_USER\"]");

            // Utilisez user_s pour ajouter l'utilisateur avec les contrôles de saisie
            userService.add(newUser);

            // Afficher une alerte de succès
            showAlert(AlertType.INFORMATION, "Inscription Réussie", "Votre compte a été créé avec succès.");



        } catch (SQLException e) {
            // Afficher une alerte d'erreur
            showAlert(AlertType.ERROR, "Erreur d'Inscription", e.getMessage());
        }
    }

    @FXML
    private void handleGoToSignIn() {
        Gami.showSignIn();
    }

    @FXML
    private void chooseFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        // Show open file dialog
        selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            String imagePath = selectedFile.toURI().toString();
            System.out.println("Selected file: " + imagePath);
            // Now you can save the imagePath to the database or perform any other operation
        }
    }


    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        picker.setDayCellFactory(getPastDaysCellFactory());

    }


    private Callback<DatePicker, DateCell> getPastDaysCellFactory() {
        return datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                // Disable future dates
                if (item.isAfter(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #eeeeee;"); // Optional: change the background color for disabled dates
                }
            }
        };
    }
}
