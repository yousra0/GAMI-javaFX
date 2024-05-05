package nalu.gami2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import entity.user;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import outil.database;
import service.user_s;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class UsersViewController implements Initializable {

    int number;

    @FXML
    private TableView<user> usersTable;

    @FXML
    private TableColumn<user, Integer> colId;

    @FXML
    private TableColumn<user, String> colEmail;

    @FXML
    private TableColumn<user, String> colNom;

    @FXML
    private TableColumn<user, String> colPrenom;

    @FXML
    private TableColumn<user, String> colPays;

    @FXML
    private TableColumn<user, String> colRoles;

    @FXML
    private Label NbrUsers;

    // Ajoutez des TableColumn pour d'autres propriétés si nécessaire

    private user_s userService;

    public UsersViewController() {
        userService = new user_s(database.getInstance().getConn());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadUsers();
        NbrUsers.setText(String.valueOf(number));
    }

    @FXML
    public void initialize() {
        // Initialisation des colonnes avec des PropertyValueFactory

        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colPays.setCellValueFactory(new PropertyValueFactory<>("pays"));
        colRoles.setCellValueFactory(new PropertyValueFactory<>("roles"));



        loadUsers();
    }

    private void loadUsers() {
        try {
            List<user> userList = userService.show();
            System.out.println("Nombre d'utilisateurs chargés : " + userList.size()); // Ajout pour le débogage
            number = userList.size();
            usersTable.getItems().setAll(userList);
        } catch (SQLException e) {
            e.printStackTrace(); // Changez ceci pour imprimer la pile d'exceptions
            showAlert("Erreur de chargement", "Impossible de charger la liste des utilisateurs. " + e.getMessage());
        }
    }

    @FXML
    private void deleteUser() {
        user selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            Optional<ButtonType> result = showAlertWithConfirmation("Suppression", "Êtes-vous sûr de vouloir supprimer cet utilisateur ?");
            if (result.get() == ButtonType.OK) {
                try {
                    userService.delete(selectedUser.getId());
                    usersTable.getItems().remove(selectedUser);
                    showAlert("Supprimé", "Utilisateur supprimé avec succès.");
                } catch (SQLException e) {
                    showAlert("Erreur de suppression", "Impossible de supprimer l'utilisateur : " + e.getMessage());
                }
            }
        } else {
            showAlert("Pas de sélection", "Veuillez sélectionner un utilisateur à supprimer.");
        }
    }

    private Optional<ButtonType> showAlertWithConfirmation(String title, String content) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        return alert.showAndWait();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void logout(ActionEvent event) {
        try {
            Stage stage = (Stage) NbrUsers.getScene().getWindow();
            stage.close();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sign-in.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}