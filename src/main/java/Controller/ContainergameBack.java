package Controller;

import Entity.Game;
import Outil.DataBase;
import Service.Game_s;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class ContainergameBack {

    @FXML
    private ImageView imagev;

    @FXML
    private Label lienTF;

    @FXML
    private Label nameTF;
    private Game games;
    private final Connection conn = DataBase.getInstance().getConn();
    private final Game_s gamesS = new Game_s(conn);
    public void setTournois(Game g) {
        this.games = g;
        nameTF.setText(g.getName());
        Image img = new Image("file:" + g.getImage());
        imagev.setImage(img);
        lienTF.setText(g.getLien());
    }

    @FXML
    void delete(ActionEvent event) {
        if (games!= null) {
            System.out.println("Deleting Game: " + games.toString());
            try {

                gamesS.delete(games.getId());

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Backgame.fxml"));
                Parent root = loader.load();
                Backgame gameShow = loader.getController();


                // Get the current stage and set the new scene
                Stage stage = (Stage) nameTF.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();

            } catch (SQLException e) {
                e.printStackTrace();

                // Handle any errors or exceptions
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("No Game selected.");
        }

    }

    @FXML
    void edit(ActionEvent event) throws IOException
    {

    }

}
