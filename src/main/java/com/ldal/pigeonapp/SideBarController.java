package com.ldal.pigeonapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SideBarController {
    public static Button inboxButton;
    public static Button draftsButton;
    public static Button sentButton;
    public static Button spamButton;
    public static Button composerButton;
    public static Button drafterButton;
    public static Button configureButton;
    public static Button clearerButton;
    public static VBox labelsVbox;

    public static ArrayList<Button> labelButtons;

    public static void goToSelector(Button clickedButton) throws IOException
    {
        if(clickedButton.equals(inboxButton))
        {
            EmailSelector.folderID = 0;
        }
        else if(clickedButton.equals(draftsButton))
        {
            EmailSelector.folderID = 1;
        }
        else if(clickedButton.equals(sentButton))
        {
            EmailSelector.folderID = 2;
        }
        else if(clickedButton.equals(spamButton))
        {
            EmailSelector.folderID = 3;
        }
        else{
            int i;
            for (i = 0; i < labelButtons.size(); i++) {
                if (labelButtons.get(i).equals(clickedButton)) break;
            }
            EmailSelector.folderID = 4 + i;

        }

        Parent root = FXMLLoader.load(PigeonApplication.class.getResource("/EmailSelector.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) (clickedButton.getScene().getWindow());
        stage.setScene(scene);

    }

    public static void goToLabels(Button clickedButton) throws IOException {
        Parent root = FXMLLoader.load(PigeonApplication.class.getResource("/CustomLabelsScene.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) (clickedButton.getScene().getWindow());
        stage.setScene(scene);
    }

    public static void goToComposer(ActionEvent actionEvent) throws IOException {
        EmailComposer.draft = null;

        Parent root = FXMLLoader.load(PigeonApplication.class.getResource("/EmailComposer.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) (((Node)(actionEvent.getSource())).getScene().getWindow());
        stage.setScene(scene);
    }

    public static void editDraft(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(PigeonApplication.class.getResource("/EmailComposer.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) (((Node)(actionEvent.getSource())).getScene().getWindow());
        stage.setScene(scene);
    }

    public static void initSideBar(){
        labelButtons = new ArrayList<>();


        for (int c = 0; c < Client.getCustomLabels().size(); c++)
        {
            CustomLabel cl = Client.getCustomLabels().get(c);
            cl.setLabelID(c);
            Client.getCustomLabels().set(c, cl);
        }




        String cssURL = SideBarController.class.getResource("/styleforROBOTO.css").toExternalForm();

        Button[] btn = {inboxButton, draftsButton, sentButton, spamButton, composerButton, configureButton, clearerButton};
        ArrayList<Button> buttons = new ArrayList<>(List.of(btn));
        for (CustomLabel c : Client.getCustomLabels()){
            Button button = new Button();
            button.setText(c.getLabelName());
            button.getStylesheets().add(cssURL);
            button.getStyleClass().add("custom-font-even-smaller");
            button.setOnAction(e -> {
                try {
                    goToSelector((Button) (e.getSource()));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            button.setCursor(Cursor.HAND);

            labelButtons.add(button);

            buttons.add(button);
            labelsVbox.getChildren().add(button);
        }

        for(Button b : buttons){
            b.setStyle("-fx-text-fill: #000000; -fx-background-color: TRANSPARENT");
            b.setOnMouseEntered(event -> b.setStyle("-fx-text-fill: #e17c65; -fx-background-color: TRANSPARENT"));
            b.setOnMouseExited(event -> b.setStyle("-fx-text-fill: #000000; -fx-background-color: TRANSPARENT"));

        }
    }



}
