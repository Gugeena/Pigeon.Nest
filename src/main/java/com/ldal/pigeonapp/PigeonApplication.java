package com.ldal.pigeonapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PigeonApplication extends Application
{
    static String dateTimeSincer;
    SQLServer sqlServer = new SQLServer();
    @Override
    public void start(Stage stage) throws IOException
    {
        Scene scene;
        Parent root;
        new Client();
        //if(SQLServer.password != null && SQLServer.userName != null)
        //{
            //Client.setSqlServer(new SQLServer());
        //}
        if(Client.getUser() != null && Client.isRememberMe())
        {
            //root = FXMLLoader.load(PigeonApplication.class.getResource("/LoginScene.fxml"));
            root = FXMLLoader.load(PigeonApplication.class.getResource("/ProfileScene.fxml"));
            //stage.setFullScreen(true);
            //root = FXMLLoader.load(PigeonApplication.class.getResource("/ConMenu.fxml"));
            //root = FXMLLoader.load(PigeonApplication.class.getResource("/WelcomeScene.fxml"));
        }
        else
        {
            //root = FXMLLoader.load(PigeonApplication.class.getResource("/LoginScene.fxml"));
            root = FXMLLoader.load(PigeonApplication.class.getResource("/WelcomeScene.fxml"));
            //stage.setFullScreen(true);
            //root = FXMLLoader.load(PigeonApplication.class.getResource("/ConMenu.fxml"));
            //root = FXMLLoader.load(PigeonApplication.class.getResource("/EmailSelector.fxml"));
        }

        stage.setResizable(false);
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Pigeon");
        Image icon = new Image(PigeonApplication.class.getResource("/icon.png").toString());
        stage.getIcons().add(icon);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch();
    }
}