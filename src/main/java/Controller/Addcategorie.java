package Controller;

import Entity.CategorieJeux;
import Outil.DataBase;
import Service.CategorieJeux_s;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class Addcategorie {

    @FXML
    private TextField descriptionTextField;

    @FXML
    private TextField nomTextField;

    @FXML
    void addCategorie(ActionEvent event) {

    }
    Connection conn= DataBase.getInstance().getConn() ;
    CategorieJeux_s categorieService = new CategorieJeux_s(conn);
    @FXML
    void ajouterCategorie(ActionEvent event)
    {
        // Vérifier la longueur des champs titreTextField et contenuTextField
        if (nomTextField.getText().length() < 2 || nomTextField.getText().length() > 15 ||
                descriptionTextField.getText().length() < 2 || descriptionTextField.getText().length() > 350)
        {
            afficherErreur("La taille du t doit être entre 2 et 15 caractères et du descr doit être entre 2 et 350 caractères.");
            return;
        }

        // Vérifier si les champs obligatoires sont remplis
        if (nomTextField.getText().isEmpty() || descriptionTextField.getText().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Veuillez remplir tous les champs obligatoires.");
            alert.show();
            return;
        }


        CategorieJeux categorieJeux = new CategorieJeux(nomTextField.getText(), descriptionTextField.getText());

        try
        {
            //add
            categorieService.add(categorieJeux);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("categorie ajouté avec succés");
            alert.show();

            //show
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherPost.fxml"));
            try
            {
                Parent root = loader.load();
                AfficherGame afficherGame = loader.getController();
                afficherGame.setNameTextField(nomTextField.getText());
                afficherGame.setDescriptionTextField(descriptionTextField.getText());


                nomTextField.getScene().setRoot(root);
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

}