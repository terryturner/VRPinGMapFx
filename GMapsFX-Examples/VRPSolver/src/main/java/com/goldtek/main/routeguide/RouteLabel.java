package com.goldtek.main.routeguide;

import java.util.ArrayList;
import java.util.List;

public class RouteLabel {
    private static RouteLabel sInstance = new RouteLabel();
    
    public static RouteLabel getInstance() { return sInstance; }
    
    private RouteLabel() {
        for (char c = 'A'; c <= 'Z'; c++){
            ascii.add (String.valueOf (c));
        }
    }
    
    private List<String> ascii = new ArrayList <String> (52);
    private List<String> drivers = new ArrayList<>();
    
    public String get(int index) {
        if (index < 0) index = 0;
        else if(index >=26){
        	return ascii.get(index-26)+ascii.get(index-26);
        }
//        else if (index >= ascii.size()) index = index % ascii.size();
        return ascii.get(index);
    }
    
    public String getLabel(int index, int order) {
        if (index < 0) index = 0;
        else if(index >=26){
        	return ascii.get(index-26)+ascii.get(index-26)+order;
        }
//        else if (index >= ascii.size()) index = index % ascii.size();
        return ascii.get(index) + order;
    }
    
    public void setDriver(int index, String driver) {
        if (index < 0) index = 0;
        else if (index >= drivers.size()) {
            for (int i = drivers.size(); i < index+1 ; i++) drivers.add("");
        }
        drivers.add(index, driver);
    }
    
    public String getDriver(int index) {
        if (index < 0) index = 0;
        else if (index >= drivers.size()) index = drivers.size() - 1;
        return drivers.get(index);
    }
}
