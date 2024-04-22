package Controller;

import Entity.Reclamation;
import Entity.Reponse;
import Outil.DataBase;
import Service.Reclamation_s;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AfficherReclamation
{
    private int currentIndex = 0;

    private List<String> titresReclamations;
    private List<Reclamation> Reclamations;
    private String selectedReclamtionTitle;

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

    public AfficherReclamation()
    {
        Connection cnx = DataBase.getInstance().getConn();
        reclamationService = new Reclamation_s(cnx);
    }

    Connection cnx = DataBase.getInstance().getConn();
    Reclamation_s reclamationService = new Reclamation_s(cnx);

    @FXML
    private void supprimerReclamation()
    {
        // Récupérer le titre du reclamation à supprimer
        String titre = titretf .getText();

        try {
            // Supprimer la reclamation
            reclamationService.deleteByTitre(titre);
            afficherInformation("La réclamation a été supprimée avec succès.");
        } catch (SQLException e) {
            afficherErreur("Une erreur s'est produite lors de la suppression du réclamation : " + e.getMessage());
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

                    Reclamation selectedReclamation = reclamationService.getByTitre(titre);
                    setTitretf(selectedReclamation.getTitre_rec());
                    setContenutf(selectedReclamation.getContenu_rec());
                    setDatetf(selectedReclamation.getDate_rec());


                    selectReclamation(selectedReclamation.getTitre_rec());
                       } catch (SQLException e) {
                afficherErreur("Une erreur s'est produite lors de la récupération des détails du reclamation : " + e.getMessage());
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
}