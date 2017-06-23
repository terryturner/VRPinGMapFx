package com.goldtek.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.layout.AnchorPane;




public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

    	Parent root = FXMLLoader.load(getClass().getResource("/com/goldtek/main/Scene.fxml"));
        Scene scene = new Scene(root);
        stage.setFullScreen(false);
//        stage.setMaximized(true);  //set default is max windows.
        stage.setResizable(true); //disable resize windows.
//        stage.initStyle(StageStyle.DECORATED);//default
//        stage.initStyle(StageStyle.UTILITY); //set windows style

        
        scene.setFill(Color.BLACK);
        scene.getStylesheets().add("/styles/Styles.css");

        MenuBar menuBar = new MenuBar();
        menuBar.prefWidthProperty().bind(stage.widthProperty()); //setup menu bar Fit to Parent width;
        
        // --- Menu File
        Menu menuFile = new Menu("File");
        
	        // --- Menu File __MenuItem exit        
	        MenuItem exit = new MenuItem("Exit");
	        exit.setOnAction(new EventHandler<ActionEvent>() {
	            public void handle(ActionEvent t) {
	                System.exit(0);
	            }
	        });
	        menuFile.getItems().addAll(exit);
	 
        // --- Menu Edit
        Menu menuEdit = new Menu("Edit");
 
        // --- Menu View
        Menu menuView = new Menu("View");
 
        menuBar.getMenus().addAll(menuFile, menuEdit, menuView);
        
        
        if (scene.getRoot() instanceof VBox) {
            ((VBox) scene.getRoot()).getChildren().addAll(menuBar);            	
        } else if (scene.getRoot() instanceof AnchorPane) {
            ((AnchorPane) scene.getRoot()).getChildren().addAll(menuBar);        	
        }

        
        
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
