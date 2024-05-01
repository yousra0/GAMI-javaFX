package Controller;

import Entity.Game;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;

public class PlayGame implements Initializable {

    @FXML
    private WebView webView;

    private Game games;

    @FXML
    private TextField lienTF;

    public void setGame(Game games){
        this.games=games;

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {



    }



    public void loadPage(ActionEvent actionEvent) {
        String link = games.getLien(); // Obtenir le lien depuis un champ de texte, vous pouvez l'ajuster selon vos besoins
        WebEngine engine = webView.getEngine();
        engine.load(link);
    }
}