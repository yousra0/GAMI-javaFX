package nalu.gami2;

import entity.Reclamation;
import entity.Reponse;
import outil.database;
import service.Reclamation_s;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class AfficherReclamation
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


    private Connection conn = database.getInstance().getConn();
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
        Connection cnx = database.getInstance().getConn();
        reclamationService = new Reclamation_s(cnx);
    }

    Connection cnx = database.getInstance().getConn();
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
        statt();

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

                // Récupérer les réponses associées à cette réclamation
                List<Reponse> reponses = reclamationService.getReponsesByReclamationId(selectedReclamation.getId());

                // Afficher les réponses dans l'interface utilisateur
                StringBuilder reponsesText = new StringBuilder();
                for (Reponse reponse : reponses) {
                    reponsesText.append(reponse.getContenu_rep()).append("\n");
                }
                reponsetf.setText(reponsesText.toString());

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
    void statt(){

        // Récupérer toutes les réclamations à l'aide du service de réclamations
        List<Reclamation> reclamations = reclamationService.getAll();

        // Fonction pour calculer les statistiques de réclamation par date
        Map<String, Integer> statistiques = new HashMap<>();

        // Parcourir la liste des réclamations
        for (Reclamation reclamation : reclamations) {
            String date = reclamation.getDate_rec();

            // Vérifier si la date existe déjà dans les statistiques
            if (statistiques.containsKey(date)) {
                // Si la date existe, incrémenter le compteur correspondant
                int count = statistiques.get(date);
                statistiques.put(date, count + 1);
            } else {
                // Si la date n'existe pas, ajouter une nouvelle entrée avec un compteur initial de 1
                statistiques.put(date, 1);
            }
        }


        // Convertir les statistiques en une liste d'entrées
        List<Map.Entry<String, Integer>> statistiquesList = new ArrayList<>(statistiques.entrySet());

        // Créer une liste d'objets PieChart.Data pour le diagramme circulaire
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String, Integer> entry : statistiquesList) {
            String date = entry.getKey();
            int nombreReclamations = entry.getValue();
            pieChartData.add(new PieChart.Data("Date " + date, nombreReclamations));
        }

        // Mettre à jour les données du PieChart avec les statistiques
        pieChartR.setData(pieChartData);
        pieChartR.setTitle("Statistiques par Date de Reclamation");

    }
    @FXML
    private void ExportExcel() {
        // Récupérer toutes les réclamations à partir du service de réclamations
        List<Reclamation> reclamations = reclamationService.getAll();
        // Définir le chemin du fichier Excel de destination

        String filePath = "export.xlsx";
        exportToExcel(reclamations, filePath);
        System.out.println("Données exportées vers Excel.");

    }
    public void exportToExcel(List<Reclamation> reclamations, String filePath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Reclamations");

            // Style pour l'état "En cours de traitement" (noir)
            CellStyle enCoursStyle = workbook.createCellStyle();
            enCoursStyle.setFillForegroundColor(IndexedColors.BLACK.getIndex());
            enCoursStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Style pour l'état "Refusé" (rouge)
            CellStyle refuseStyle = workbook.createCellStyle();
            refuseStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
            refuseStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Style pour l'état "Demande traitée" (vert)
            CellStyle traiteStyle = workbook.createCellStyle();
            traiteStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
            traiteStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // Créer une ligne d'en-tête avec les attributs
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Titre");
            headerRow.createCell(1).setCellValue("contenu");
            headerRow.createCell(2).setCellValue("Date");

            // Parcourir les demandes et les écrire dans le fichier Excel
            int rowNum = 1;
            for (Reclamation demande : reclamations) {
                Row row = sheet.createRow(rowNum++);
                int colNum = 0;
                row.createCell(colNum++).setCellValue(demande.getTitre_rec());
                row.createCell(colNum++).setCellValue(demande.getContenu_rec());
                row.createCell(colNum++).setCellValue(demande.getDate_rec());
                //row.createCell(colNum++).setCellValue(reclamationService.getEmailDirecteur(demande.getDirecteurCampagne()));
                //Cell cell = row.createCell(colNum);
                //cell.setCellValue(demande.getStatut());


            }
            try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                workbook.write(outputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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