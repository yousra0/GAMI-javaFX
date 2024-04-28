package Controller;

import Entity.Game;
import Service.Game_s;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class Jeuxdetails {

    @FXML
    private Button back;

    @FXML
    private VBox containerPane;

    @FXML
    private ImageView imageView;

    @FXML
    private Label rate;

    @FXML
    private Label titleLabel;
    private int id;
    private Game jeux;

    @FXML
    void backbtn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/showGame2.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();

        stage.show();

    }
    public void setJeux(Game jeux) {
        this.jeux = jeux;
        this.id = jeux.getId();

        if (jeux != null) {
            Game_s sj = new Game_s();
            sj.initConnection();
            HBox titleAndRatingContainer = new HBox();
            titleAndRatingContainer.setAlignment(Pos.CENTER_LEFT);
            titleAndRatingContainer.setSpacing(10);

            titleLabel.setText(jeux.getName());



            ImageView filledStarImage = new ImageView(new Image("/images/filled.png"));
            filledStarImage.setFitHeight(25);
            filledStarImage.setFitWidth(25);


            titleAndRatingContainer.getChildren().addAll(titleLabel, filledStarImage);


            ImageView imageView = new ImageView(new Image("/images/valorant-1640045685890.jpg"));
            imageView.setFitHeight(338);
            imageView.setFitWidth(337);
            HBox gameDetailsContainer = new HBox(imageView);

            VBox gameDetailsVBox = new VBox(titleAndRatingContainer);

            HBox ratingContainer = new HBox();
            ratingContainer.setAlignment(Pos.CENTER);
            ratingContainer.setSpacing(5);



            ImageView[] starImages = new ImageView[5];
            for (int i = 0; i < 5; i++) {
                starImages[i] = new ImageView(new Image("/images/empty.png"));
                starImages[i].setFitHeight(25);
                starImages[i].setFitWidth(25);
                ratingContainer.getChildren().add(starImages[i]);


            }



            gameDetailsVBox.getChildren().add(ratingContainer);

            gameDetailsContainer.getChildren().add(gameDetailsVBox);

            VBox blogContainer = new VBox(gameDetailsContainer);
            blogContainer.setAlignment(Pos.CENTER);
            blogContainer.setSpacing(20);

            ScrollPane scrollPane = new ScrollPane(blogContainer);
            scrollPane.setFitToWidth(true);

            containerPane.getChildren().setAll(scrollPane);
        }
    }

}
