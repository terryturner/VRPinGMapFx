package com.goldtek.main.routeguide;

import java.util.ArrayList;
import java.util.List;

public class RouteColor {
    private static RouteColor sInstance = new RouteColor();
    
    public static RouteColor getInstance() { return sInstance; }
    
    private RouteColor() {
        
    }
    
    private String[] colors = { "#438391", "#62a3cf", "#333366", "#770f5d", "#ffe246", "#9e379f", "#01aebf" };
    
    public String get(int index) {
        if (index < 0) index = 0;
        else if (index >= colors.length) index = index % colors.length;
        return colors[index];
    }

}
