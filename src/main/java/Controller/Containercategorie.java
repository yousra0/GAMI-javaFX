package Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


import Entity.CategorieJeux;
import Entity.Game;
import Outil.DataBase;
import Service.CategorieJeux_s;
import Service.Game_s;
import javafx.fxml.FXML;
import javafx.scene.image.Image;

import javax.swing.text.Element;
import javax.swing.text.html.ImageView;
import javafx.scene.image.Image;
import java.awt.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.scene.control.Button;
public class Containercategorie  {



    @FXML
    private Label showdescatTF;



    @FXML
    private Label shownamecatTF;
    @FXML
    private Button editButton;

    @FXML
    private CategorieJeux categories;

    private Backcat backcatController; // Garder une référence au contrôleur Backcat
    public void setBackcatController(Backcat controller) {
        this.backcatController = controller;
    }


    private final Connection conn = DataBase.getInstance().getConn();
    private final CategorieJeux_s categorieJeuxS = new CategorieJeux_s(conn);

    public void setCategorie(CategorieJeux c) {
        this.categories = c;
        shownamecatTF.setText(c.getNomCat());
        showdescatTF.setText(c.getDescription());
    }
    // Autres méthodes de la classe

    public String getShowNameCat() {
        return shownamecatTF.getText();
    }

    public String getShowDescCat() {
        return showdescatTF.getText();
    }




    @FXML
    void editCategory(ActionEvent actionEvent) {
        if (backcatController != null && categories != null) {
            String newName = shownamecatTF.getText().trim(); // Nouveau nom de la catégorie
            String newDescription = showdescatTF.getText().trim(); // Nouvelle description de la catégorie
            if (!newName.isEmpty() && !newDescription.isEmpty()) {
                categories.setNomCat(newName);
                categories.setDescription(newDescription);
                try {
                    categorieJeuxS.edit(categories); // Appel à la méthode de mise à jour dans le service
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                // Rafraîchir l'affichage des catégories dans Backcat
                backcatController.loadCategorie(); // Appel à la méthode loadCategorie() de Backcat
            } else {
                // Afficher un message d'erreur si les champs sont vides
                // Ou fournir une indication à l'utilisateur sur les champs manquants
            }
        }
    }




}
