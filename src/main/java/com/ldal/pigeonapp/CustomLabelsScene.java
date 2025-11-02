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
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CustomLabelsScene implements Initializable {
    @FXML
    private TextField emailInput;
    @FXML
    private Label emailAdderText;
    @FXML
    private VBox emailsVbox;

    @FXML
    private Label currLabelName;
    @FXML
    private Label addToLabelText;
    @FXML
    private AnchorPane newLabelContextMenu;
    @FXML
    private Button nextLabelButton;
    @FXML
    private TextField labelNameInput;

    Client client;

    private CustomLabel currLabel;
    private ArrayList<CustomLabel> labels;

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
    public void emailAdd(ActionEvent actionEvent) {
        String email = emailInput.getText();
        if(!email.isEmpty() && Client.getSQLServer().validatePigeonEmail(email) && !currLabel.emails.contains(email))
        {
            currLabel.emails.add(email);
            WarnerClass.WarnerError(warner, email + " added to this label", true);
            client.saveSettings();
            switchLabel();
        }
        else if(!Client.getSQLServer().validatePigeonEmail(email))
        {
            WarnerClass.WarnerError(warner, "Invalid user", false);
        }
        else if(email.isEmpty())
        {
            WarnerClass.WarnerError(warner, "Please input information", false);
        }
        else
        {
            WarnerClass.WarnerError(warner, "User already in this label", false);
        }
    }

    @FXML
    public void emailRemove(ActionEvent actionEvent) {
        String email = emailInput.getText();
        if(!email.isEmpty() && Client.getSQLServer().validatePigeonEmail(email) && currLabel.emails.contains(email))
        {
            currLabel.emails.removeIf(e ->(e.equals(email)));
            WarnerClass.WarnerError(warner, email + " removed from this label", true);
            switchLabel();

        }
        else if(email.isEmpty() || !Client.getSQLServer().validatePigeonEmail(email))
        {
            WarnerClass.WarnerError(warner, "Invalid user", false);
        }
        else
        {
            WarnerClass.WarnerError(warner, "User not in this label", false);
        }
    }

    @FXML
    public void labelDelete(ActionEvent actionEvent) {
        if(currLabel == null) return;
        CustomLabel labelToSet;

        if(CustomLabel.getLabelAmount() > 1 && currLabel.getLabelID() != 0) labelToSet = labels.get(currLabel.getLabelID() - 1);
        else if (CustomLabel.getLabelAmount() > 1 && currLabel.getLabelID() == 0) labelToSet = labels.get(1);
        else labelToSet = null;

        labels.remove(currLabel);

        for (int c = 0; c < labels.size(); c++)
        {
            CustomLabel cl = labels.get(c);
            cl.setLabelID(c);
            labels.set(c, cl);
        }

        currLabel = labelToSet;

        switchLabel();

        CustomLabel.decrementAmount();
        Client.setCustomLabels(labels);
        client.saveLabels();
    }

    @FXML
    public void nextLabel(ActionEvent actionEvent) {
        if((currLabel == null) || (currLabel.getLabelID() == labels.size() - 1)) return;
        currLabel = labels.get(currLabel.getLabelID() + 1);
        switchLabel();
    }

    @FXML
    public void prevLabel(ActionEvent actionEvent) {
        if((currLabel == null) || (currLabel.getLabelID() == 0)) return;
        currLabel = labels.get(currLabel.getLabelID() - 1);
        switchLabel();
    }

    @FXML
    public void openLabelCreator(ActionEvent actionEvent){
        newLabelContextMenu.setVisible(true);
        newLabelContextMenu.setDisable(false);

    }

    @FXML
    public void closeLabelCreator(ActionEvent actionEvent){
        newLabelContextMenu.setVisible(false);
        newLabelContextMenu.setDisable(true);

    }

    private void switchLabel(){
        if(currLabel == null){
            currLabelName.setText("None");
            addToLabelText.setText("Create a label to start");
        }
        else{
            emailsVbox.getChildren().clear();
            currLabelName.setText(currLabel.getLabelName());
            addToLabelText.setText("Add new emails to " + currLabel.getLabelName());

            for(String email : currLabel.emails){
                Label eLabel = new Label(email);
                String css = getClass().getResource("/styleforROBOTO.css").toExternalForm();
                eLabel.getStylesheets().add(css);
                eLabel.getStyleClass().add("custom-font-even-smaller");
                eLabel.getStyleClass().add("custom-font-color-black");
                emailsVbox.getChildren().add(eLabel);
            }

        }
        if((currLabel == null) || (currLabel.getLabelID() == labels.size() - 1)) {
            nextLabelButton.setText("+");
            nextLabelButton.setOnAction(e -> openLabelCreator(e));
        }
        else{
            nextLabelButton.setText("T");
            nextLabelButton.setOnAction(e -> nextLabel(e));
        }

    }

    @FXML
    private void createLabel(ActionEvent actionEvent){
        String lName = labelNameInput.getText();
        if(lName.isEmpty() || !CustomLabel.hasUniqueName(lName)) {
            warner.setText("Duplicate label name!");
            return;
        }
        labelNameInput.setText("");

        CustomLabel label = new CustomLabel(lName);
        labels.add(label);
        currLabel = label;
        switchLabel();
        closeLabelCreator(new ActionEvent());
        Client.setCustomLabels(labels);
        client.saveLabels();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        client = new Client();

        labels = Client.getCustomLabels();
        if(labels.isEmpty()) currLabel = null;
        else currLabel = labels.getFirst();

        switchLabel();
    }


}
