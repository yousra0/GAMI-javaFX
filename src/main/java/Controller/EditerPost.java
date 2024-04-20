package Controller;

import Entity.Post;
import Outil.DataBase;
import Service.Post_s;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import io.github.palexdev.materialfx.controls.MFXDatePicker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class EditerPost
{
    @FXML
    private TextField contenuTextField;

    @FXML
    private MFXDatePicker dateTextField;

    @FXML
    private TextField dislikesTextField;

    @FXML
    private TextField fichierTextField;

    @FXML
    private TextField likesTextField;

    @FXML
    private TextField titreTextField;




    public void setTitre(String titre)
    {
        titreTextField.setText(titre);
    }
    public void setContenu(String contenu)
    {
        contenuTextField.setText(contenu);
    }
    public void setDate(String date) {
        dateTextField.setText(date);
    }
    public void setFichier(String fichier) {
        fichierTextField.setText(fichier);
    }
    public void setLikes(int likes) {
        likesTextField.setText(String.valueOf(likes));
    }
    public void setDislikes(int dislikes) {
        dislikesTextField.setText(String.valueOf(dislikes));
    }

    @FXML
    private void retournerAAfficherPost() {
        Stage stage = (Stage) titreTextField.getScene().getWindow();
        stage.close();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherPost.fxml"));
        try {
            Parent root = loader.load();
            Stage afficherPostStage = new Stage();
            afficherPostStage.setScene(new Scene(root));
            afficherPostStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    Connection conn= DataBase.getInstance().getConn() ;
    Post_s postService = new Post_s(conn);
    @FXML
    void enregistrer(ActionEvent event)
    {
        LocalDate date = dateTextField.getValue();

        if (date != null)
        {
            String dateStr = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            String titre = titreTextField.getText();
            String contenu = contenuTextField.getText();
            String fichier = fichierTextField.getText();
            int likes = 0;
            int dislikes = 0;
            try {
                likes = Integer.parseInt(likesTextField.getText());
                dislikes = Integer.parseInt(dislikesTextField.getText());
            } catch (NumberFormatException e) {
                afficherErreur("Veuillez saisir des valeurs numériques pour les champs likes et dislikes.");
                return;
            }

            Post post = new Post();
            post.setTitre(titre);
            post.setContenu_pub(contenu);
            post.setDate_pub(dateStr);
            post.setFile(fichier);
            post.setLikes(likes);
            post.setDislikes(dislikes);

            try {
                postService.editByTitre(post);
                afficherInformation("Les modifications ont été enregistrées avec succès.");
                Stage stage = (Stage) titreTextField.getScene().getWindow();
                stage.close();
            } catch (SQLException e) {
                afficherErreur("Une erreur s'est produite lors de l'enregistrement des modifications : " + e.getMessage());
            }
        } else {
            afficherErreur("Veuillez sélectionner une date.");
        }
    }





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
}
