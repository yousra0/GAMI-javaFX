package nalu.gami2;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import outil.database;
import service.user_s;
import service.EmailSender;


public class ForgotPasswordController {
    @javafx.fxml.FXML
    private TextField emailTF;
    @javafx.fxml.FXML
    public static int code;
    public static String EmailReset ;



    public void send(ActionEvent actionEvent) throws UnsupportedEncodingException {
        code = generateVerificationCode();
        Alert A = new Alert(Alert.AlertType.WARNING);
        user_s su = new user_s(database.getInstance().getConn());

        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        boolean verifMail = emailTF.getText().matches(emailRegex);

        if (!emailTF.getText().equals("") && verifMail) {
            if (su.ChercherMail(emailTF.getText()) == 1) {
                EmailReset = emailTF.getText();
                String appPassword = "nahw pgoq svsc ejea";
             //   EmailSender.sendEmail("mohamedyassinekhlaf@gmail.com", "mamanabiha", emailTF.getText(), "Verification code", "Votre code de verification est : " + code);
                EmailSender.sendEmail("mohamedyassinekhlaf@gmail.com", appPassword, emailTF.getText(), "Verification code", "Votre code de verification est : " + code);

                try {

                    Parent page1 = FXMLLoader.load(getClass().getResource("verif-code.fxml"));

                    Scene scene = new Scene(page1);

                    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

                    stage.setScene(scene);

                    stage.show();

                } catch (IOException ex) {

                    System.out.println(ex.getMessage());

                }

            } else {
                A.setContentText("pas de compte lié avec cette adresse ! ");
                A.show();
            }
        } else {
            A.setContentText("Veuillez saisir une adresse mail valide ! ");
            A.show();
        }
    }


    public static int generateVerificationCode() {
        // Générer un code de vérification aléatoire à 6 chiffres
        Random random = new Random();
        return 100000 + random.nextInt(900000);
    }
}
