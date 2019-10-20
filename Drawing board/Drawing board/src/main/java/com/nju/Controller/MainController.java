package com.nju.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXNodesList;
import com.nju.DataDservice.DataServiceImpl.PaintDataServiceImp;
import com.nju.DataDservice.PaintDataService;
import com.nju.Domain.Paint;
import com.nju.Domain.Point;
import com.nju.Domain.Stroke;
import com.nju.Domain.TagRect;
import com.nju.UIDomain.Button;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;


public class MainController implements Initializable {
    @FXML
    private StackPane stackPane;

    @FXML
    private Canvas canvas;

    private Paint paint = new Paint();
    private PaintDataService dataService = new PaintDataServiceImp();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Stroke stroke = new Stroke();//当前笔画
        TagRect tagRect = new TagRect();//当前标注框

        Button ibutton1 = new Button("M", "菜单", 0);
        JFXButton button1 = ibutton1.getButton();

        Button ibutton2 = new Button("绘", "绘制图像", 1);
        JFXButton button2 = ibutton2.getButton();

        Button ibutton3 = new Button("标", "标注图像", 1);
        JFXButton button3 = ibutton3.getButton();

        Button ibutton4 = new Button("撤", "撤销上一步", 0);
        JFXButton button4 = ibutton4.getButton();

        Button ibutton5 = new Button("画", "撤销上一步笔画", 1);
        JFXButton button5 = ibutton5.getButton();
        button5.getStyleClass().add("animated-option-sub-button3");

        Button ibutton6 = new Button("注", "撤销上一步标注", 1);
        JFXButton button6 = ibutton6.getButton();
        button6.getStyleClass().add("animated-option-sub-button3");

        Button ibutton7 = new Button("文", "文件操作", 0);
        JFXButton button7 = ibutton7.getButton();

        Button ibutton8 = new Button("存", "存储当前画布", 1);
        JFXButton button8 = ibutton8.getButton();
        button8.getStyleClass().add("animated-option-sub-button3");

        Button ibutton9 = new Button("取", "打开文件", 1);
        JFXButton button9 = ibutton9.getButton();
        button9.getStyleClass().add("animated-option-sub-button3");

        Button ibutton10 = new Button("新", "新建文件", 1);
        JFXButton button10 = ibutton10.getButton();
        button10.getStyleClass().add("animated-option-sub-button3");

        JFXNodesList nodesList2 = new JFXNodesList();
        nodesList2.setSpacing(10);
        nodesList2.addAnimatedNode(button7);
        nodesList2.addAnimatedNode(button8);
        nodesList2.addAnimatedNode(button9);
        nodesList2.addAnimatedNode(button10);
        nodesList2.setRotate(270);

        JFXNodesList nodesList1 = new JFXNodesList();
        nodesList1.setSpacing(10);
        nodesList1.addAnimatedNode(button4);
        nodesList1.addAnimatedNode(button5);
        nodesList1.addAnimatedNode(button6);
        nodesList1.setRotate(270);

        JFXNodesList nodesList = new JFXNodesList();
        nodesList.setSpacing(10);
        nodesList.addAnimatedNode(button1);
        nodesList.addAnimatedNode(button2);
        nodesList.addAnimatedNode(button3);
        nodesList.addAnimatedNode(nodesList1);
        nodesList.addAnimatedNode(nodesList2);

        button2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            buttonHandler(ibutton2, ibutton3);
        });
        button3.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            buttonHandler(ibutton3,ibutton2);
        });

        stackPane.getChildren().add(nodesList);
        stackPane.setMargin(nodesList, new Insets(10));
        stackPane.setAlignment(Pos.TOP_LEFT);
        /*
        初始化画笔
         */
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.setLineWidth(2);
        /*
        添加撤销监听
         */
        button5.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            paint.resetLastStep(graphicsContext);
        });
        button6.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            paint.resetLastRect(graphicsContext);
        });
        /*
        添加文件监听
         */
        button8.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            dataService.savePaintAsTxt(paint);
        });
        button9.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            paint = dataService.getSavedPaint();
            if(paint != null){
                graphicsContext.clearRect(0,0, canvas.getWidth(), canvas.getHeight());
                paint.restoreAll(graphicsContext);
            }
        });
        button10.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            graphicsContext.clearRect(0,0, canvas.getWidth(), canvas.getHeight());
            paint = new Paint();
        });
        /*
        添加画布监听
         */
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            if(ibutton3.getIsUp() == 1){
                tagRect.setOriginPoint(new Point(event.getX(), event.getY()));
                tagRect.setSize(new Point(event.getX(),event.getY()));
            }
        });
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            if(ibutton2.getIsUp() == 1){
                graphicsContext.lineTo(event.getX(),event.getY());
                graphicsContext.stroke();
                stroke.addPoint(new Point(event.getX(), event.getY()));
            }else if(ibutton3.getIsUp() == 1){
                tagRect.clearRect(graphicsContext);
                paint.restoreAll(graphicsContext);
                tagRect.setSize(new Point(event.getX(), event.getY()));
                tagRect.drawRect(graphicsContext);
            }
        });
        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
            graphicsContext.beginPath();
            if (ibutton2.getIsUp() == 1){
                Stroke tmpStroke = new Stroke(stroke);//对当前笔画进行深拷贝
                stroke.removeAllPoints();
                paint.addStroke(tmpStroke);
            }else if(ibutton3.getIsUp() == 1){
                String rev = paint.tagChosenRect(canvas, graphicsContext, new Rectangle((int)Math.ceil(tagRect.getOriginPoint().getX()), (int)Math.floor(tagRect.getOriginPoint().getY()), (int)Math.floor(tagRect.getWidth()), (int)Math.floor(tagRect.getHeight())));
                if (rev != null){
                    String mark = showTagDialog(tagRect, graphicsContext, rev);
                    if(mark != null){
                        TagRect tmpRect = new TagRect(tagRect);
                        tmpRect.addRemark(graphicsContext, mark);
                        paint.addTagRect(tmpRect);
                    }
                }
            }
        });
    }
    private void setButtonUp(Button button){
        button.getButton().getStyleClass().removeAll("animated-option-sub-button2");
        button.getButton().getStyleClass().add("animated-option-sub-button");
        button.setIsUp(1);
    }
    private void setButtonDown(Button button){
        button.getButton().getStyleClass().removeAll("animated-option-sub-button");
        button.getButton().getStyleClass().add("animated-option-sub-button2");
        button.setIsUp(0);
    }
    private void buttonHandler(Button button1, Button button2){
        if(button1.getIsUp() == 0){
            setButtonDown(button2);
            setButtonUp(button1);
        }else{
            setButtonDown(button1);
        }
    }
    private String showTagDialog(TagRect rect, GraphicsContext graphicsContext, String recommend){
        TextInputDialog inputDialog = new TextInputDialog(recommend);
        inputDialog.setTitle("在这里对图像进行标注");
        inputDialog.setHeaderText("推荐标注为： "+recommend);
        inputDialog.setContentText("您的标注：");
        Optional<String> result = inputDialog.showAndWait();
        if(result.isPresent()){
            return result.get();
        }else{
            rect.clearRect(graphicsContext);
            paint.restoreAll(graphicsContext);
            return null;
        }
    }
}
