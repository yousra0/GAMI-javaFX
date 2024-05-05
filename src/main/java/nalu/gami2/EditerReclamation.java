package nalu.gami2;

import entity.Reclamation;
import outil.database;
import service.Reclamation_s;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDate;

public class EditerReclamation {

    @FXML
    private TextField contenutf;
    @FXML
    private DatePicker datetf;
    @FXML
    private TextField titretf;

    private Reclamation reclamation;

    public void setTitre(String titre) {
        titretf.setText(titre);
    }

    public void setContenu(String contenu) {
        contenutf.setText(contenu);
    }

    public void setDate(String date) {
        datetf.setValue(LocalDate.parse(date));
    }

    public void retournerAAfficherReclamation(ActionEvent actionEvent) {
        Stage stage = (Stage) titretf.getScene().getWindow();
        stage.close();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherReclamation.fxml"));
        try {
            Parent root = loader.load();
            Stage afficherPostStage = new Stage();
            afficherPostStage.setScene(new Scene(root));
            afficherPostStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setReclamation(Reclamation reclamation) {
        if (reclamation == null) {
            throw new IllegalArgumentException("reclamation ne peut pas être null");
        }
        this.reclamation = reclamation;
        titretf.setText(reclamation.getTitre_rec());
        contenutf.setText(reclamation.getContenu_rec());
        datetf.setValue(LocalDate.parse(reclamation.getDate_rec()));
    }

    Connection conn = database.getInstance().getConn();
    Reclamation_s reclamationService = new Reclamation_s(conn);

    @FXML
    public void enregistrer(ActionEvent actionEvent) {
        conn = database.getInstance().getConn();
        reclamationService = new Reclamation_s(conn);
        String titre = titretf.getText();
        String contenu = contenutf.getText();
        LocalDate date = datetf.getValue();
        Reclamation reclamation = new Reclamation();
        reclamation.setTitre_rec(titre);
        reclamation.setContenu_rec(contenu);
        reclamation.setDate_rec(date.toString());
        try {
            reclamationService.editByTitre(reclamation); // Mettre à jour le post dans la base de données
            afficherInformation("Les modifications ont été enregistrées avec succès.");
            Stage stage = (Stage) titretf.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            afficherErreur("Une erreur s'est produite lors de l'enregistrement des modifications : " + e.getMessage());

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
