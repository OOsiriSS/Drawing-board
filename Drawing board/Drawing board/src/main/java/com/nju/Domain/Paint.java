package com.nju.Domain;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nju.Applicaiton.MainApp;
import com.nju.Service.ImageBase64Service;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


public class Paint {

    private ArrayList<Stroke> shapes = new ArrayList<>();

    private ArrayList<TagRect> tags = new ArrayList<>();

    public void addTagRect(TagRect tagRect){
        this.tags.add(tagRect);
    }

    public void addStroke(Stroke stroke){
        this.shapes.add(stroke);
    }

    /**
     * 撤销上一笔
     * @param graphicsContext
     */
    public void resetLastStep(GraphicsContext graphicsContext){
        if(shapes.size() == 0){
            return;
        }
        Stroke tmpStroke = shapes.get(shapes.size()-1);
        tmpStroke.clearStroke(graphicsContext, graphicsContext.getLineWidth());
        shapes.remove(shapes.size()-1);
        /*
        处理重合线条
         */
        restoreAll(graphicsContext);
    }

    /**
     * 撤销上一个标注
     * @param graphicsContext
     */
    public void resetLastRect(GraphicsContext graphicsContext){
        if(tags.size() == 0) return;
        TagRect tmpRect = tags.get(tags.size() - 1);
        tmpRect.clearRect(graphicsContext);
        tags.remove(tags.size() - 1);
        restoreAll(graphicsContext);
    }

    /**
     * 重绘当前画布所有内容
     * @param graphicsContext
     */
    public void restoreAll(GraphicsContext graphicsContext){
        restoreRects(graphicsContext);
        restoreStrokes(graphicsContext);
    }

    /**
     * 标注选中部分
     * @param canvas
     * @param graphicsContext
     * @param snapthotRect
     * @return
     */
    public String tagChosenRect(Canvas canvas, GraphicsContext graphicsContext, Rectangle snapthotRect){
        WritableImage wim = new WritableImage(900,600);
        canvas.snapshot(null, wim);

        File src = new File(getClass().getClassLoader().getResource("img/src.png").getPath());
        File dest = new File(getClass().getClassLoader().getResource("img/dest.png").getPath());
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(wim,null), "png", src);
        }catch (Exception e){
            e.printStackTrace();
        }
        boolean cutImageSuccess = cutImage(src, dest, snapthotRect);
        if(cutImageSuccess){
            return recognizeImage(dest, MainApp.access_token);
        }
        //非法标注清除标框
        TagRect rect = new TagRect();
        rect.setOriginPoint(new Point(snapthotRect.getX(), snapthotRect.getY()));
        rect.setWidth(snapthotRect.getWidth());
        rect.setHeight(snapthotRect.getHeight());
        rect.clearRect(graphicsContext);
        restoreAll(graphicsContext);
        return null;
    }

    private void restoreStrokes(GraphicsContext graphicsContext){
        for(Stroke stroke: shapes){
            stroke.drawStroke(graphicsContext);
        }
    }

    private void restoreRects(GraphicsContext graphicsContext){
        for (TagRect tagRect: tags){
            tagRect.clearRect(graphicsContext);
        }
        for(TagRect tagRect: tags){
            tagRect.drawRect(graphicsContext);
        }
    }

    private boolean cutImage(File srcFile, File destFile, java.awt.Rectangle snapshotRect){
        try {
            FileInputStream fileInputStream= new FileInputStream(srcFile);
            ImageInputStream imageInputStream = ImageIO.createImageInputStream(fileInputStream);
            ImageReader reader = ImageIO.getImageReadersBySuffix("png").next();
            reader.setInput(imageInputStream, true);

            ImageReadParam param = reader.getDefaultReadParam();
            param.setSourceRegion(snapshotRect);//截取
            BufferedImage bufferedImage = reader.read(0, param);
            FileOutputStream fileOutputStream= new FileOutputStream(destFile.getPath());
            ImageIO.write(bufferedImage, "png", fileOutputStream);
            fileInputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalArgumentException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("非法标注框");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    private String recognizeImage(File file, String access_token){
        String imageBase64 = ImageBase64Service.getBase64OfImage(file);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("image", imageBase64);
        String image = jsonObject.toJSONString();
        String requestUrl = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/classification/shape" + "?access_token=" + access_token;
        try {
            //发送HTTP JSON请求
            URL realUrl = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestMethod("POST");
            connection.connect();

            //读取结果
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(image.getBytes(StandardCharsets.UTF_8));
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = "";
            String line = null;
            while((line = reader.readLine()) != null){
                result += line;
            }
            JSONObject resultJSONObject = JSON.parseObject(result);
            JSONArray category = resultJSONObject.getJSONArray("results");
            JSONObject categoryResult = category.getJSONObject(0);
            return categoryResult.getString("name");
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Stroke> getShapes() {
        return shapes;
    }

    public void setShapes(ArrayList<Stroke> shapes) {
        this.shapes = shapes;
    }

    public ArrayList<TagRect> getTags() {
        return tags;
    }

    public void setTags(ArrayList<TagRect> tags) {
        this.tags = tags;
    }
}
