package nalu.gami2;
import entity.user;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import service.user_s;

import java.io.IOException;

public class SignInController {
    @FXML
    public TextField emailField;

    @FXML
    public PasswordField passwordField;

    @FXML
    public Button signInButton;


    @FXML
    public void goToForgot(){
        try {
            Stage stage = (Stage) passwordField.getScene().getWindow();
            stage.close();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("forgotPassword.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goToSignUp(){
        try {
            Stage stage = (Stage) passwordField.getScene().getWindow();
            stage.close();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sign-up.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void signin() {
        signInButton.setOnAction(event -> {
            String email = emailField.getText();
            String password = passwordField.getText();
            user_s userService = new user_s(outil.database.getInstance().getConn());
            try {
                if (userService.signIn(email, password)) {
                    user currentUser = userService.getUserByEmail(email);
                    SessionManager.setCurrentUser(currentUser);
                    // Vérifiez si l'utilisateur connecté est l'admin pour charger la vue appropriée.
                    if (SessionManager.isAdmin()) {
                        Gami.showUsersView(); // Chargez la vue de la table des utilisateurs pour les admins.
                    } else {
                        loadUserProfileView(); // Chargez la vue de profil pour les utilisateurs normaux.
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Échec de la connexion. E-mail ou mot de passe incorrect.");
                    alert.showAndWait();                }
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Une erreur s'est produite : " + e.getMessage());
                alert.showAndWait();
                e.printStackTrace();                e.printStackTrace();
            }
        });
    }
    private void loadUserProfileView() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("user-profile.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) signInButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Profil Utilisateur");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Gérer l'exception (par exemple, afficher une alerte à l'utilisateur)
        }
    }
}


