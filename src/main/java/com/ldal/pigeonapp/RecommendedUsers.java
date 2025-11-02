package com.ldal.pigeonapp;

import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class RecommendedUsers
{
    public void recommendedUsersTab(ArrayList<String> topRecepeints, AnchorPane anchorPane, TextField receiver)
    {
        /*
        vBox = new VBox();
        boolean isAlreadyCreated = false;
        if(!topRecepeints.isEmpty())
        {
            Label declare = new Label("People You Frequently Email: ");
            declare.getStylesheets().add("styleforROBOTO.css");
            declare.getStyleClass().add("custom-font-even-smaller");
            Label label = new Label("");
            vBox.getChildren().addAll(declare, label);
            for (String s : topRecepeints) {
                Button recepeint = new Button(s);
                recepeint.getStylesheets().add("styleforROBOTO.css");
                recepeint.getStyleClass().add("custom-font-even-smaller");
                recepeint.setStyle("-fx-background-color: transparent; -fx-font-size: 18px; -fx-text-fill: black; -fx-radius-size: 6px; -fx-background-radius: 10; -fx-opacity: 0.82; -fx-padding: 0;");
                vBox.getChildren().add(recepeint);
                recepeint.setOnAction(e ->
                {
                    receiver.setText(s);
                });
            }
            vBox.setStyle("-fx-background-color: #EDB388;");
            vBox.setLayoutX(265);
            vBox.setLayoutY(141);
            anchorPane.getChildren().add(vBox);
        }
        else
        {
            System.out.println("top recepeients is empty");
        }
        */


        HBox hbox = new HBox();
        Label declare = new Label("People You Frequently Email: ");
        declare.getStylesheets().add("styleforROBOTO.css");
        declare.getStyleClass().add("custom-font-even-smaller");
        declare.setStyle("-fx-text-fill: #000000");
        hbox.getChildren().add(declare);
        hbox.setStyle("-fx-background-color: #D8A982;");
        hbox.setLayoutX(215);
        hbox.setLayoutY(695);
        for(String s : topRecepeints)
        {
            Button recepeint = new Button(s);
            Label nothing = new Label(" ");
            recepeint.getStylesheets().add("styleforROBOTO.css");
            recepeint.getStyleClass().add("custom-font-even-smaller");
            recepeint.setCursor(Cursor.HAND);
            recepeint.setStyle("-fx-text-fill: #000000; -fx-background-color: transparent; -fx-opacity: 0.82; -fx-padding: 0;");
            recepeint.setOnMouseEntered(event -> recepeint.setStyle("-fx-text-fill: #e17c65; -fx-background-color: transparent; -fx-opacity: 0.82; -fx-padding: 0;"));
            recepeint.setOnMouseExited(event -> recepeint.setStyle("-fx-text-fill: #000000; -fx-background-color: transparent; -fx-opacity: 0.82; -fx-padding: 0;"));
            hbox.getChildren().addAll(recepeint, nothing);
            recepeint.setOnAction(e ->
            {
                if(receiver.getText().isEmpty())
                {
                    receiver.setText(s);
                }
                else
                {
                    receiver.setText(receiver.getText() + ", " + s);
                }
            });
        }
        anchorPane.getChildren().add(hbox);
    }
}
