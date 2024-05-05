package nalu.gami2;

import entity.Reclamation;
import org.controlsfx.control.Notifications;
import outil.database;
import service.Reclamation_s;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.controlsfx.control.Notifications.create;

public class AjouterReclamation {

    @FXML
    private TextField contenuTextField;

    @FXML
    private DatePicker dateTextField;

    @FXML
    private TextField titreTextField;

    Connection cnx = database.getInstance().getConn();
    Reclamation_s reclamationService = new Reclamation_s(cnx);

    @FXML
    void AjouterReclamation(ActionEvent event)
    {
        // Vérifier la longueur des champs titreTextField et contenuTextField
        if (titreTextField.getText().length() < 2 || titreTextField.getText().length() > 20 ||
                contenuTextField.getText().length() < 2 || contenuTextField.getText().length() > 350)
        {
            afficherErreur("La taille du titre doit être entre 2 et 20 caractères et du contenu doit être  entre 2 et 350 caractères.");

            return;
        }

        // Vérifier si les champs obligatoires sont remplis
        if (titreTextField.getText().isEmpty() || contenuTextField.getText().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Veuillez remplir tous les champs obligatoires.");
            alert.show();
            return;
        }



        // Récupérer la date actuelle du système
        LocalDate currentDate = LocalDate.now();
        Date date = Date.valueOf(currentDate);


        // Format the selected date into a string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateString = currentDate.format(formatter);

        Reclamation reclamation = new Reclamation(titreTextField.getText(), contenuTextField.getText(), dateString);

        try {
            //add
            reclamationService.add(reclamation);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("réclamation ajoutée avec succés");
            alert.show();

            //show
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AfficherReclamation.fxml"));
            try {
                Parent root = loader.load();
                AfficherReclamation afficherReclamation = loader.getController();
                afficherReclamation.setTitretf(titreTextField.getText());
                afficherReclamation.setContenutf(contenuTextField.getText());
                afficherReclamation.setDatetf(dateString);

                titreTextField.getScene().setRoot(root);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Show notification
            Notifications.create()
                    .title("Réclamation ajoutée")
                    .text("La réclamation a été ajoutée avec succès.")
                    .showInformation();

        } catch (SQLException e) {
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
    void initialize()
    {}
}
