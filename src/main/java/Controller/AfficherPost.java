package Controller;


import Entity.Post;
import Outil.DataBase;
import Service.Post_s;
import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

public class AfficherPost
{
    @FXML
    private TextField titreTextField;

    @FXML
    private TextField contenuTextField;

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

    public void setImage(Image image)
    {
        imageviewFile.setImage(image);
    }
    public AfficherPost()
    {
        Connection conn = DataBase.getInstance().getConn();
        postService = new Post_s(conn);
    }
    @FXML
    void initialize()
    {
        String imagePath = fichierTextField.getText();
        if (!imagePath.isEmpty())
        {
            File file = new File(imagePath);
            Image image = new Image(file.toURI().toString());
            imageviewFile.setImage(image);
        }
    }
    @FXML
    private void retournerAajouterPost()
    {
        Stage stage = (Stage) titreTextField.getScene().getWindow(); // Récupérer la fenêtre actuelle
        stage.close(); // Fermer la fenêtre actuelle

        // Ouvrir la page AjouterPost.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterPost.fxml"));
        try {
            Parent root = loader.load();
            Stage ajouterPostStage = new Stage();
            ajouterPostStage.setScene(new Scene(root));
            ajouterPostStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    Connection conn= DataBase.getInstance().getConn() ;
    Post_s postService = new Post_s(conn);

    @FXML
    private void supprimerPost()
    {
        // Récupérer le titre du post à supprimer
        String titre = titreTextField.getText();

        try {
            // Supprimer le post
            postService.deleteByTitre(titre);
            afficherInformation("Le post a été supprimé avec succès.");
        } catch (SQLException e) {
            afficherErreur("Une erreur s'est produite lors de la suppression du post : " + e.getMessage());
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

    @FXML
    void editPost(ActionEvent event)
    {
        try {
            // Récupérer les nouvelles valeurs des champs
            String newTitre = titreTextField.getText();
            String newContenu = contenuTextField.getText();
            String newDateString = dateTextField.getText(); // Utilise getText() pour les TextField
            String newFile = fichierTextField.getText();
            int newLikes = Integer.parseInt(likesTextField.getText());
            int newDislikes = Integer.parseInt(dislikesTextField.getText());

            // Créer un nouveau post avec les nouvelles valeurs
            Post editedPost = new Post(newTitre, newContenu, newDateString, newFile, newLikes, newDislikes);

            // Mettre à jour le post dans la base de données
            postService.edit(editedPost);

            // Afficher un message de confirmation
            afficherInformation("Le post a été modifié avec succès.");
        } catch (NumberFormatException e) {
            // En cas d'erreur de formatage des nombres
            afficherErreur("Veuillez saisir un nombre valide pour les likes et les dislikes.");
        } catch (SQLException e) {
            // En cas d'erreur lors de la mise à jour de la base de données
            afficherErreur("Une erreur s'est produite lors de la modification du post : " + e.getMessage());
        }
    }




    //fonction exporter pdf dans le dossier documents
    @FXML
    private void exporterEnPDF(ActionEvent event)
    {
        Document document = new Document();

        try {
            // Générer un nom de fichier unique
            String fileName = "export_" + System.currentTimeMillis() + ".pdf";

            // Chemin d'accès où le fichier PDF sera enregistré
            String pathToSave = System.getProperty("user.home") + File.separator + "Documents" + File.separator + fileName;

            PdfWriter.getInstance(document, new FileOutputStream(pathToSave));

            document.open();
            document.add(new Paragraph("Titre: " + titreTextField.getText()));
            document.add(new Paragraph("Contenu: " + contenuTextField.getText()));
            document.add(new Paragraph("Date: " + dateTextField.getText()));
            document.add(new Paragraph("Fichier: " + fichierTextField.getText()));
            document.add(new Paragraph("Likes: " + likesTextField.getText()));
            document.add(new Paragraph("Dislikes: " + dislikesTextField.getText()));
            document.close();

            afficherInformation("Les données ont été exportées en PDF avec succès.");
        } catch (IOException | DocumentException e) {
            afficherErreur("Une erreur s'est produite lors de l'exportation en PDF : " + e.getMessage());
        }
    }

    @FXML
    void openMap(ActionEvent event)
    {
        Stage stage = new Stage();
        GluonMapExample mapExample = new GluonMapExample();
        mapExample.start(stage);

        Node node = (Node) event.getSource(); // Récupère le Node déclenchant l'événement
        Scene scene = node.getScene(); // Récupère la scène à partir du Node
        MapView mapView = (MapView) scene.lookup("#mapView"); // Remplacez "mapView" par l'ID de votre MapView
        MapPoint espritLocation = new MapPoint(36.89830009644064, 10.186927429895055);
        CustomCircleMarkerLayer markerLayer = new CustomCircleMarkerLayer(espritLocation);
        mapView.addLayer(markerLayer);
    }
    @FXML
    public void add(ActionEvent actionEvent)
    {
        Stage stage = (Stage) titreTextField.getScene().getWindow(); // Récupérer la fenêtre actuelle
        stage.close(); // Fermer la fenêtre actuelle

        // Ouvrir la page AjouterPost.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterPost.fxml"));
        try {
            Parent root = loader.load();
            Stage ajouterPostStage = new Stage();
            ajouterPostStage.setScene(new Scene(root));
            ajouterPostStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void search(ActionEvent actionEvent)
    {
    }
}
