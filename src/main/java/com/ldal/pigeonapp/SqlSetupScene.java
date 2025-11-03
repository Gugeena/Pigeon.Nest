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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class SqlSetupScene
{
    @FXML
    private Label warner;
    @FXML
    public TextField username;
    @FXML
    public PasswordField password;
    @FXML
    public CheckBox remmemberOfSQL;

    @FXML
    public void SetItUp(ActionEvent event)
    {
        if(!username.getText().isEmpty() && !password.getText().isEmpty())
        {
            try
            {
                SQLServer.userName = username.getText();
                SQLServer.password = password.getText();
                //SQLServer sqlServer = new SQLServer();
                WarnerClass.WarnerError(warner, "Connection established", true);
                if(remmemberOfSQL.isSelected())
                {
                    Client.saveSQLInfo();
                }
            }
            catch (Exception e)
            {
                SQLServer.userName = null;
                SQLServer.password = null;
                WarnerClass.WarnerError(warner, "Connection with SQLServer failed", false);
                e.printStackTrace();
            }
        }
        else
        {
            WarnerClass.WarnerError(warner, "Please input information", false);
        }
    }

    @FXML
    private void Backtomenu(ActionEvent event) throws IOException
    {
        Parent root = FXMLLoader.load(PigeonApplication.class.getResource("/WelcomeScene.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }
}
