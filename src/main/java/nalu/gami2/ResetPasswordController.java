package nalu.gami2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import outil.database;
import service.PasswordEncoder;
import service.user_s;

import java.io.IOException;

public class ResetPasswordController {
    @javafx.fxml.FXML
    private PasswordField tfPassword;
    @javafx.fxml.FXML
    private PasswordField tfConfirm;
    @javafx.fxml.FXML
    private Button btnReset;

    @FXML
    private void btnResetAction(ActionEvent event) throws Exception {
        Alert A = new Alert(Alert.AlertType.INFORMATION);
        if (!tfPassword.getText().equals("") && tfPassword.getText().equals(tfConfirm.getText())) {
            user_s su = new user_s(database.getInstance().getConn());
            String encrypt = PasswordEncoder.encode(tfPassword.getText());
            su.ResetPaswword(ForgotPasswordController.EmailReset, encrypt);
            A.setContentText("Mot de passe modifié avec succes ! ");
            A.show();
            try {

                Parent page1 = FXMLLoader.load(getClass().getResource("sign-in.fxml"));

                Scene scene = new Scene(page1);

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                stage.setScene(scene);

                stage.show();

            } catch (IOException ex) {

                System.out.println(ex.getMessage());

            }
        } else {
            A.setContentText("veuillez saisir un mot de passe conforme !");
            A.show();
        }

    }

}
