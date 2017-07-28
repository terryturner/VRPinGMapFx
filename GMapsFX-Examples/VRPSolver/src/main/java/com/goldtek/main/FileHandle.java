package com.goldtek.main;

import java.io.File;

import javafx.stage.FileChooser;
import javafx.stage.Window;

public class FileHandle {
    private static FileHandle sInstance = new FileHandle();
    
    public static FileHandle getInstance() { return sInstance; }
    
    public boolean isExists(String path) {
        File file = new File(path);
        if (file.exists()) return true;
        return false;
    }
    
    public void showChooser(Window window) {
        FileChooser dialog = new FileChooser();
        dialog.showOpenDialog(window);
    }
    
    public File showXMLChooser(Window window) {
        FileChooser dialog = new FileChooser();
        dialog.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML", "*.xml"));
        return dialog.showOpenDialog(window);
    }
    
    public File showXMLChooser(Window window, String title) {
        FileChooser dialog = new FileChooser();
        dialog.setTitle(title);
        dialog.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML", "*.xml"));
        return dialog.showOpenDialog(window);
    }
}
