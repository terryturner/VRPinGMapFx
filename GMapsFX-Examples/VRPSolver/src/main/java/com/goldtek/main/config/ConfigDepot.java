package com.goldtek.main.config;

import com.goldtek.algorithm.Depot;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ConfigDepot extends Depot {
    private final StringProperty mName = new SimpleStringProperty();
    private final BooleanProperty mSelected = new SimpleBooleanProperty();
    private final IntegerProperty mDeliver = new SimpleIntegerProperty();
    private final IntegerProperty mPickup = new SimpleIntegerProperty();
    
    public ConfigDepot(Depot depot) {
        super(depot);
        mName.set(depot.getName());
    }
    
    public ConfigDepot(String id, String county, String area, String name, double lng, double lat) {
        super(id, county, area, name, lng, lat);
        mName.set(name);
    }
    
    public final StringProperty nameProperty() {
        return this.mName;
    }

    public final String getName() {
        return this.nameProperty().get();
    }

    public final void setName(final String name) {
        this.nameProperty().set(name);
    }

    public final BooleanProperty onProperty() {
        return this.mSelected;
    }

    public final boolean isOn() {
        return this.onProperty().get();
    }

    public final void setOn(final boolean on) {
        this.onProperty().set(on);
    }
    
    public final IntegerProperty deliverProperty() {
        return this.mDeliver;
    }
    
    public final int getDeliverCapacity() {
        setDeliverCapacity(this.deliverProperty().get());
        return this.deliverProperty().get();
    }
    
    public final void setDeliver(final int amount) {
        this.deliverProperty().set(amount);
        setDeliverCapacity(amount);
    }

    public final IntegerProperty pickupProperty() {
        return this.mPickup;
    }
    
    public final int getPickupCapacity() {
        setPickupCapacity(this.pickupProperty().get());
        return this.pickupProperty().get();
    }
    
    public final void setPickup(final int amount) {
        this.pickupProperty().set(amount);
        setPickupCapacity(amount);
    }
    
    public String toString() {
        return getName();
    }
    
    public String debug() {
        return String.format("ConfigDepot: [%s] [%f, %f] %s - Pickup: %d, Deliver: %d", getLocationID(), getLongitude(), getLatitude(), getName(), getPickupCapacity(), getDeliverCapacity());
    }
}
