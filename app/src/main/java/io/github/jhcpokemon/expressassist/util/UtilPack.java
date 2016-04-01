package io.github.jhcpokemon.expressassist.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * 验证是否为有效的邮箱地址
 */
public class UtilPack {
    public static int statusCode(String email) {
        int resultCode = 0;
        try {
            InternetAddress emailAddress = new InternetAddress(email);
            emailAddress.validate();
        } catch (AddressException e) {
            resultCode = 1;
        }
        return resultCode;
    }

    public static boolean valid(String email, String password) {
        return statusCode(email) == 0 && !password.equals("");
    }

    public static List<String> parseUri(String uri) {
        List<String> list = new ArrayList<>();
        String query = uri.split("\\?")[1];
        String[] pairs = query.split("&");
        for (String s : pairs) {
            list.add(s.split("=")[1]);
        }
        return list;
    }

    public static String getJsonData(String uri) {
        StringBuilder jsonSB = new StringBuilder();
        URL httpURL;
        String line;
        try {
            httpURL = new URL(uri);
            HttpURLConnection connection = (HttpURLConnection) httpURL.openConnection();
            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            BufferedReader bReader = new BufferedReader(reader);
            while ((line = bReader.readLine()) != null) {
                jsonSB.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonSB.toString();
    }
}
