package com.nju.Service;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import java.io.*;


public class ImageBase64Service {
    public static String getBase64OfImage(File file){
        InputStream inputStream = null;
        byte[] data = null;

        try{
            inputStream = new FileInputStream(file);
            data = new byte[inputStream.available()];
            inputStream.read(data);
        } catch (IOException e){
            e.printStackTrace();
        }
        return new String(Base64.encode(data));
    }
}
