package Controller;

import Entity.Post;
import Outil.DataBase;
import Service.Post_s;

import io.github.palexdev.materialfx.controls.MFXDatePicker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.SQLException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

    public EditerPost()
    {
        Connection conn = DataBase.getInstance().getConn();
        postService = new Post_s(conn);
    }
    Connection conn= DataBase.getInstance().getConn() ;
    Post_s postService = new Post_s(conn);
    private Post post;

    @FXML
    void initialize()
    {
        // Charger le post à éditer en fonction de son titre
        String titre = "Titre du post à éditer"; // Remplacez par le titre du post à éditer
        try {
            Post post = postService.getPostByTitre(titre);
            if (post != null) {
                // Afficher les valeurs du post dans les champs de texte
                titreTextField.setText(post.getTitre());
                contenuTextField.setText(post.getContenu_pub());
                dateTextField.setValue(LocalDate.parse(post.getDate_pub()));
                fichierTextField.setText(post.getFile());
                likesTextField.setText(String.valueOf(post.getLikes()));
                dislikesTextField.setText(String.valueOf(post.getDislikes()));
                this.post = post;
            } else {
                afficherErreur("Le post avec le titre \"" + titre + "\" n'a pas été trouvé.");
            }
        } catch (SQLException e) {
            afficherErreur("Une erreur s'est produite lors de la récupération du post : " + e.getMessage());
        }
    }

    @FXML
    void enregistrer(ActionEvent event)
    {
        // Récupérer les nouvelles valeurs des champs
        String titre = titreTextField.getText();
        String contenu = contenuTextField.getText();
        String date = dateTextField.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); // Assurez-vous que la date est bien formatée
        String fichier = fichierTextField.getText();
        int likes = Integer.parseInt(likesTextField.getText());
        int dislikes = Integer.parseInt(dislikesTextField.getText());

        // Mettre à jour le post avec les nouvelles valeurs
        post.setTitre(titre);
        post.setContenu_pub(contenu);
        post.setDate_pub(date);
        post.setFile(fichier);
        post.setLikes(likes);
        post.setDislikes(dislikes);
        try {
            // Mettre à jour le post dans la base de données
            postService.edit(post);
            afficherInformation("Le post a été modifié avec succès.");
        } catch (SQLException e) {
            afficherErreur("Une erreur s'est produite lors de la modification du post : " + e.getMessage());
        }
    }
    private void afficherErreur(String message)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }

    private void afficherInformation(String message)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.show();
    }

    public void retournerAAfficherPost(ActionEvent actionEvent) {
        // Code pour revenir à la vue AfficherPost
    }
}
