package com.ldal.pigeonapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingsScene
{
    @FXML
    private TextField currentpassword;
    @FXML
    private TextField newpassword;
    @FXML
    private TextField newpasswordconf;
    PassHasher passHasher = new PassHasher();
    //SQLServer sqlServer = new SQLServer();
    @FXML
    private Label warner;
    @FXML
    private TextField gmailer;
    @FXML
    private TextField verificationCoder;
    @FXML
    private Label warning;
    private boolean hasSentCode = false;
    String confcode;
    String error;
    String recoverypass;

    @FXML
    public void changepassword(ActionEvent event)
    {
        if(SQLServer.instance.validatePassword(Client.getUser().getLogin(), passHasher.hasher(currentpassword.getText())) && newpassword.getText().equals(newpasswordconf.getText()) && User.validatePassword(newpassword.getText()))
        {
            SQLServer.instance.changePassword(passHasher.hasher(newpassword.getText()), Client.getUser().getLogin());
            warner.setStyle("-fx-text-fill: green");
            warner.setText("Password changed successfully");
            Client.setRememberMe(false);
        }
        else
        {
            warner.setStyle("-fx-text-fill: red");
            warner.setText("Invalid Information");
            Client.setRememberMe(Client.isRememberMe());
        }
    }
    @FXML
    public void spamChanger(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(PigeonApplication.class.getResource("/SpamSettingsScene.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    @FXML
    public void labels(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(PigeonApplication.class.getResource("/CustomLabelsScene.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    @FXML
    public void Backtomenu(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(PigeonApplication.class.getResource("/EmailSelector.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {throw new RuntimeException(e);}
    }


    public void codeSender(ActionEvent event)
    {
        if(gmailer.getText().isEmpty())
        {
            WarnerClass.WarnerError(warning, "Please input information", false);
        }
        else
        {
            new Thread(() ->
            {
                confcode = passHasher.backuppassword();
                GMAILEmailsender.EmailSender(gmailer.getText(), "", confcode, 1, "");
                hasSentCode = true;
            }).start();

            WarnerClass.WarnerError(warning, "If your gmail is valid you should receiver your code", true);
        }
    }

    public void linkUp(ActionEvent event)
    {
        if(!hasSentCode)
        {
            WarnerClass.WarnerError(warner, "Please send the code first", false);
        }
        else
        {
            if(confcode.equals(verificationCoder.getText()))
            {
                new Thread(() ->
                {
                    SQLServer.instance.setGmail(gmailer.getText(), Client.getUser().getLogin());
                    GMAILEmailsender.EmailSender(gmailer.getText(), "", "", 2, Client.getUser().getLogin());
                }).start();
                WarnerClass.WarnerError(warning, "Your gmail is linked up!", true);
            }
            else
            {
                WarnerClass.WarnerError(warning, "Verification code is incorrect", false);
            }
        }
    }
}
