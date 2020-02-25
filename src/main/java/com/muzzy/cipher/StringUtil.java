package com.muzzy.cipher;

import com.muzzy.domain.Transaction;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

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

    public static PublicKey getPubKeyFromString(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] publicKeyData = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyData);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    public static PrivateKey getPrvKeyFromString(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] privateKeyData = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateKeyData);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    public static String getHashRoot(LinkedHashSet<Transaction> transactions) {
        int size = transactions.size();
        List<String> parents = new ArrayList<>();
        for (Transaction transaction : transactions) {
            parents.add(transaction.getTransactionId());
        }
        List<String> childs = parents;
        while (size > 1) {
            childs = new ArrayList<>();
            for (int i = 1; i < parents.size(); i++) {
                childs.add(applySha256(parents.get(i - 1) + parents.get(i)));
            }
            size = childs.size();
            parents = childs;
        }
        return (childs.size() == 1) ? childs.get(0) : "";
    }
}
