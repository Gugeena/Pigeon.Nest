package com.ldal.pigeonapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;


import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeScene
{
    @FXML
    private Label warner;

    @FXML
    public void LoginButtonEvent(ActionEvent event) throws IOException
    {
        if(SQLServer.getInstance() != null)
        {
            if (SQLServer.password != null && SQLServer.userName != null && !SQLServer.password.isEmpty() && !SQLServer.userName.isEmpty())
            {
                Parent root = FXMLLoader.load(PigeonApplication.class.getResource("/LoginScene.fxml"));
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
            }
        }
        else
        {
            warner.setStyle("-fx-text-fill: red;");
            warner.setText("Connection Failed");
        }
    }

    @FXML
    public void SignUpButton(ActionEvent event) throws IOException
    {
        if(SQLServer.getInstance() != null)
        {
            if (SQLServer.password != null && SQLServer.userName != null && !SQLServer.password.isEmpty() && !SQLServer.userName.isEmpty())
            {
                Parent root = FXMLLoader.load(PigeonApplication.class.getResource("/SignUpScene.fxml"));
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
            }
            else
            {
                warner.setStyle("-fx-text-fill: red;");
                warner.setText("Connection Failed");
            }
        }
    }

    @FXML
    public void sqlsetuper(ActionEvent event) throws IOException
    {
        Parent root = FXMLLoader.load(PigeonApplication.class.getResource("/SqlSetupScene.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }


}
