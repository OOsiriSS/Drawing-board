package com.nju.DataDservice.DataServiceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nju.Applicaiton.MainApp;
import com.nju.DataDservice.PaintDataService;
import com.nju.Domain.Paint;
import javafx.stage.FileChooser;

import java.io.*;


public class PaintDataServiceImp implements PaintDataService {
    @Override
    public boolean savePaintAsTxt(Paint paint) {
        String storevalue = convertPaintToText(paint);
        BufferedWriter writer;

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Canvas");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(MainApp.primaryStage);

        if(file == null){
            return false;
        }
        try{
            if(file.exists()){
                file.delete();
                file.createNewFile();
            }
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            writer.write(storevalue);
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public Paint getSavedPaint() {
        BufferedReader reader;

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Canvas");
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("TXT files(*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File file = fileChooser.showOpenDialog(MainApp.primaryStage);

        if(file == null){
            return null;
        }

        try{
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String jsonString = "";
            String line = null;
            while((line = reader.readLine()) != null){
                jsonString += line;
            }
            reader.close();

            Paint paint = JSON.parseObject(jsonString, Paint.class);
            return paint;

        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    private String convertPaintToText(Paint paint){
        String jsonString = JSONObject.toJSONString(paint);
        return jsonString;
    }
}
