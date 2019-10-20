package com.nju.UIDomain;

import com.jfoenix.controls.JFXButton;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;


public class Button {
    private JFXButton button;
    private int isUp = 0;

    public Button(String name, String toolTip, int sign){

        if(sign == 0){
            button = new JFXButton("");
            Label label = new Label(name);
            label.setAlignment(Pos.CENTER);
            button.setAlignment(Pos.CENTER);
            button.setGraphic(label);
            button.setTooltip(new Tooltip(toolTip));
            label.setStyle("-fx-text-fill: WHITE;-fx-font-weight: 900");
            button.setButtonType(JFXButton.ButtonType.RAISED);
            button.getStyleClass().add("animated-option-button");
        }else{
            button = new JFXButton(name);
            button.setTooltip(new Tooltip("绘制图像"));
            button.setButtonType(JFXButton.ButtonType.RAISED);
            button.getStyleClass().add("animated-option-button");
        }
    }



    public JFXButton getButton() {
        return button;
    }

    public void setButton(JFXButton button) {
        this.button = button;
    }

    public int getIsUp() {
        return isUp;
    }

    public void setIsUp(int isUp) {
        this.isUp = isUp;
    }
}
