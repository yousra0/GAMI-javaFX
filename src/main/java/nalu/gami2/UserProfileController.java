package nalu.gami2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import service.user_s;
import entity.user;

import java.io.IOException;

public class UserProfileController {
    @FXML private TextField nameField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private TextField datenaiField;
    @FXML private TextField paysField;
    @FXML private ImageView profileImageView;
    @FXML private Button editButton;

    private user_s userService;
    private user currentUser;
    private boolean inEditMode = false;

    user connectedUser;


    public void initialize() {
        userService = new user_s(outil.database.getInstance().getConn());
        loadUserProfile();
        toggleFields(false); // Disable fields initially
    }

    private void loadUserProfile() {
        currentUser = SessionManager.getCurrentUser();
        if (currentUser != null) {
            nameField.setText(currentUser.getNom());
            prenomField.setText(currentUser.getPrenom());
            emailField.setText(currentUser.getEmail());
            datenaiField.setText(currentUser.getDatenai());
            paysField.setText(currentUser.getPays());

            String imageUrl = currentUser.getPprofile();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                try {
                    Image image = new Image(imageUrl, true);
                    profileImageView.setImage(image);
                } catch (IllegalArgumentException e) {
                    System.out.println("Error loading image from URL: " + imageUrl);
                    e.printStackTrace();
                    showAlert("Image Load Error", "Profile image cannot be loaded: " + imageUrl, AlertType.ERROR);
                }
            } else {
                showAlert("Image Not Found", "No profile image URL provided.", AlertType.INFORMATION);
            }
        } else {
            showAlert("Error", "No logged-in user found.", AlertType.ERROR);
            // Redirect to login screen or handle accordingly
        }
    }


    @FXML
    private void handleEditAction() {
        if (inEditMode) {
            // Save changes
            try {
                currentUser.setNom(nameField.getText());
                currentUser.setPrenom(prenomField.getText());
                currentUser.setEmail(emailField.getText());
                currentUser.setDatenai(datenaiField.getText());
                currentUser.setRoles(currentUser.getRoles());
                currentUser.setPays(paysField.getText());
                currentUser.setPprofile(currentUser.getPprofile()); // Handle profile picture change if necessary

                userService.edit(currentUser);
                showAlert("Success", "Profile updated successfully!", AlertType.INFORMATION);
            } catch (Exception e) {
                showAlert("Error", "An error occurred while saving profile: " + e.getMessage(), AlertType.ERROR);
            }
        }
        inEditMode = !inEditMode;
        toggleFields(inEditMode);
        editButton.setText(inEditMode ? "Save" : "Edit");
    }

    private void toggleFields(boolean enable) {
        nameField.setEditable(enable);
        prenomField.setEditable(enable);
        emailField.setEditable(enable);
        datenaiField.setEditable(enable);
        paysField.setEditable(enable);

        // Do not allow editing the primary key (email in this case) if it's being used as such
    }

    private void showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void logout(ActionEvent event) {
        try {
            Stage stage = (Stage) profileImageView.getScene().getWindow();
            stage.close();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sign-in.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void initUser(user u) {
        this.connectedUser = u;
    }
}
