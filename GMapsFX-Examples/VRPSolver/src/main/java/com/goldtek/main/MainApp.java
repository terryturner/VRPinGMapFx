package com.goldtek.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.layout.AnchorPane;



public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

    	Parent root = FXMLLoader.load(getClass().getResource("/com/goldtek/main/Scene2.fxml"));
        Scene scene = new Scene(root);
        stage.setFullScreen(false);
        stage.setMaximized(false);  //set default is max windows.
        stage.setResizable(false); //disable resize windows.
        
        scene.setFill(Color.BLACK);
        scene.getStylesheets().add("/styles/Styles.css");

        
        stage.setTitle("Goldtek VRP Solver");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
