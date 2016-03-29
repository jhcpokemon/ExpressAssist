package io.github.jhcpokemon.expressassist.util;


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
}
