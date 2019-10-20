package com.nju.Service;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class BaiduAuthService {

    public static String getAuth(){
        String clientId = "iK5CpBGE5ZZEDIrdvUHN837a";
        String clientSecret = "yF6fC3M6kRGl5y8W2NzqPj5nGunVmFRi";
        return getAuth(clientId, clientSecret);
    }

    public static String getAuth(String ak, String sk){
        String authHost = "http://aip.baidubce.com/oauth/2.0/token?";
        String getAccessTokenUrl = authHost
                + "grant_type=client_credentials"
                + "&client_id="+ak
                + "&client_secret="+sk;
        try{
            URL realUrl = new URL(getAccessTokenUrl);
            HttpURLConnection connection = (HttpURLConnection)realUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            /*
            Map<String, List<String>> map = connection.getHeaderFields();
            for (String key: map.keySet()){
                System.out.println(key + "--->" + map.get(key));
            }
            */
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = "";
            String line;
            while((line = reader.readLine()) != null){
                result += line;
            }
            System.err.println("result:" + result);
            JSONObject jsonObject = new JSONObject(result);
            String access_token = jsonObject.getString("access_token");
            return access_token;
        }catch (Exception e){
            System.err.println("获取token失败");
            e.printStackTrace();
        }
        return null;
    }
}
