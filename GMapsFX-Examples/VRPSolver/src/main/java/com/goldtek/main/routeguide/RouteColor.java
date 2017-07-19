package com.goldtek.main.routeguide;

import java.util.ArrayList;
import java.util.List;

public class RouteColor {
    private static RouteColor sInstance = new RouteColor();
    
    public static RouteColor getInstance() { return sInstance; }
    
    public RouteColor() {
        
    }
    
    private String[] colors = { "#006600", "#ff0000", "#6600ff", "#ff9900", "#800000", "#ff00ff", "#0099cc" };
    
    public String get(int index) {
        if (index < 0) index = 0;
        else if (index >= colors.length) index = index % colors.length;
        return colors[index];
    }

}
