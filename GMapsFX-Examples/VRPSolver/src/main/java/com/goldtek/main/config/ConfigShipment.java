package com.goldtek.main.config;

import com.goldtek.algorithm.Depot;

public class ConfigShipment {
    private final Depot From;
    private final Depot To;
    private final int Amount;
    
    public ConfigShipment(Depot from, Depot to, int amount) {
        From = from;
        To = to;
        Amount = amount;
    }
    
    public Depot getPickup() { return From; }
    public Depot getDropoff() { return To; }
    public int getAmount() { return Amount; }
}
