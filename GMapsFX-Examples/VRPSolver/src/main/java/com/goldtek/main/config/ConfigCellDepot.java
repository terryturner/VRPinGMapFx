package com.goldtek.main.config;

import java.io.IOException;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.converter.NumberStringConverter;

public class ConfigCellDepot extends ListCell<ConfigDepot> {
    @FXML protected CheckBox Select;
    @FXML protected Text Name;
    @FXML protected TextField Deliver;
    @FXML protected TextField Pickup;
    private Parent root;
    
    private final Callback<ConfigDepot, ObservableValue<Boolean>> mCheckedCallback;
    private final Callback<ConfigDepot, IntegerProperty> mDeliverCallback;
    private final Callback<ConfigDepot, IntegerProperty> mPickupCallback;
    
    private ObservableValue<Boolean> mSelectProperty;
    private IntegerProperty mDeliverProperty;
    private IntegerProperty mPickupProperty;
    private NumberStringConverter mNumStringCoverter = new NumberStringConverter();
    
    public ConfigCellDepot() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/goldtek/main/ConfigCellDepot.fxml"));
            loader.setController(this);
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.getStyleClass().add("check-box-list-cell");
        
        mCheckedCallback = new Callback<ConfigDepot, ObservableValue<Boolean>>() {
            @Override public ObservableValue<Boolean> call(ConfigDepot depot) { return depot.onProperty(); }
        };
        
        mDeliverCallback = new Callback<ConfigDepot, IntegerProperty>() {
            @Override public IntegerProperty call(ConfigDepot depot) { return depot.deliverProperty(); }
        };
        
        mPickupCallback = new Callback<ConfigDepot, IntegerProperty>() {
            @Override public IntegerProperty call(ConfigDepot depot) { return depot.pickupProperty(); }
        };
        
        Deliver.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    Deliver.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        
        Pickup.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    Pickup.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }
    
    @Override
    protected void updateItem(ConfigDepot item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
            Name.setText(item.getLocationID() + " " + item.getName());
            setGraphic(root);
            
            if (mSelectProperty != null) Select.selectedProperty().unbindBidirectional((BooleanProperty) mSelectProperty);
            mSelectProperty = mCheckedCallback.call(item);
            if (mSelectProperty != null) Select.selectedProperty().bindBidirectional((BooleanProperty) mSelectProperty);
            
            if (mDeliverProperty != null) Deliver.textProperty().unbindBidirectional((IntegerProperty)mDeliverProperty);
            mDeliverProperty = mDeliverCallback.call(item);
            if (mDeliverProperty != null) {
                Deliver.textProperty().bindBidirectional((IntegerProperty)mDeliverProperty, mNumStringCoverter);
                Deliver.positionCaret(Deliver.getLength());
            }
            
            if (mPickupProperty != null) Pickup.textProperty().unbindBidirectional((IntegerProperty)mPickupProperty);
            mPickupProperty = mPickupCallback.call(item);
            if (mPickupProperty != null) {
                Pickup.textProperty().bindBidirectional((IntegerProperty)mPickupProperty, mNumStringCoverter);
                Pickup.positionCaret(Pickup.getLength());
            }
        } else {
            setGraphic(null);
        }
    }
}

