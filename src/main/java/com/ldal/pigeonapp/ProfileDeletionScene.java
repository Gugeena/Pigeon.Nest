package com.ldal.pigeonapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.io.IOException;

public class ProfileDeletionScene
{
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField passwordconf;
    @FXML
    private CheckBox iAmSurer;
    @FXML
    private Label warning;
    SQLServer sqlServer = new SQLServer();
    Client client = new Client();
    PassHasher passHasher = new PassHasher();
    @FXML
    public void Backtomenu(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(PigeonApplication.class.getResource("/ProfileScene.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    @FXML
    public void Delete(ActionEvent event) throws IOException
    {
        if(sqlServer.validatePassword(Client.getUser().getLogin(), passHasher.hasher(password.getText())) &&
                password.getText().equals(passwordconf.getText()) && !password.getText().isEmpty() && !passwordconf.getText().isEmpty() && iAmSurer.isSelected())
        {
            sqlServer.deleteAccount(Client.getUser().getLogin());
            Client.setRememberMe(false);
            client.saveSettings();

            Parent root = FXMLLoader.load(PigeonApplication.class.getResource("/WelcomeScene.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        }
        else if(!sqlServer.validatePassword(Client.getUser().getLogin(), passHasher.hasher(password.getText())))
        {
            warning.setText("Invalid password");
            warning.setStyle("-fx-text-fill: red;");
        }
        else if(passwordconf.getText().isEmpty() || password.getText().isEmpty())
        {
            warning.setText("Input info");
            warning.setStyle("-fx-text-fill: red;");
        }
        else if(!password.getText().equals(passwordconf.getText()))
        {
            warning.setText("passwords do not match");
            warning.setStyle("-fx-text-fill: red;");
        }
        else if(!iAmSurer.isSelected())
        {
            warning.setText("You must be sure in order to proceed");
            warning.setStyle("-fx-text-fill: red;");
        }

    }
}
