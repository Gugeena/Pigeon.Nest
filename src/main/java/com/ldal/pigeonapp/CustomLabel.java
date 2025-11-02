package com.ldal.pigeonapp;

import javafx.scene.paint.Color;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class CustomLabel implements Serializable {
    private String labelName;
    private int labelID;
    private static int labelAmount;
    public ArrayList<String> emails;
    private User user;

    @Serial
    private final static long serialVersionUID = 2508L;

    CustomLabel(String labelName){
        emails = new ArrayList<>();
        setLabelName(labelName);
        setLabelID(labelAmount);
        labelAmount++;
        user = Client.getUser();
    }

    public User getUser() {
        return user;
    }

    public static boolean hasUniqueName(String labelName){
        ArrayList<CustomLabel> labels = Client.getCustomLabels();
        for (CustomLabel label : labels) {
            if (labelName.equals(label.getLabelName())) return false;
        }
        return true;
    }

    public static void decrementAmount(){
        labelAmount--;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public int getLabelID() {
        return labelID;
    }

    public void setLabelID(int labelID) {
        this.labelID = labelID;
    }

    public static int getLabelAmount() {
        return labelAmount;
    }

    public static void setLabelAmount(int labelAmount) {
        CustomLabel.labelAmount = labelAmount;
    }
}
