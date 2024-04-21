package nalu.gami2;
import entity.user;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.user_s;

public class SignInController {
    @FXML
    public TextField emailField;

    @FXML
    public PasswordField passwordField;

    @FXML
    public Button signInButton;

    // Ajoutez un TextArea pour les messages du terminal
    @FXML
    public TextArea terminalMessages;


    @FXML
    public void signin() {
        signInButton.setOnAction(event -> {
            String email = emailField.getText();
            String password = passwordField.getText();
            user_s userService = new user_s(outil.database.getInstance().getConn());
            try {
                if (userService.signIn(email, password)) {
                    terminalMessages.appendText("Connexion réussie !\n");
                    user currentUser = userService.getUserByEmail(email);
                    SessionManager.setCurrentUser(currentUser);
                    // Vérifiez si l'utilisateur connecté est l'admin pour charger la vue appropriée.
                    if (SessionManager.isAdmin()) {
                        Gami.showUsersView(); // Chargez la vue de la table des utilisateurs pour les admins.
                    } else {
                        loadUserProfileView(); // Chargez la vue de profil pour les utilisateurs normaux.
                    }
                } else {
                    terminalMessages.appendText("Échec de la connexion. E-mail ou mot de passe incorrect.\n");
                }
            } catch (Exception e) {
                terminalMessages.appendText("Une erreur s'est produite : " + e.getMessage() + "\n");
                e.printStackTrace();
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


