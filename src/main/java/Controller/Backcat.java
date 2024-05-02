package Controller;

import Entity.CategorieJeux;
import Entity.Game;
import Outil.DataBase;
import Service.CategorieJeux_s;
import Service.Game_s;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.text.html.ImageView;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Backcat {


    @FXML
    private Button catjeuxbtn;


    @FXML
    private Containercategorie containercategorie; // Référence au contrôleur Containercategorie

    @FXML
    private Button catupdateBtn;

    @FXML
    private Button close;

    @FXML
    private Label errormsg;

    @FXML
    private AnchorPane jeuxback;

    @FXML
    private Button jeuxbtn;

    @FXML
    private AnchorPane jeuxform;




    @FXML
    private TextField tfdesccat;

    @FXML
    private TextField tfnomcat;

    @FXML
    private VBox categorieslayout;
    private CategorieJeux categories;
    private boolean catView = false;
    //etablir la connexion de base donnée
    Connection conn = DataBase.getInstance().getConn();
    CategorieJeux_s sb = new CategorieJeux_s(conn);

    @FXML
    void addcat(ActionEvent event) throws SQLException, IOException {
        String name = tfnomcat.getText().trim();
        String description = tfdesccat.getText().trim();


        if(name.isEmpty()){

            tfnomcat.setStyle("-fx-border-color: red");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir le nom");
            alert.showAndWait();
            return;
        }
        // Vérifier la longueur des champs titreTextField et contenuTextField
        if (name.length() < 2 || name.length() > 15 ||
                description.length() < 2 || description.length() > 100)
        {
            afficherErreur("La taille du t doit être entre 2 et 15 caractères et du descr doit être entre 2 et 350 caractères.");
            return;
        }
        CategorieJeux b = new CategorieJeux(name, description);
        sb.add(b);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Categorie ajoutée avec succès.");
        alert.show();

        tfdesccat.clear();
        tfnomcat.clear();
        Parent root = FXMLLoader.load(getClass().getResource("/Backcat.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    void affcat(ActionEvent event) throws IOException {
        catView = true;

        Parent root = FXMLLoader.load(getClass().getResource("/Backcat.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();


    }
    private void chargerValeursDeContainercategorie() {
        if (containercategorie != null) {
            String nomCat = containercategorie.getShowNameCat();
            String descCat = containercategorie.getShowDescCat();
            // Utilisez ces valeurs comme nécessaire dans Backcat
        }
    }




    @FXML
    void  affjeuxpage(ActionEvent event)
            throws IOException {
        catView = true;

        Parent root = FXMLLoader.load(getClass().getResource("/showGame2.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();


    }

    @FXML
    private void empty(ActionEvent event){
        tfdesccat.clear();
        tfnomcat.clear();
    }



    @FXML
    void suppcat(ActionEvent event) {


String name = tfnomcat.getText();

 Game_s gs = new Game_s();

CategorieJeux_s cj = new CategorieJeux_s();

        try {
            CategorieJeux c= cj.getByName(name);
            gs.delete(c.getId());
            cj.deleteByName(name);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void loadCategorie() {
        try {
            CategorieJeux_s tS = new CategorieJeux_s(conn);
            List<CategorieJeux> resL = tS.getAllCategories();
            categorieslayout.getChildren().clear(); // Effacez tout contenu précédent

            // Parcourez chaque catégorie dans la liste
            for (CategorieJeux categorie : resL) {
                // Chargez le fichier FXML du conteneur de catégorie
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/containercategorie.fxml"));
                Parent interfaceRoot = loader.load();

                // Obtenez le contrôleur du conteneur de catégorie
                Containercategorie controller = loader.getController();
                // Configurez le contrôleur avec la catégorie actuelle
                controller.setCategorie(categorie);

                // Ajoutez le conteneur de catégorie au VBox
                categorieslayout.getChildren().add(interfaceRoot);

                // Ajoutez un espace vertical entre chaque catégorie (optionnel)
                categorieslayout.getChildren().add(new Separator());
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();

        }
    }
    private void afficherErreur(String message)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }
    // Méthode pour afficher les données de la catégorie sélectionnée dans les champs de texte
    // Méthode pour afficher les données de la catégorie sélectionnée dans les champs de texte
    // Méthode pour afficher les données de la catégorie sélectionnée dans les champs de texte
    public void showEditForm(CategorieJeux categorie) {
        tfnomcat.setText(categorie.getNomCat());
        tfdesccat.setText(categorie.getDescription());
        // Passer la catégorie sélectionnée au Containercategorie pour l'édition
        containercategorie.setCategorie(categorie);
    }






    @FXML
    void initialize() {
        loadCategorie();
    }

}
