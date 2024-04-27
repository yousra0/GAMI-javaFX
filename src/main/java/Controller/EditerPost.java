package Controller;

import Entity.Post;
import Outil.DataBase;
import Service.Post_s;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

public class EditerPost
{
    @FXML
    private TextField contenuTextField;

    @FXML
    private TextField titreTextField;

    @FXML
    private TextField dateTextField;

    @FXML
    private TextField fichierTextField;

    @FXML
    private TextField likesTextField;

    @FXML
    private TextField dislikesTextField;

    @FXML
    private ImageView imageviewFile;

    public void setTitreTextField(String titreTextField) {
        this.titreTextField.setText(titreTextField);
    }

    public void setContenuTextField(String contenuTextField) {
        this.contenuTextField.setText(contenuTextField);
    }

    public void setDateTextField(String dateTextField) {
        this.dateTextField.setText(dateTextField);
    }

    public void setFichierTextField(String fichierTextField) {
        this.fichierTextField.setText(fichierTextField);
    }

    public void setLikesTextField(int likesTextField) {
        this.likesTextField.setText(String.valueOf(likesTextField));
    }

    public void setDislikesTextField(int dislikesTextField) {
        this.dislikesTextField.setText(String.valueOf(dislikesTextField));
    }
    private Post post;
    private Post_s postService;
    Connection conn= DataBase.getInstance().getConn() ;
    Post_s postS=new Post_s(conn);

    public EditerPost()
    {
        // Default constructor
    }
    public void setPost(Post post) {
        if (post == null) {
            throw new IllegalArgumentException("Post ne peut pas être null");
        }
        this.post = post;
        this.titreTextField.setText(post.getTitre());
        this.contenuTextField.setText(post.getContenu_pub());
        this.dateTextField.setText(post.getDate_pub().toString());
        this.fichierTextField.setText(post.getFile());
        this.likesTextField.setText(String.valueOf(post.getLikes()));
        this.dislikesTextField.setText(String.valueOf(post.getDislikes()));
    }
    @FXML
    public void enregistrer(ActionEvent event) {
        // Récupérer les nouvelles valeurs des champs
        String newTitre = titreTextField.getText();
        String newContenu = contenuTextField.getText();
        String newFile = fichierTextField.getText();
        int newLikes = Integer.parseInt(likesTextField.getText());
        int newDislikes = Integer.parseInt(dislikesTextField.getText());

        // Récupérer la date actuelle
        java.util.Date currentDate = new java.util.Date();
        Date newDate = new Date(currentDate.getTime());

        // Créer un nouvel objet Post avec les nouvelles valeurs
        Post updatedPost = new Post();
        updatedPost.setTitre(newTitre);
        updatedPost.setContenu_pub(newContenu);
        updatedPost.setDate_pub(newDate);
        updatedPost.setFile(newFile);
        updatedPost.setLikes(newLikes);
        updatedPost.setDislikes(newDislikes);

        // Définir l'ID du post à éditer
        updatedPost.setId(post.getId());

        try {
            // Appeler la méthode edit du service pour mettre à jour le post dans la base de données
            postService.edit(updatedPost);

            // Afficher un message de succès
            afficherInformation("Post modifié avec succès.");

            // Rafraîchir la vue en rechargeant les données
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherPost.fxml"));
            Parent root = loader.load();
            AfficherPost controller = loader.getController();
            controller.initialize(); // Vous devrez peut-être passer des données nécessaires pour initialiser le contrôleur

            // Fermer la fenêtre d'édition
            Stage stage = (Stage) dateTextField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (SQLException | IOException e) {
            // Gérer les erreurs en affichant un message d'erreur
            afficherErreur("Une erreur s'est produite lors de l'enregistrement du post.");
            e.printStackTrace();
        }
    }
    private void setImage(Image image)
    {
        imageviewFile.setImage(image);
    }
    @FXML
    private void chargerImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            setImage(image);
        }
    }
    private void afficherInformation(String message)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.show();
    }
    @FXML
    private void retournerAAfficherPost(ActionEvent event)
    {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    private void afficherErreur(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}