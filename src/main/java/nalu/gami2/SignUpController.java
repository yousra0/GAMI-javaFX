package nalu.gami2;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import entity.user;
import service.user_s;
import outil.database;

import java.io.IOException;
import java.sql.SQLException;

public class SignUpController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField paysField;
    @FXML private TextField datenaiField;
    @FXML private TextField pprofileField;

    private final user_s userService;

    public SignUpController() {
        this.userService = new user_s(database.getInstance().getConn());
    }

    @FXML
    private void handleSignUp() {
        try {
            // Créez un objet user à partir des champs de saisie
            user newUser = new user();
            newUser.setEmail(emailField.getText());
            newUser.setPassword(passwordField.getText());
            newUser.setNom(nomField.getText());
            newUser.setPrenom(prenomField.getText());
            newUser.setPays(paysField.getText());
            newUser.setDatenai(datenaiField.getText());
            newUser.setPprofile(pprofileField.getText());
            newUser.setRoles("[\"ROLE_USER\"]"); // Supposons un rôle par défaut 'USER'

            // Utilisez user_s pour ajouter l'utilisateur avec les contrôles de saisie
            userService.add(newUser);

            // Afficher une alerte de succès
            showAlert(AlertType.INFORMATION, "Inscription Réussie", "Votre compte a été créé avec succès.");

            // Vous pouvez ici ajouter la navigation vers l'écran de connexion ou actualiser l'écran

        } catch (SQLException e) {
            // Afficher une alerte d'erreur
            showAlert(AlertType.ERROR, "Erreur d'Inscription", e.getMessage());
        }
    }

    @FXML
    private void handleGoToSignIn() {
        Gami.showSignIn();
    }


    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
