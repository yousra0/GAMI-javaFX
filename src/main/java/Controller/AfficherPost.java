package Controller;


import Entity.Comment;
import Entity.Post;
import Outil.DataBase;
import Service.Comment_s;
import Service.Post_s;
import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @FXML
    private TextField commenttf;

    @FXML
    private MFXTextField commentTextField;

    @FXML
    private TextField searchTextField;

    @FXML
    private ComboBox<String> postComboBox;

    @FXML
    private ListView<String> postListView;

    @FXML
    private Button likeButton;

    @FXML
    private Button dislikeButton;
    
    private Connection conn = DataBase.getInstance().getConn();
    private Post_s postService = new Post_s(conn);
    private String selectedPostTitle;
    private List<Comment> comments = new ArrayList<>();
    private Post post;
    

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
        this.conn = DataBase.getInstance().getConn();
        this.postService = new Post_s(conn);
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


    @FXML
    private void supprimerPost() {
        // Récupérer le titre du post à supprimer
        String titre = titreTextField.getText();

        try {
            // Récupérer le post à partir du titre
            Post post = postService.getByTitre(titre);
            // Récupérer tous les commentaires associés à ce post
            List<Comment> comments = postService.getCommentsByPostId(post.getId());
            // Supprimer chaque commentaire associé à ce post
            for (Comment comment : comments) {
                postService.deleteComment(comment.getId());
            }
            // Enfin, supprimer le post lui-même
            postService.deleteByTitre(titre);
            afficherInformation("Le post et ses commentaires ont été supprimés avec succès.");

            // Rafraîchir la scène pour afficher les changements
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherPost.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) titreTextField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (SQLException | IOException e) {
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
    private void editPost(ActionEvent event) throws IOException
    {
        // Vérifier si un post est sélectionné
        if (selectedPostTitle == null || selectedPostTitle.isEmpty()) {
            afficherErreur("Veuillez sélectionner un post à éditer.");
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditerPost.fxml"));
        Parent root = loader.load();
        EditerPost editerPost = loader.getController();

        try {
            // Récupérer le post à éditer à partir du titre
            Post selectedPost = postService.getByTitre(selectedPostTitle);
            editerPost.setPost(selectedPost); // Make sure selectedPost is initialized
        } catch (SQLException e) {
            afficherErreur("Une erreur s'est produite lors de la récupération du post à éditer : " + e.getMessage());
            return;
        }

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        Stage currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        currentStage.close();
        stage.show();
    }


    @FXML
    private void incrementLikes(ActionEvent event) {
        if (!likesTextField.getText().isEmpty()) {
            int currentLikes = Integer.parseInt(likesTextField.getText());
            setLikesTextField(currentLikes + 1);
            updateLikes(currentLikes + 1);
        }
    }
    @FXML
    private void decrementLikes(ActionEvent event) {
        if (!likesTextField.getText().isEmpty()) {
            int currentLikes = Integer.parseInt(likesTextField.getText());
            if (currentLikes > 0) {
                setLikesTextField(currentLikes - 1);
                updateLikes(currentLikes - 1);
            }
        }
    }
    @FXML
    private void incrementDislikes(ActionEvent event) {
        if (!dislikesTextField.getText().isEmpty()) {
            int currentDislikes = Integer.parseInt(dislikesTextField.getText());
            setDislikesTextField(currentDislikes + 1);
            updateDislikes(currentDislikes + 1);
        }
    }
    @FXML
    private void decrementDislikes(ActionEvent event) {
        if (!dislikesTextField.getText().isEmpty()) {
            int currentDislikes = Integer.parseInt(dislikesTextField.getText());
            if (currentDislikes > 0) {
                setDislikesTextField(currentDislikes - 1);
                updateDislikes(currentDislikes - 1);
            }
        }
    }
    private void updateLikes(int newLikes) {
        try {
            // Récupérer le titre du post à mettre à jour
            String titre = titreTextField.getText();

            // Mettre à jour les likes dans la base de données
            postService.updateLikes(titre, newLikes);

        } catch (SQLException e) {
            afficherErreur("Une erreur s'est produite lors de la mise à jour des likes : " + e.getMessage());
        }
    }
    private void updateDislikes(int newDislikes) {
        try {
            // Récupérer le titre du post à mettre à jour
            String titre = titreTextField.getText();

            // Mettre à jour les dislikes dans la base de données
            postService.updateDislikes(titre, newDislikes);

        } catch (SQLException e) {
            afficherErreur("Une erreur s'est produite lors de la mise à jour des dislikes : " + e.getMessage());
        }
    }

    @FXML
    private void exporterEnPDF(ActionEvent event)
    {
        //fonction exporter pdf dans le dossier documents
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
    /*@FXML
    public void initialize() {
        try {
            List<String> titresPosts = postService.getAllTitres();
            postListView.getItems().addAll(titresPosts);

            postListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    Post selectedPost = postService.getByTitre(newValue);
                    setTitreTextField(selectedPost.getTitre());
                    setContenuTextField(selectedPost.getContenu_pub());
                    setDateTextField(selectedPost.getDate_pub());
                    setFichierTextField(selectedPost.getFile());
                    setLikesTextField(selectedPost.getLikes());
                    setDislikesTextField(selectedPost.getDislikes());
                    selectPost(selectedPost.getTitre());
                } catch (SQLException e) {
                    afficherErreur("Une erreur s'est produite lors de la récupération des détails du post : " + e.getMessage());
                }
            });
        } catch (SQLException e) {
            afficherErreur("Une erreur s'est produite lors du chargement des titres des posts : " + e.getMessage());
        }
    }*/
    @FXML
    private void addComment(ActionEvent event)
    {
        // Récupérer le titre du post sélectionné
        String titrePost = selectedPostTitle;
        if (titrePost != null && !titrePost.isEmpty())
        {
            // Vérifier si le champ de commentaire n'est pas vide
            if (commentTextField.getText().isEmpty())
            {
                afficherErreur("Veuillez saisir un commentaire.");
                return;
            }
            try
            {
                // Récupérer le post à partir du titre
                Post post = postService.getByTitre(titrePost);
                // Créer un nouveau commentaire à partir du texte saisi dans le champ de texte
                Comment nouveauCommentaire = new Comment();
                nouveauCommentaire.setContenu_comment(commentTextField.getText());
                // Associer le commentaire au post sélectionné
                nouveauCommentaire.setPost_id(post.getId());
                // Ajouter le commentaire à la base de données
                postService.addComment(post, nouveauCommentaire);

                // Afficher le commentaire ajouté dans commenttf
                commenttf.setText(nouveauCommentaire.getContenu_comment());

                afficherInformation("Le commentaire a été ajouté avec succès.");
                // Effacer le champ de texte du commentaire après l'ajout
                commentTextField.clear();
            }
            catch (SQLException e)
            {
                afficherErreur("Une erreur s'est produite lors de l'ajout du commentaire : " + e.getMessage());
            }
        }
        else
        {
            afficherErreur("Veuillez sélectionner un post.");
        }
    }
    private void selectPost(String titre)
    {
        selectedPostTitle = titre;
        // Mettez en surbrillance visuellement le bouton sélectionné si nécessaire
    }
    public void search(ActionEvent event) {
        String searchText = searchTextField.getText().toLowerCase();

        for (String titre : titresPosts) {
            if (titre.toLowerCase().contains(searchText)) {
                try {
                    Post selectedPost = postService.getByTitre(titre);
                    setTitreTextField(selectedPost.getTitre());
                    setContenuTextField(selectedPost.getContenu_pub());
                    setDateTextField(selectedPost.getDate_pub().toString()); // Adaptation pour afficher la date
                    setFichierTextField(selectedPost.getFile());
                    setLikesTextField(selectedPost.getLikes());
                    setDislikesTextField(selectedPost.getDislikes());
                    // Charger l'image à partir du fichier
                    File file = new File(selectedPost.getFile());
                    if (file.exists()) {
                        Image image = new Image(file.toURI().toString());
                        setImage(image);
                    } else {
                        // Gérer le cas où le fichier image n'existe pas
                        setImage(null);
                    }
                    selectPost(selectedPost.getTitre());
                    return; // Sortir de la boucle si un post correspondant est trouvé
                } catch (SQLException e) {
                    afficherErreur("Une erreur s'est produite lors de la récupération des détails du post : " + e.getMessage());
                }
            }
        }

        afficherErreur("Aucun post correspondant n'a été trouvé.");
    }

    @FXML
    private TextArea titreTextArea;
    private List<String> titresPosts;
    private int currentIndex = 0;
    @FXML
    public void initialize() {
        try {
            titresPosts = postService.getAllTitres();
            afficherPostCourant();
        } catch (SQLException e) {
            afficherErreur("Une erreur s'est produite lors du chargement des titres des posts : " + e.getMessage());
        }
    }
    public void afficherPostCourant() {
        if (!titresPosts.isEmpty()) {
            String titre = titresPosts.get(currentIndex);
            titreTextArea.setText(titre);

            try {
                Post selectedPost = postService.getByTitre(titre);
                setTitreTextField(selectedPost.getTitre());
                setContenuTextField(selectedPost.getContenu_pub());
                setDateTextField(selectedPost.getDate_pub().toString()); // Adaptation pour afficher la date
                setFichierTextField(selectedPost.getFile());
                setLikesTextField(selectedPost.getLikes());
                setDislikesTextField(selectedPost.getDislikes());

                // Charger l'image à partir du fichier
                File file = new File(selectedPost.getFile());
                if (file.exists()) {
                    Image image = new Image(file.toURI().toString());
                    setImage(image);
                } else {
                    // Gérer le cas où le fichier image n'existe pas
                    setImage(null);
                }

                selectPost(selectedPost.getTitre());
            } catch (SQLException e) {
                afficherErreur("Une erreur s'est produite lors de la récupération des détails du post : " + e.getMessage());
            }
        } else {
            titreTextArea.setText("");
            // Effacer les autres champs d'affichage des détails
        }
    }
    @FXML
    private void supprimerCommentaire(ActionEvent event) {
        // Récupérer le contenu du commentaire à supprimer
        String commentaire = commenttf.getText();

        try {
            // Récupérer le post associé au commentaire
            Post post = postService.getByTitre(selectedPostTitle);
            // Récupérer le commentaire à partir de son contenu
            List<Comment> comments = postService.getCommentsByContenu(commentaire);
            // Vérifier si le commentaire existe pour le post
            if (!comments.isEmpty()) {
                // Demander confirmation à l'utilisateur avant de supprimer le commentaire
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Êtes-vous sûr de vouloir supprimer ce commentaire ?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    // Si l'utilisateur confirme, supprimer le commentaire
                    postService.deleteComment(comments.get(0).getId());
                    afficherInformation("Le commentaire a été supprimé avec succès.");
                    // Effacer le champ de texte du commentaire après la suppression
                    commenttf.clear();
                }
            } else {
                afficherErreur("Le commentaire sélectionné n'existe pas.");
            }
        } catch (SQLException e) {
            afficherErreur("Une erreur s'est produite lors de la suppression du commentaire : " + e.getMessage());
        }
    }


    @FXML
    private void postSuivant(ActionEvent event) {
        if (currentIndex < titresPosts.size() - 1) {
            currentIndex++;
            afficherPostCourant();
        }
    }

    @FXML
    private void postPrecedent(ActionEvent event) {
        if (currentIndex > 0) {
            currentIndex--;
            afficherPostCourant();
        }
    }
}