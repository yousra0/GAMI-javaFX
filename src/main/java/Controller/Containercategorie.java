package Controller;
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
import java.util.ResourceBundle;

public class Containercategorie  {



    @FXML
    private Label showdescatTF;

    @FXML
    private ImageView showimgcat;

    @FXML
    private Label shownamecatTF;

    @FXML
    private CategorieJeux categories;


    private final Connection conn = DataBase.getInstance().getConn();
    private final CategorieJeux_s categorieJeuxS = new CategorieJeux_s(conn);

    public void setCategorie(CategorieJeux c) {
        this.categories = c;
        shownamecatTF.setText(c.getNomCat());
        showdescatTF.setText(c.getDescription());
    }


}
