package com.goldtek.main.routeguide;

import com.goldtek.algorithm.Depot;

import javafx.scene.image.Image;

public interface IDragGuide {
	Image getImage(int index);
	void setImage(int index, Image image);
	void OnDragDetected(Depot item);
	Depot getDragItem();
}
