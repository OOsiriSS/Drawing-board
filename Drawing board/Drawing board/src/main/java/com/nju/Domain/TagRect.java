package com.nju.Domain;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


public class TagRect {

    private Point originPoint;

    private double width;
    private double height;

    private String mark;

    public TagRect(){
        this.mark = "";
    }

    public TagRect(TagRect tagRect){
        this.originPoint = new Point(tagRect.getOriginPoint().getX(), tagRect.getOriginPoint().getY());
        this.width = tagRect.getWidth();
        this.height = tagRect.getHeight();
        this.mark = tagRect.getMark();
    }

    public Point getOriginPoint() {
        return originPoint;
    }

    public void setOriginPoint(Point originPoint) {
        this.originPoint = originPoint;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark){
        this.mark = mark;
    }

    public void setSize(Point nowPoint){
        this.width = nowPoint.getX() - originPoint.getX();
        this.height = nowPoint.getY() - originPoint.getY();
    }

    public void drawRect(GraphicsContext graphicsContext){
        double strokeWidth = graphicsContext.getLineWidth();
        Color color = (Color) graphicsContext.getStroke();
        graphicsContext.setLineWidth(1);
        graphicsContext.setStroke(Color.LIGHTGRAY);
        graphicsContext.strokeRect(originPoint.getX(), originPoint.getY(), width, height);
        graphicsContext.beginPath();
        addRemark(graphicsContext, mark);
        graphicsContext.setLineWidth(strokeWidth);
        graphicsContext.setStroke(color);
    }

    public void clearRect(GraphicsContext graphicsContext){
        double strokeWidth = graphicsContext.getLineWidth();
        Color color = (Color) graphicsContext.getStroke();
        graphicsContext.setLineWidth(3);
        graphicsContext.setStroke(Color.WHITE);

        graphicsContext.moveTo(originPoint.getX(), originPoint.getY());
        graphicsContext.lineTo(originPoint.getX()+width, originPoint.getY());
        graphicsContext.lineTo(originPoint.getX()+width, originPoint.getY()+height);
        graphicsContext.lineTo(originPoint.getX(), originPoint.getY()+height);
        graphicsContext.lineTo(originPoint.getX(), originPoint.getY());

        graphicsContext.stroke();
        graphicsContext.beginPath();

        graphicsContext.clearRect(originPoint.getX(), originPoint.getY(), width, height);

        graphicsContext.setLineWidth(strokeWidth);
        graphicsContext.setStroke(color);
    }

    public void addRemark(GraphicsContext graphicsContext, String mark){
        this.mark = mark;
        Double strokeWidth = graphicsContext.getLineWidth();
        Color color = (Color) graphicsContext.getStroke();
        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.setLineWidth(1);
        graphicsContext.setFont(new Font("Microsoft YaHei", 20));
        graphicsContext.strokeText(mark, originPoint.getX()+10, originPoint.getY()+20);
        graphicsContext.setLineWidth(strokeWidth);
        graphicsContext.setStroke(color);
    }
}
