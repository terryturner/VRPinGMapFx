package com.goldtek.main.routeguide;

import java.util.ArrayList;
import java.util.List;

public class RouteLabel {
    private static RouteLabel sInstance = new RouteLabel();
    
    public static RouteLabel getInstance() { return sInstance; }
    
    private RouteLabel() {
        for (char c = 'A'; c <= 'Z'; c++)
            ascii.add (String.valueOf (c));
    }
    
    private List <String> ascii = new ArrayList <String> (26);
    
    public String get(int index) {
        if (index < 0) index = 0;
        else if (index >= ascii.size()) index = index % ascii.size();
        return ascii.get(index);
    }
    
    public String getLabel(int index, int order) {
        if (index < 0) index = 0;
        else if (index >= ascii.size()) index = index % ascii.size();
        return ascii.get(index) + order;
    }
}
