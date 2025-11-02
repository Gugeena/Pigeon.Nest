package com.ldal.pigeonapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class ForgottenChanger
{
    @FXML
    private PasswordField backuppassword;
    @FXML
    private PasswordField newpassword;
    @FXML
    private PasswordField newpasswordconf;
    @FXML
    private TextField loginer;
    @FXML
    private Label warner;
    @FXML
    private Label warner1;
    SQLServer sqlServer = new SQLServer();
    PassHasher passHasher = new PassHasher();
    String newRecoverypass;
    @FXML
    private Label warner2;

    @FXML
    public void Backtomenu(ActionEvent event) throws IOException
    {
        Parent root = FXMLLoader.load(PigeonApplication.class.getResource("/WelcomeScene.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    @FXML
    public void changepassword(ActionEvent event)
    {
        if(sqlServer.validateLogin(loginer.getText()) && User.validatePassword(newpassword.getText()) && sqlServer.validateRecoveryPass(loginer.getText(), backuppassword.getText()) && newpasswordconf.getText().equals(newpassword.getText()))
        {
            sqlServer.changePassword(passHasher.hasher(newpassword.getText()), loginer.getText());
            newRecoverypass = passHasher.backuppassword();
            sqlServer.changeRecoverypass(newRecoverypass, loginer.getText());
            WarnerClass.WarnerError(warner, "Password changed successfully", true);
            WarnerClass.WarnerError(warner1, "new recoverypass: " + newRecoverypass, true);
        }
        else if(!sqlServer.validateRecoveryPass(loginer.getText(), backuppassword.getText()))
        {
            WarnerClass.WarnerError(warner, "Invalid recoveryPass", false);
        }
        else
        {
            WarnerClass.WarnerError(warner, "Invalid information", false);
        }
    }

    @FXML
    public void codeSender(ActionEvent event)
    {
        if(loginer.getText().isEmpty())
        {
            WarnerClass.WarnerError(warner2, "Please input information", false);
            return;
        }

        if(!sqlServer.validateLogin(loginer.getText()))
        {
            WarnerClass.WarnerError(warner2, "Invalid user", false);
            return;
        }
        else if(!sqlServer.checkerGmail(loginer.getText()))
        {
            WarnerClass.WarnerError(warner2, "Account does not have gmail linked", false);
            return;
        }
        else if(sqlServer.checkerGmail(loginer.getText()))
        {
            new Thread(() ->
            {
                String newRecoveryPassword = passHasher.backuppassword();
                sqlServer.changeRecoverypass(newRecoveryPassword, loginer.getText());
                GMAILEmailsender.EmailSender(sqlServer.getterGmail(loginer.getText()), newRecoveryPassword, "", 3,"");
            }).start();
            WarnerClass.WarnerError(warner2, "Email Sent", true);
        }
    }
}
