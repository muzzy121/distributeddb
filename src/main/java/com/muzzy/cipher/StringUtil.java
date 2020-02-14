package com.muzzy.cipher;

import com.muzzy.domain.Transaction;

import java.security.Key;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Set;

public class StringUtil {

    private StringUtil() {
    }

    ;

    public static String applySha256(String input) {

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(input.getBytes("UTF-8"));

            StringBuilder hexString = new StringBuilder();
            for (byte elem : hash) {
                String hex = Integer.toHexString(0xff & elem);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return String.valueOf(hexString);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getStringFromKey(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public static String getHashRoot(Set<Transaction> transactions) {
        int size = transactions.size();
        List<String> parrents = new ArrayList<>();
        for (Transaction transaction : transactions) {
            parrents.add(transaction.getTransactionId());
        }
        List<String> childs = parrents;
        while (size > 1) {
            childs = new ArrayList<>();
            for (int i = 1; i < parrents.size(); i++) {
                childs.add(applySha256(parrents.get(i - 1) + parrents.get(i)));
            }
            size = childs.size();
            parrents = childs;
        }
        return (childs.size() == 1) ? childs.get(0) : "";
    }
}

