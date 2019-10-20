package com.nju.Domain;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;


public class Stroke {

    private ArrayList<Point> points = new ArrayList<>();

    public Stroke(Stroke stroke){
        for(Point point: stroke.getPoints()){
            this.points.add(point);
        }
    }

    public Stroke(){

    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public void addPoint(Point point){
        points.add(point);
    }

    public void drawStroke(GraphicsContext graphicsContext){

        Color color = (Color) graphicsContext.getStroke();
        double strokeWidth = graphicsContext.getLineWidth();

        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.setLineWidth(2);

        for (Point point: points) {
            graphicsContext.lineTo(point.getX(), point.getY());
        }
        graphicsContext.stroke();
        graphicsContext.beginPath();

        graphicsContext.setLineWidth(strokeWidth);
        graphicsContext.setStroke(color);
    }

    public void clearStroke(GraphicsContext graphicsContext ,double strokeWidth){
        graphicsContext.setStroke(Color.WHITE);
        graphicsContext.setLineWidth(strokeWidth+3);
        for (Point point: points) {
            graphicsContext.lineTo(point.getX(), point.getY());
        }
        graphicsContext.stroke();
        graphicsContext.beginPath();
        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.setLineWidth(strokeWidth);
    }

    public void removeAllPoints(){
        points.clear();
    }
}
