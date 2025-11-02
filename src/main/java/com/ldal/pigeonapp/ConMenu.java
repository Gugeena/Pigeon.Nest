package com.ldal.pigeonapp;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ConMenu
{
    ToolBarController toolBarController = new ToolBarController();
    @FXML
    public AnchorPane anchorPane;
    @FXML
    public void profilebutton(ActionEvent event)
    {
        toolBarController.ConMenu(event, anchorPane);
    }
    @FXML
    public void settingsbutton(ActionEvent event) throws IOException
    {
        Parent root = FXMLLoader.load(PigeonApplication.class.getResource("/SettingsScene.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }
}
