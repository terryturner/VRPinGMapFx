package com.goldtek.main.routeguide;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

import java.io.IOException;

import com.goldtek.algorithm.ColorfulDepot;
import com.goldtek.algorithm.ColorfulDepot.ListViewID;
import com.goldtek.algorithm.Depot;

public class RouteGuidItem extends ListCell<ColorfulDepot> {
	@FXML
	private VBox vBox;
	@FXML
	private Label label1;
	@FXML
	private Label label2;

	Parent root;

	public RouteGuidItem() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/goldtek/main/RouteGuidItem.fxml"));
		fxmlLoader.setController(this);
		try {
			root = fxmlLoader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void updateItem(ColorfulDepot colorDepot, boolean empty) {
		super.updateItem(colorDepot, empty);
		if (colorDepot != null) {
			switch (colorDepot.getListViewID()) {
			case START:
				String labelStart = (RouteLabel.getInstance().get(colorDepot.getIndex()) + "車起始點"+ " " + "\n" + "取貨=>"
						+ colorDepot.getDepot().getPickupCapacity() + "件");
				label1.setText(labelStart + "\n");
				label1.setTextFill(Color.web(RouteColor.getInstance().get(colorDepot.getIndex())));
				label1.setStyle("-fx-font-weight: bolder;-fx-font-size:15px;");
				break;
			case WAYPOINT:
				String labelPoint = (RouteLabel.getInstance().get(colorDepot.getIndex()) +colorDepot.getDepotPoint() +" "
						+ colorDepot.getDepot().getName() + "\n" + "放貨=>" + colorDepot.getDepot().getDeliverCapacity()
						+ "件" + "\n" + "取貨=>" + colorDepot.getDepot().getPickupCapacity() + "件");
				label1.setText(labelPoint + "\n");
				label1.setTextFill(Color.web(RouteColor.getInstance().get(colorDepot.getIndex())));
				label1.setStyle("-fx-font-weight: bolder;-fx-font-size:15px;");
				break;
			case END:
				String labelEed = (RouteLabel.getInstance().get(colorDepot.getIndex()) + "車終點"+" " + "\n" + "放貨=>"
						+ colorDepot.getDepot().getDeliverCapacity() + "件");
				label1.setText(labelEed+"\n");
				label1.setTextFill(Color.web(RouteColor.getInstance().get(colorDepot.getIndex())));
				label1.setStyle("-fx-font-weight: bolder;-fx-font-size:15px;");
				break;
			default:
				break;
			}
			setGraphic(root);
//			setPrefHeight(80);
			vBox.setPrefHeight(75);
			vBox.setAlignment(Pos.CENTER);
		}
	}

}
