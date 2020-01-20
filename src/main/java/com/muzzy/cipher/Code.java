package com.muzzy.cipher;

import java.security.MessageDigest;

public class Code {

    private Code(){};

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
}
