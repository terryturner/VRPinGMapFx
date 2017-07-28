package com.goldtek.main.config;

import javafx.scene.control.ButtonBar.ButtonData;

import java.io.File;
import java.io.IOException;

import com.goldtek.algorithm.VrpMaker;
import com.goldtek.main.FileHandle;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.ImageView;
import javafx.stage.Window;
import javafx.util.Callback;

public class ConfigDialog implements Callback<ButtonType, Boolean>{
    private VrpMaker mVrpMaker = VrpMaker.getInstance();
    private ConfigDialogController mController;
    
    private final Window mWindow;
    private final ButtonType mConfirmButton = new ButtonType("Confirm", ButtonData.OK_DONE);
    private final ButtonType mCancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);    
    private final Dialog<Boolean> Dialog = new Dialog<>();

    public ConfigDialog(Window win) {
        mWindow = win;

        Dialog.setTitle("Algorithm Config");
        Dialog.setHeaderText("Adjust parameters here");
        
        ImageView image = new ImageView(this.getClass().getResource("/images/settings.png").toString());
        image.setFitWidth(50);
        image.setFitHeight(50);
        Dialog.setGraphic(image);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/goldtek/main/ConfigDialog.fxml"));
            Parent content = loader.load();
            mController = loader.getController();
            
            Dialog.getDialogPane().setContent(content);
        } catch (IOException e) {
            e.printStackTrace();
        }

        
        Dialog.getDialogPane().getButtonTypes().addAll(mConfirmButton, mCancelButton);        
        Dialog.setResultConverter(this);
        Dialog.getDialogPane().setPrefSize(600, 480);
        
        String inputPath = "input/goldtek_golden_sample.xml";
        if (!mVrpMaker.intialized())
        {
            File file = FileHandle.getInstance().showXMLChooser(mWindow, "請開啟標準範例");
            if (file == null) return;

            inputPath = file.getPath();
            mVrpMaker.readForGoldenSample(inputPath);
        }

        mController.setBuilder();
    }
    
    public void show() {
        if (mVrpMaker.intialized()) Dialog.showAndWait();
    }

    @Override
    public Boolean call(ButtonType buttonType) {
        if (buttonType == mConfirmButton) {
            mVrpMaker.buildFiniteSize(mController.getAddedVehicles(), mController.getAddedDepots());
            return true;
        } else if (buttonType == mCancelButton) {
            //System.exit(0);
            return false;
        }
        return false;
    }
}
