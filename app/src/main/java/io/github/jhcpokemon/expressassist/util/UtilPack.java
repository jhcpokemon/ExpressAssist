package io.github.jhcpokemon.expressassist.util;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * 验证是否为有效的邮箱地址
 */
public class UtilPack {
    public static final String TAG = "MYLOG";

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
        Log.i(TAG, Arrays.deepToString(pairs));
        for (String s : pairs) {
            if (s.contains("=")) {
                list.add(s.split("=")[1]);
            }
        }
        return list;
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
