package com.ldal.pigeonapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EmailReader implements Initializable {
    public static Email email;
    @FXML
    private Label emailText;
    @FXML
    private Label senderText;
    @FXML
    private Label subjText;
    @FXML
    private Label dateText;
    @FXML
    private Label timeText;

    @FXML
    private Button inboxButton;
    @FXML
    private Button draftsButton;
    @FXML
    private Button sentButton;
    @FXML
    private Button spamButton;
    @FXML
    private Button composerButton;
    @FXML
    private Label Declareridk;
    @FXML
    private Button drafterButton;
    ToolBarController toolBarController = new ToolBarController();
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private VBox sideBarVbox;

    @FXML
    private void goToSelector(ActionEvent actionEvent) throws IOException {
        Button clickedButton = (Button) actionEvent.getSource();
        if(clickedButton.equals(inboxButton)) EmailSelector.folderID = 0;
        else if(clickedButton.equals(draftsButton)) EmailSelector.folderID = 1;
        else if(clickedButton.equals(sentButton)) EmailSelector.folderID = 2;
        else if(clickedButton.equals(spamButton)) EmailSelector.folderID = 3;

        SideBarController.goToSelector(clickedButton);

    }

    @FXML
    private void goToComposer(ActionEvent actionEvent) throws IOException {
        SideBarController.goToComposer(actionEvent);
    }

    @FXML
    private void goToLabels(ActionEvent actionEvent) throws IOException {
        SideBarController.goToLabels((Button)actionEvent.getSource());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SideBarController.inboxButton = inboxButton;
        SideBarController.draftsButton = draftsButton;
        SideBarController.spamButton = spamButton;
        SideBarController.sentButton = sentButton;
        SideBarController.composerButton = composerButton;
        SideBarController.drafterButton = drafterButton;

        SideBarController.initSideBar();


        String dateTime = email.getDateTime();
        if(EmailSelector.folderID != 2)
        {
            Declareridk.setText("From: ");
            senderText.setText(email.getSender().getEmail());
        }
        else
        {

            Declareridk.setText("To: ");
            senderText.setText(email.getReceiver().getEmail());
        }
        emailText.setText(email.getText());
        subjText.setText(email.getSubject());
        if(dateTime != null)dateText.setText(dateTime.substring(0,10));
        if(dateTime != null)timeText.setText(dateTime.substring(11));
    }

    @FXML
    private void profilebutton(ActionEvent event)
    {
        toolBarController.ConMenu(event, anchorPane);
    }
}
