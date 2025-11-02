package com.ldal.pigeonapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EmailDrafter implements Initializable {
    @FXML
    private TextArea emailText;
    @FXML
    private TextField receiverText;
    @FXML
    private TextField subjText;

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
    private Button drafterButton;
    @FXML
    private Button sendButton;

    @FXML
    private Label errorText;

    @FXML
    private VBox sideBarVbox;

    @FXML
    private void goToLabels(ActionEvent actionEvent) throws IOException {
        SideBarController.goToLabels((Button)actionEvent.getSource());
    }

    @FXML
    private AnchorPane carefulAnchorPane;

    private int goToFolder = 0;

    public static Email draft;

    @FXML
    private void exitComposer(ActionEvent actionEvent) throws IOException {
        Button clickedButton = (Button) actionEvent.getSource();
        if(clickedButton.equals(inboxButton)) goToFolder = 0;
        else if(clickedButton.equals(draftsButton)) goToFolder = 1;
        else if(clickedButton.equals(sentButton)) goToFolder = 2;
        else if(clickedButton.equals(spamButton)) goToFolder = 3;

        String rec = receiverText.getText();
        String subj = subjText.getText();
        String eText = emailText.getText();

        if(!rec.isEmpty() || !subj.isEmpty() || !eText.isEmpty())
        {
            carefulAnchorPane.setDisable(false);
            carefulAnchorPane.setVisible(true);
        }
        else {
            SideBarController.goToSelector((Button)actionEvent.getSource());
        }
    }

    @FXML
    private void draftEmail(ActionEvent actionEvent) throws IOException {
        SQLServer sqlServer = new SQLServer();

        String rec = receiverText.getText();
        String subj = subjText.getText();
        String eText = emailText.getText();

        Email email = new Email();

        email.setSender(Client.getUser());
        if(!subj.isEmpty()) email.setSubject(subj);
        if(!eText.isEmpty()) email.setText(eText);

        Client.emailDrafter(email);

        goToSelector(actionEvent);
    }

    @FXML
    private void keepWriting(ActionEvent actionEvent){
        carefulAnchorPane.setDisable(true);
        carefulAnchorPane.setVisible(false);
    }

    @FXML
    private void goToSelector(ActionEvent actionEvent) throws IOException {
        if(goToFolder == 0) SideBarController.goToSelector(inboxButton);
        if(goToFolder == 1) SideBarController.goToSelector(draftsButton);
        if(goToFolder == 2) SideBarController.goToSelector(sentButton);
        if(goToFolder == 3) SideBarController.goToSelector(spamButton);
    }

    @FXML
    private void sendEmail(ActionEvent actionEvent){
        SQLServer sqlServer = new SQLServer();

        String rec = receiverText.getText();
        String subj = subjText.getText();
        String eText = emailText.getText();

        if(!sqlServer.validateUser(rec)){
            errorText.setText("*User not found!");
            return;
        }
        if(subj.length() > 75){
            errorText.setText("*Subject max length(75) exceeded!");
            return;
        }
        if(eText.length() > 75000){
            errorText.setText("*Text max length(75k) exceeded!");
            return;
        }
        Email email = new Email(Client.getUser(), sqlServer.userFromEmail(rec), eText, subj);


        Client.emailSender(email);
        errorText.setText("*Email sent!");
        receiverText.setText("");
        subjText.setText("");
        emailText.setText("");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SideBarController.inboxButton = inboxButton;
        SideBarController.draftsButton = draftsButton;
        SideBarController.sentButton = sentButton;
        SideBarController.spamButton = spamButton;
        SideBarController.composerButton = composerButton;
        SideBarController.drafterButton = drafterButton;

        sendButton.setStyle("-fx-background-color : #c83f44; -fx-background-radius : 15");
        sendButton.setOnMouseExited(e -> sendButton.setStyle("-fx-background-color : #c83f44; -fx-background-radius : 15"));
        sendButton.setOnMouseEntered(e -> sendButton.setStyle("-fx-background-color : #da4348; -fx-background-radius : 15"));

        SideBarController.initSideBar();

        if(draft != null){
            if(!draft.getText().isEmpty())emailText.setText(draft.getText());
            if(!draft.getSubject().isEmpty())subjText.setText(draft.getSubject());
        }

    }
}
