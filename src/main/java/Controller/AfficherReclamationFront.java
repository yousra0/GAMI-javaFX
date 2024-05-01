package Controller;

import Entity.Reclamation;
import Entity.Reponse;
import Outil.DataBase;
import Service.Reclamation_s;
import com.google.protobuf.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class AfficherReclamationFront
{
    private int currentIndex = 0;

    private List<String> titresReclamations;
    private List<Reclamation> Reclamations;
    private String selectedReclamtionTitle;
    @FXML
    private TextField searchTextField;
    @FXML
    private PieChart pieChartR;
    @FXML
    private TextField contenutf;

    @FXML
    private TextField titretf;
    @FXML
    private TextField reponsetf;
    @FXML
    private TextField datetf;
    @FXML
    private  TextField reponseTextField ;
    private String selectedReclamationTitle;

    private List<Reponse> reponses = new ArrayList<>();
    private Reclamation reclamation;


    private Connection conn = DataBase.getInstance().getConn();
    private Reclamation_s reclamationS = new Reclamation_s(conn);

    public void setContenutf(String contenutf) {
        this.contenutf.setText(contenutf);
    }

    public void setTitretf(String titretf) {
        this.titretf.setText(titretf);
    }

    public void setDatetf(String datetf) {
        this.datetf.setText(datetf);
    }

    public AfficherReclamationFront()
    {
        Connection cnx = DataBase.getInstance().getConn();
        reclamationService = new Reclamation_s(cnx);
    }

    Connection cnx = DataBase.getInstance().getConn();
    Reclamation_s reclamationService = new Reclamation_s(cnx);

    @FXML
    private void supprimerReclamation() {
        // Récupérer le titre de la réclamation à supprimer
        String titre = titretf.getText();

        try {
            // Récupérer la réclamation à partir du titre
            Reclamation reclamation = reclamationService.getByTitre(titre);

            // Afficher une boîte de dialogue de confirmation
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Supprimer la réclamation");
            alert.setContentText("Êtes-vous sûr de vouloir supprimer cette réclamation ?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Récupérer tous les reponses associés à cette réclamation
                List<Reponse> reponses = reclamationService.getReponsesByReclamationId(reclamation.getId());

                // Supprimer chaque commentaire associé à cette réclamation
                for (Reponse reponse : reponses) {
                    reclamationService.deleteReponse(reponse.getId());
                }

                // Enfin, supprimer la réclamation elle-même
                reclamationService.deleteByTitre(titre);

                afficherInformation("La réclamation et ses commentaires ont été supprimés avec succès.");

                // Rafraîchir la scène pour afficher les changements
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherReclamation.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) titretf.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            }
        } catch (SQLException | IOException e) {
            afficherErreur("Une erreur s'est produite lors de la suppression de la réclamation : " + e.getMessage());
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
    private void addReponse(ActionEvent event) {
        // Récupérer le titre du post sélectionné
        String titreReclamation = selectedReclamationTitle;
        if (titreReclamation != null && !titreReclamation.isEmpty()) {
            // Vérifier si le champ de reponse n'est pas vide
            if (reponseTextField.getText().isEmpty()) {
                afficherErreur("Veuillez saisir une reponse.");
                return;
            }
            try {
                // Récupérer l reclamation à partir du titre
                Reclamation reclamation = reclamationService.getByTitre(titreReclamation);
                // Créer un nouveau reponse à partir du texte saisi dans le champ de texte
                Reponse nouveauReponse = new Reponse();
                nouveauReponse.setContenu_rep(reponseTextField.getText());
                // Associer la reponse au reclamation sélectionné
                nouveauReponse.setreclamation_id(reclamation.getId());
                // Ajouter l reponse à la base de données
                reclamationService.addReponse(reclamation, nouveauReponse);

                // Afficher la reponse ajouté dans commenttf
                reponsetf.setText(nouveauReponse.getContenu_rep());

                afficherInformation("La reponse a été ajouté avec succès.");
                // Effacer le champ de texte du commentaire après l'ajout
                reponseTextField.clear();
            } catch (SQLException e) {
                afficherErreur("Une erreur s'est reclamation lors de l'ajout du reponse : " + e.getMessage());
            }
        } else {
            afficherErreur("Veuillez sélectionner un reclamation.");
        }
    }
    private void selectReclamation(String titre_rec)
    {
        selectedReclamationTitle = titre_rec;
        // Mettez en surbrillance visuellement le bouton sélectionné si nécessaire
    }
    @FXML
    public void initialize() {

        try {

            titresReclamations = reclamationService.getAllTitres();
            afficherReclamationCourant();
        } catch (SQLException e) {
            afficherErreur("Une erreur s'est produite lors du chargement des titres des posts : " + e.getMessage());
        }
    }
    public void afficherReclamationCourant() {
        if (!titresReclamations.isEmpty()) {
            String titre = titresReclamations.get(currentIndex);
            titretf.setText(titre);

            try {
                // Récupérer la réclamation sélectionnée
                Reclamation selectedReclamation = reclamationService.getByTitre(titre);

                // Afficher les détails de la réclamation
                setTitretf(selectedReclamation.getTitre_rec());
                setContenutf(selectedReclamation.getContenu_rec());
                setDatetf(selectedReclamation.getDate_rec());

                // Récupérer les réponses associées à cette réclamation
                List<Reponse> reponses = reclamationService.getReponsesByReclamationId(selectedReclamation.getId());

                // Afficher les réponses dans le champ prévu à cet effet
                StringBuilder reponsesText = new StringBuilder();
                for (Reponse reponse : reponses) {
                    reponsesText.append(reponse.getContenu_rep()).append("\n");
                }
                reponsetf.setText(reponsesText.toString());

                // Mettre à jour la réclamation sélectionnée
                selectReclamation(selectedReclamation.getTitre_rec());
            } catch (SQLException e) {
                afficherErreur("Une erreur s'est produite lors de la récupération des détails de la réclamation : " + e.getMessage());
            }
        } else {
            titretf.setText("");
            // Effacer les autres champs d'affichage des détails
        }
    }


    @FXML
    private void retournerAjouterReclamation()
    {
        Stage stage = (Stage) titretf.getScene().getWindow(); // Récupérer la fenêtre actuelle
        stage.close(); // Fermer la fenêtre actuelle

        // Ouvrir la page AjouterReclamation.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterReclamation.fxml"));
        try {
            Parent root = loader.load();
            Stage ajouterReclamationStage = new Stage();
            ajouterReclamationStage.setScene(new Scene(root));
            ajouterReclamationStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }




    }

    public void supprimerReponse(ActionEvent actionEvent) {
        String reponse = reponsetf.getText();

        try {
            // Récupérer la réclamation associée à la réponse
            Reclamation reclamation = reclamationService.getByTitre(selectedReclamationTitle);
            // Récupérer la réponse à partir de son contenu
            List<Reponse> reponses = reclamationService.getReponsesByContenu(reponse);
            // Vérifier si la réponse existe pour la réclamation
            if (!reponses.isEmpty()) {
                // Demander confirmation à l'utilisateur avant de supprimer la réponse
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Êtes-vous sûr de vouloir supprimer cette réponse ?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    // Si l'utilisateur confirme, supprimer la réponse
                    reclamationService.deleteReponse(reponses.get(0).getId());
                    afficherInformation("La réponse a été supprimée avec succès.");
                    // Effacer le champ de texte de la réponse après la suppression
                    reponsetf.clear();
                }
            } else {
                afficherErreur("La réponse sélectionnée n'existe pas.");
            }
        } catch (SQLException e) {
            afficherErreur("Une erreur s'est produite lors de la suppression de la réponse : " + e.getMessage());
        }

    }

    public void Stat(ActionEvent actionEvent) {
    }

    public void search(ActionEvent actionEvent) {
        String searchText = searchTextField.getText().toLowerCase();
        for (String titre : titresReclamations) {
            if (titre.toLowerCase().contains(searchText)) {
                try {
                    Reclamation selectedReclamation = reclamationService.getByTitre(titre);
                    setTitretf(selectedReclamation.getTitre_rec());
                    setContenutf(selectedReclamation.getContenu_rec());
                    setDatetf(selectedReclamation.getDate_rec().toString()); // Adaptation pour afficher la date
                    //selectedReclamation(selectedReclamation.getTitre_rec());
                    return;
                } catch (SQLException e) {
                    afficherErreur("Une erreur s'est produite lors de la récupération des détails du reclamation : " + e.getMessage());
                }
            }

        }
        afficherErreur("Aucun post correspondant n'a été trouvé.");

    }
    @FXML
    void editerReclamation(ActionEvent event) throws IOException {
        String titre = titretf.getText();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditerReclamation.fxml"));
        Parent root = loader.load();
        EditerReclamation editerReclamationController = loader.getController();

        try {
            editerReclamationController.setTitre(titre);
            editerReclamationController.setContenu(contenutf.getText());
            editerReclamationController.setDate(datetf.getText());
        } catch (NumberFormatException e) {
            afficherErreur("Veuillez saisir des valeurs numériques pour les champs likes et dislikes.");
            return;
        }

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
        stage.show();


    }





    @FXML
    private void reclamationSuivant(ActionEvent event) {
        if (currentIndex < titresReclamations.size() - 1) {
            currentIndex++;
            afficherReclamationCourant();
        }
    }

    @FXML
    public void reclamationprecedent(ActionEvent actionEvent) {
        if (currentIndex > 0) {
            currentIndex--;
            afficherReclamationCourant();
        }
    }
}