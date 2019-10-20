package com.nju.Applicaiton;

import com.nju.Service.BaiduAuthService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainApp extends Application {

    public static Stage primaryStage;
    public static String access_token;

    public static void main(String[] args){
        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        MainApp.access_token = BaiduAuthService.getAuth();
        MainApp.primaryStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/Main.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("css/jfoenix-components.css").toExternalForm());
        primaryStage.setTitle("Tagger");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
