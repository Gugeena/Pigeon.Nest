package com.ldal.pigeonapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;

public class ToolBarController
{
    public boolean isAlreadyCreated = false;
    private VBox mainhbox;
    Client client = new Client();

    public void ConMenu(ActionEvent event, AnchorPane anchorPane)
    {
        if (!isAlreadyCreated)
        {
            creatingWindow(anchorPane);
            isAlreadyCreated = true;
        }
        else
        {
            if (mainhbox != null)
            {
                anchorPane.getChildren().remove(mainhbox);
                isAlreadyCreated = false;
            }
        }
    }

    public void creatingWindow(AnchorPane anchorPane)
    {
        Circle profileCircle = new Circle(60, Color.CRIMSON);
        Button profileButton = new Button("");
        profileButton.setPrefSize(120, 120);
        profileButton.setStyle("-fx-background-color: transparent; -fx-border-radius: 8px;");
        profileButton.setOnAction(e ->
        {
            try
            {
                Parent root = FXMLLoader.load(PigeonApplication.class.getResource("/ProfileScene.fxml"));
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                stage.setScene(scene);
            }
            catch (IOException ex)
            {
                throw new RuntimeException(ex);
            }
        });
        profileButton.setOnMouseEntered(e -> profileCircle.setFill(Color.web("#B22234")));
        profileButton.setOnMouseExited(e -> profileCircle.setFill(Color.CRIMSON));
        profileButton.setCursor(Cursor.HAND);

        Label username = new Label(Client.getUser().getEmail());
        username.setStyle("-fx-text-fill: crimson; -fx-font-weight: bold; -fx-font-size: 15");
        Label initialText = new Label(String.valueOf(Client.getUser().getLogin().charAt(0)) + String.valueOf(Client.getUser().getLogin().charAt(1)));
        initialText.setTextFill(Color.WHITE);
        initialText.setStyle("-fx-font-size: 25px; -fx-font-weight: bold;");

        StackPane circleWithText = new StackPane(profileCircle, initialText, profileButton);
        circleWithText.setPrefSize(120, 120);

        HBox hbox = new HBox(10);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getChildren().addAll(circleWithText, username);

        hbox.setFocusTraversable(false);
        hbox.setBackground(Background.EMPTY);
        hbox.setStyle("-fx-background-color: transparent;");

        HBox hbox1 = new HBox();
        HBox vBox = new HBox(20);
        Button settingsbutton = new Button("Settings");
        settingsbutton.setOnAction(e ->
        {
            try
            {
                Parent root = FXMLLoader.load(PigeonApplication.class.getResource("/SettingsScene.fxml"));
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                stage.setScene(scene);
            }
            catch (IOException ex)
            {
                throw new RuntimeException(ex);
            }
        });

        Button logOut = new Button("LogOut");
        logOut.setOnAction(e ->
        {
            try
            {
                Client.setRememberMe(false);
                Client.setCustomLabels(new ArrayList<>());
                client.saveSettings();
                Parent root = FXMLLoader.load(PigeonApplication.class.getResource("/WelcomeScene.fxml"));
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                stage.setScene(scene);
            }
            catch (IOException ex)
            {
                throw new RuntimeException(ex);
            }
        });

        settingsbutton.getStylesheets().add("styleforROBOTO.css");
        settingsbutton.getStyleClass().add("custom-font-even-smaller");
        settingsbutton.setStyle("-fx-background-color: #de7259; -fx-font-size: 20px; -fx-text-fill: black; -fx-radius-size: 6px; -fx-background-radius: 10; -fx-opacity: 0.82;");
        settingsbutton.setCursor(Cursor.HAND);
        logOut.getStylesheets().add("styleforROBOTO.css");
        logOut.getStyleClass().add("custom-font-even-smaller");
        logOut.setStyle("-fx-background-color: #de7259; -fx-font-size: 20px; -fx-text-fill: black; -fx-radius-size: 6px; -fx-background-radius: 10; -fx-opacity: 0.82;");
        logOut.setCursor(Cursor.HAND);
        vBox.getChildren().addAll(settingsbutton, logOut);

        CustomMenuItem profileItem = new CustomMenuItem(hbox);
        CustomMenuItem buttons = new CustomMenuItem(hbox1);
        profileItem.setStyle(String.valueOf(Color.BEIGE));

        mainhbox = new VBox(30);
        mainhbox.getChildren().addAll(hbox, vBox);
        mainhbox.setStyle("-fx-background-color: #EDB388;");
        anchorPane.getChildren().add(mainhbox);
        mainhbox.setTranslateX(975);
        mainhbox.setTranslateY(45);

        //contextMenu.getItems().addAll(profileItem, buttons);
        //for (MenuItem item : contextMenu.getItems())
        //{
           // ((CustomMenuItem)item).getContent().setStyle("-fx-min-width: 500px; -fx-min-height: 100px; -fx-padding: 10px;");
        //}

        //contextMenu.setStyle(String.valueOf(Color.BEIGE));
        //contextMenu.setStyle("-fx-background-color: beige;");

        //contextMenu.show((Button) event.getSource(), Side.RIGHT, 400, 0);
    }
}
