package com.goldtek.main.routeguide;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import java.io.IOException;

public class RouteGuidItem extends ListCell<ColorfulDepot> {
	@FXML private VBox Pane;
	@FXML private Label Line1;
	@FXML private Label Line2;
	@FXML private Label Line3;

	private Parent root;

	public RouteGuidItem() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/goldtek/main/RouteGuidItem.fxml"));
		fxmlLoader.setController(this);
		try {
			root = fxmlLoader.load();
			root.getStylesheets().add("/styles/RouteGuidItem.css");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void updateItem(ColorfulDepot colorDepot, boolean empty) {
		super.updateItem(colorDepot, empty);
		if (colorDepot != null) {
		    String label = RouteLabel.getInstance().get(colorDepot.getIndex());
		    String color = RouteColor.getInstance().get(colorDepot.getIndex());
		    String driver = RouteLabel.getInstance().getDriver(colorDepot.getIndex());
		    Pane.setStyle("-fx-border-color:" + color);

			switch (colorDepot.getListViewID()) {
			case START:
			    Line1.setText(String.format("%s車起始點 - %s", label, driver));
				Line2.setText("從集貨中心 裝載 " + colorDepot.getDepot().getPickupCapacity() + "件");
				Line3.setText("");
				break;
			case WAYPOINT:
				Line1.setText(label + colorDepot.getDepotPoint() + " " + colorDepot.getDepot().getName());
				
				String deliverMsg = "送貨 " + colorDepot.getDepot().getDeliverCapacity() + "件";
				if (colorDepot.getDepot().getDropoffShipment() > 0) deliverMsg += ", 直送放貨 " + colorDepot.getDepot().getDropoffShipment() + "件";
				String pickupMsg = "取貨 " + colorDepot.getDepot().getPickupCapacity() + "件";
				if (colorDepot.getDepot().getPickupShipment() > 0) pickupMsg += ", 快遞 " + colorDepot.getDepot().getPickupShipment() + "件";
				
				Line2.setText(deliverMsg);
                Line3.setText(pickupMsg);
				break;
			case END:
				Line1.setText(label + "車終點");
                Line2.setText("對集貨中心 提交 " + colorDepot.getDepot().getDeliverCapacity() + "件");
                Line3.setText("");
				break;
			default:
				break;
			}
			setGraphic(root);
		}
	}

}
