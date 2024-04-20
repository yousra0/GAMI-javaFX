package Controller;

import Entity.Post;
import Outil.DataBase;
import Service.Post_s;
import io.github.palexdev.materialfx.controls.MFXDatePicker;

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

import java.io.IOException;
import java.io.File;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.sql.Connection;
import java.sql.SQLException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class AjouterPost
{
    @FXML
    private TextField titreTextField;

    @FXML
    private TextField contenuTextField;

    @FXML
    private MFXDatePicker dateTextField;

    @FXML
    private TextField fichierTextField;

    @FXML
    private TextField likesTextField;

    @FXML
    private TextField dislikesTextField;

    @FXML
    private ImageView imageviewFile;

    Connection conn= DataBase.getInstance().getConn() ;
    Post_s postService = new Post_s(conn);
    @FXML
    void ajouterPost(ActionEvent event)
    {
        // Vérifier la longueur des champs titreTextField et contenuTextField
        if (titreTextField.getText().length() < 2 || titreTextField.getText().length() > 20 ||
                contenuTextField.getText().length() < 2 || contenuTextField.getText().length() > 350)
        {
            afficherErreur("La taille du titre et du contenu doit être entre 2 et 10 caractères.");
            return;
        }

        // Vérifier si les champs obligatoires sont remplis
        if (titreTextField.getText().isEmpty() || contenuTextField.getText().isEmpty() || dateTextField.getValue() == null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Veuillez remplir tous les champs obligatoires.");
            alert.show();
            return;
        }

        // Vérifier si les champs obligatoires sont remplis
        if (titreTextField.getText().isEmpty() || contenuTextField.getText().isEmpty() || dateTextField.getValue() == null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Veuillez remplir tous les champs obligatoires.");
            alert.show();
            return;
        }

        // Vérifier si les champs likes et dislikes contiennent des nombres valides
        int likes, dislikes;
        try
        {
            likes = Integer.parseInt(likesTextField.getText());
            dislikes = Integer.parseInt(dislikesTextField.getText());
        }
        catch (NumberFormatException e)
        {
            afficherErreur("Les champs likes et dislikes doivent être des nombres entiers.");
            return;
        }

        // Vérifier si les nombres de likes et dislikes sont positifs
        if (likes < 0 || dislikes < 0)
        {
            afficherErreur("Les nombres de likes et dislikes doivent être positifs.");
            return;
        }

        // Récupérer le chemin du fichier image
        String file = fichierTextField.getText();

        // Vérifier si le fichier existe
        if (!Files.exists(Paths.get(file)))
        {
            afficherErreur("Le fichier spécifié n'existe pas.");
            return;
        }

        LocalDate selectedDate = dateTextField.getValue();

        // Format the selected date into a string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateString = selectedDate.format(formatter);

        Post post = new Post(titreTextField.getText(), contenuTextField.getText(), dateString, fichierTextField.getText(), Integer.parseInt(likesTextField.getText()), Integer.parseInt(dislikesTextField.getText()));

        try
        {
            //add
            postService.add(post);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("post ajouté avec succés");
            alert.show();

            //show
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherPost.fxml"));
            try
            {
                Parent root = loader.load();
                AfficherPost afficherPost = loader.getController();
                afficherPost.setTitreTextField(titreTextField.getText());
                afficherPost.setContenuTextField(contenuTextField.getText());
                afficherPost.setDateTextField(dateString);
                afficherPost.setFichierTextField(fichierTextField.getText());
                afficherPost.setLikesTextField(Integer.parseInt(likesTextField.getText()));
                afficherPost.setDislikesTextField(Integer.parseInt(dislikesTextField.getText()));
                afficherPost.setImage(imageviewFile.getImage());

                titreTextField.getScene().setRoot(root);
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }

        }
        catch (SQLException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
        }

    }
    private void afficherErreur(String message)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }

    @FXML
    private void choisirImage()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            String imagePath = file.toURI().toString();
            Image image = new Image(imagePath);
            imageviewFile.setImage(image);
            fichierTextField.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void navigateToMusicPlayer(ActionEvent event)
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/music-player.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            // Fermer la fenêtre actuelle
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void initialize()
    {}
}
