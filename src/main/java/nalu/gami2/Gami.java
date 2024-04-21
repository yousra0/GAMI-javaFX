package nalu.gami2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Gami extends Application {

    private static Stage primaryStage; // Variable statique pour le stage principal

    @Override
    public void start(Stage stage) {
        primaryStage = stage; // Initialisez primaryStage ici
        showSignUp(); // Affichez la scène de sign-up par défaut
    }

    // Méthode statique pour afficher la scène de sign-up
    public static void showSignUp() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Gami.class.getResource("sign-up.fxml"));
            primaryStage.setScene(new Scene(fxmlLoader.load()));
            primaryStage.setTitle("Inscription");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode statique pour afficher la scène de sign-in
    public static void showSignIn() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Gami.class.getResource("sign-in.fxml"));
            primaryStage.setScene(new Scene(fxmlLoader.load()));
            primaryStage.setTitle("Connexion");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void showUsersView() throws IOException {
        if (SessionManager.isAdmin()) {
            FXMLLoader loader = new FXMLLoader(Gami.class.getResource("users-view.fxml"));
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.setTitle("Vue des Utilisateurs");
            primaryStage.show();
        } else {
            // Gérer le cas où l'utilisateur n'est pas administrateur.
            System.out.println("Accès refusé: Vous n'avez pas les droits d'administrateur.");
        }
    }


    public static void showForgetPasswordView() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Gami.class.getResource("ForgetPassword.fxml"));
            primaryStage.setScene(new Scene(fxmlLoader.load()));
            primaryStage.setTitle("Réinitialisation du mot de passe");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
