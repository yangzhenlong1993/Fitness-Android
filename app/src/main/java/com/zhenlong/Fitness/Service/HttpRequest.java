package com.zhenlong.Fitness.Service;


import com.zhenlong.Fitness.Bean.Msg;
import com.zhenlong.Fitness.Util.JsonTransfer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class HttpRequest {
    public static Msg buildURLConnection(Msg msg) {

        String queryInfo = null;
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        queryInfo = JsonTransfer.MsgToJson(msg);
        try {
            URL url = new URL("http://172.20.10.5:8080/mobile_project/"+msg.getOperation());
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setReadTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestProperty("Content-Type","application/json");
            OutputStream os = connection.getOutputStream();
            os.write(queryInfo.getBytes());
            InputStream in = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                msg = JsonTransfer.JsonToMsg(line);

            };

            in.close();
            os.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return msg;
    }
}
