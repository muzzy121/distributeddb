package com.muzzy.cipher;

import java.security.Key;
import java.security.MessageDigest;
import java.util.Base64;

public class StringUtil {

    private StringUtil(){};

    public static String applySha256(String input){

        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(input.getBytes("UTF-8"));

            StringBuilder hexString = new StringBuilder();
            for (byte elem: hash) {
                String hex = Integer.toHexString(0xff & elem);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return String.valueOf(hexString);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    public static String getStringFromKey(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }
}
