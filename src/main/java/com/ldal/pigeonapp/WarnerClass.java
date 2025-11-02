package com.ldal.pigeonapp;

import javafx.scene.control.Label;

public class WarnerClass
{
    public static void WarnerError(Label warner, String messege, boolean isPositive)
    {
        warner.setText(messege);
        if(isPositive) warner.setStyle("-fx-text-fill: green");
        else warner.setStyle("-fx-text-fill: red");
    }
}
