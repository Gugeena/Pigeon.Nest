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

public class SpamSettingsScene
{
    @FXML
    private TextField userinput;
    @FXML
    private Label warner;
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
    @FXML
    public void private1(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(PigeonApplication.class.getResource("/SettingsScene.fxml"));
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
    public void spamAdd(ActionEvent event)
    {
        String userinput1 = userinput.getText();
        System.out.println(userinput.getText());
        if(!userinput1.isEmpty() && SQLServer.instance.validatePigeonEmail(userinput1) && !Client.getSpamblacklist().contains(userinput1))
        {
            Client.setSpamblacklist(userinput1);
            WarnerClass.WarnerError(warner, userinput1 + " spamlisted", true);
            Client.saveSpamBlacklist();
        }
        else if(!SQLServer.instance.validatePigeonEmail(userinput1))
        {
            WarnerClass.WarnerError(warner, "Invalid user", false);
        }
        else if(userinput1.isEmpty())
        {
            WarnerClass.WarnerError(warner, "Please input information", false);
        }
        else if(Client.getSpamblacklist().contains(userinput1))
        {
            WarnerClass.WarnerError(warner, "User already spamlisted", false);
        }
    }
    @FXML
    public void blockAdd(ActionEvent event)
    {
        String userinput1 = userinput.getText();
        if(!userinput1.isEmpty() && SQLServer.instance.validatePigeonEmail(userinput1) && !Client.getBlockedblacklist().contains(userinput1) && !userinput1.equals(Client.getUser().getEmail()))
        {
            Client.setBlockedblacklist(userinput1);
            WarnerClass.WarnerError(warner, userinput1 + " Blocked", true);
            Client.saveBlocklist();
        }
        else if(!SQLServer.instance.validatePigeonEmail(userinput1))
        {
            WarnerClass.WarnerError(warner, "Invalid user", false);
        }
        else if(userinput1.isEmpty())
        {
            WarnerClass.WarnerError(warner, "Please input information", false);
        }
        else if(userinput1.equals(Client.getUser().getEmail()))
        {
            WarnerClass.WarnerError(warner, "You cant block yourself", false);
        }
        else
        {
            WarnerClass.WarnerError(warner, "User is already blocked", false);
        }
    }
    @FXML
    public void spamRemove(ActionEvent event)
    {
        String userinput1 = userinput.getText();
        if(!userinput1.isEmpty() && SQLServer.instance.validatePigeonEmail(userinput1) && Client.getSpamblacklist().contains(userinput1))
        {
            Client.removeSpamblacklist(userinput1);
            WarnerClass.WarnerError(warner, userinput1 + " removed from spamlist", true);
            Client.saveSpamBlacklist();
        }
        else if(!SQLServer.instance.validateEmail(userinput1))
        {
            WarnerClass.WarnerError(warner, "Invalid user", false);
        }
        else if(!Client.getSpamblacklist().contains(userinput1))
        {
            WarnerClass.WarnerError(warner, "User not in spamlist", false);
        }
    }
    @FXML
    public void blockRemove(ActionEvent event)
    {
        String userinput1 = userinput.getText();
        if(!userinput1.isEmpty() && SQLServer.instance.validatePigeonEmail(userinput1) && Client.getBlockedblacklist().contains(userinput1))
        {
            Client.removeBlockedblacklist(userinput1);
            WarnerClass.WarnerError(warner, userinput1 + " UnBlocked", true);
            Client.saveBlocklist();
        }
        else if(!SQLServer.instance.validatePigeonEmail(userinput1))
        {
            WarnerClass.WarnerError(warner, "Invalid user", false);
        }
        else if(!Client.getBlockedblacklist().contains(userinput1))
        {
            WarnerClass.WarnerError(warner, "User isn't Blocked", false);
        }
    }
}
