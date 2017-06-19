package com.goldtek.main.routeguide;

import java.util.ArrayList;
import java.util.List;

import com.goldtek.algorithm.Depot;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class GuideCell extends ListCell<Depot> {
	private final ImageView imageView = new ImageView();
	private final IDragGuide dragHandler;

    public GuideCell(IDragGuide handler) {
    	dragHandler = handler;
        ListCell thisCell = this;

        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        setAlignment(Pos.CENTER);

        setOnDragDetected(event -> {
            if (getItem() == null) {
                return;
            }

            ObservableList<Depot> items = getListView().getItems();

            Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            //content.putString(getItem().getName());
            content.putString(String.valueOf(items.indexOf(getItem())));
            dragboard.setDragView(dragHandler.getImage(items.indexOf(getItem())));
            dragboard.setContent(content);
            dragHandler.OnDragDetected(getItem());

            event.consume();
        });

        setOnDragOver(event -> {
        	//System.out.println("start over: " + getItem().getName());
            if (event.getGestureSource() != thisCell &&
                   event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            
            event.consume();
        });

        setOnDragEntered(event -> {
        	//System.out.println("start enter: " + getItem().getName());
            if (event.getGestureSource() != thisCell &&
                    event.getDragboard().hasString()) {
                setOpacity(0.3);
            }
        });

        setOnDragExited(event -> {
            if (event.getGestureSource() != thisCell &&
                    event.getDragboard().hasString()) {
                setOpacity(1);
            }
        });

        setOnDragDropped(event -> {
            if (getItem() == null) {
                return;
            }

            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasString()) {
                ObservableList<Depot> items = getListView().getItems();
                //int draggedIdx = items.indexOf(db.getString());
                int draggedIdx = Integer.valueOf(db.getString());
                int thisIdx = items.indexOf(getItem());

                Image temp = dragHandler.getImage(draggedIdx);
                dragHandler.setImage(draggedIdx, dragHandler.getImage(thisIdx));
                dragHandler.setImage(thisIdx, temp);

                items.set(draggedIdx, getItem());
                items.set(thisIdx, dragHandler.getDragItem());

                List<Depot> itemscopy = new ArrayList<>(getListView().getItems());
                getListView().getItems().setAll(itemscopy);

                success = true;
            }
            event.setDropCompleted(success);

            event.consume();
        });

        setOnDragDone(DragEvent::consume);
    }

    @Override
    protected void updateItem(Depot item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setGraphic(null);
        } else {
            imageView.setImage(
            		dragHandler.getImage(getListView().getItems().indexOf(item))
            );
            setGraphic(imageView);
        }
    }
    
    public static Image textToImage(String text) {
//    	Text t = new Text(text);
//    	Scene scene = new Scene(new StackPane(t));
//    	return t.snapshot(null, null);
    	
        Label label = new Label(text);
        label.setMinSize(125, 40);
        label.setMaxSize(125, 40);
        label.setPrefSize(125, 40);
        label.setStyle("-fx-background-color: white; -fx-text-fill:black;");
        label.setWrapText(true);
        Scene scene = new Scene(new Group(label));
        WritableImage img = new WritableImage(125, 125) ;
        scene.snapshot(img);
        return img ;
    }
}
