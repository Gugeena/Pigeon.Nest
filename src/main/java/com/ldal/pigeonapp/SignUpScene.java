package com.ldal.pigeonapp;

import jakarta.mail.MessagingException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class SignUpScene implements Initializable
{
    private PassHasher passHasher;
    //private SQLServer sqlServer = new SQLServer();

    private User user;
    @FXML
    private Button SignUpbutton;
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private TextField email;
    @FXML
    private Label warning;
    @FXML
    private Button back;
    @FXML
    private TextField recoveryemail;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private AnchorPane linkGmailAnchorPane;
    @FXML
    private TextField gmailer;
    @FXML
    private TextField verificationCoder;
    @FXML
    private Label warner;
    private boolean hasSentCode = false;
    String confcode;
    String error;
    String recoverypass;
    @FXML
    private void SignUpAction(ActionEvent event)
    {
        checkInfo();
    }

    @FXML
    private void Backtomenu(ActionEvent event) throws IOException
    {
        Parent root = FXMLLoader.load(PigeonApplication.class.getResource("/WelcomeScene.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    public void checkInfo()
    {
        String username1 = username.getText();
        String password1 = password.getText();
        String email1 = email.getText();

        if (username1.isEmpty() || password1.isEmpty() || email1.isEmpty())
        {
            WarnerClass.WarnerError(warning, "Please input your data", false);
        }
        else
        {
            if (User.validateEmail(email1) && User.validateLogin(username1) && User.validatePassword(password1) && !SQLServer.instance.checkDuplicateLogin(username1) && !SQLServer.instance.checkDuplicateEmail(email1))
            {
                recoverypass = passHasher.backuppassword();

                String formatted = LocalDateTimer.localDateTime();

                SQLServer.instance.SignUp(username1, email1 + "@pigeon.nest", passHasher.hasher(password1), recoverypass, formatted);
                warning.setText("Successfully signed up, recoverypass: " + recoverypass);
                WarnerClass.WarnerError(warning, "Successfully signed up, recoverypass: " + recoverypass, true);

                linkGmailAnchorPane.setVisible(true);
            }
            else if(!User.validateEmail(email1) || !User.validateLogin(username1))
            {
                if(email1.length() < 4)
                {
                    error = "Email must be over 4 characters long";
                    WarnerClass.WarnerError(warning, "Email must be over 4 characters long", false);
                }
                else if(email1.length() > 20)
                {
                    error = "Email must be under 20 characters long";
                }
                else
                {
                    error = "Email or username contains invalid characters or restricted words";
                }
                WarnerClass.WarnerError(warning, error, false);
            }
            else if(!User.validatePassword(password1))
            {
                String error;
                if(password1.length() < 8)
                {
                    error = "password must be over 8 characters long in login";
                }
                else if(password1.length() > 26)
                {
                    error = "password must be under 26 characters long";
                }
                else
                {
                    error = "Invalid password length";
                }
                WarnerClass.WarnerError(warning, error, false);
            }
            else if(SQLServer.instance.checkDuplicateLogin(username1))
            {
                WarnerClass.WarnerError(warning, "Duplicate login", false);
            }
            else if(SQLServer.instance.checkDuplicateEmail(email1))
            {
                WarnerClass.WarnerError(warning, "Duplicate email", false);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        passHasher = new PassHasher();
        linkGmailAnchorPane.setVisible(false);
    }

    @FXML
    public void gmailTurnerOff(ActionEvent event)
    {
        linkGmailAnchorPane.setVisible(false);
        linkGmailAnchorPane.setDisable(true);
    }

    public void codeSender(ActionEvent event)
    {
        if(gmailer.getText().isEmpty())
        {
            WarnerClass.WarnerError(warner, "Please input information", false);
        }
        else
        {
            WarnerClass.WarnerError(warner, "Processing your request...", true);
            new Thread(() ->
            {
                confcode = passHasher.backuppassword();
                GMAILEmailsender.EmailSender(gmailer.getText(), "", confcode, 1, "");
                hasSentCode = true;

                Platform.runLater(() ->
                {
                    if (GMAILEmailsender.haswentthrough)
                        WarnerClass.WarnerError(warner, "If your gmail is valid you should receive your code", true);
                    else
                        WarnerClass.WarnerError(warner, "Please try again later", false);
                });
            }).start();
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
                    SQLServer.instance.setGmail(gmailer.getText(), username.getText());
                    GMAILEmailsender.EmailSender(gmailer.getText(), recoverypass, "", 0, username.getText());
                }).start();
                WarnerClass.WarnerError(warner, "Your gmail is linked up!", true);
            }
            else if(verificationCoder.getText().isEmpty())
            {
                WarnerClass.WarnerError(warner, "Please input the verification code", false);
            }
            else
            {
                WarnerClass.WarnerError(warner, "Verification code is incorrect", false);
            }
        }
    }
}
