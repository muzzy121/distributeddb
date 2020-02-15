package com.muzzy.cipher;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.Key;

public class CipherTest implements Cipherable<byte[]> {

    @Override
    public byte[] Encode(byte[] plainBytes) {
        try {
//
//          Generate the key first
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128);  // Key size
            Key key = keyGen.generateKey();
            System.out.println(key);

            // Create Cipher instance and initialize it to encrytion mode
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");  // Transformation of the algorithm
            cipher.init(Cipher.ENCRYPT_MODE, key);

            return cipher.doFinal(plainBytes);

//            Reinitialize the Cipher to decryption mode
//            cipher.init(Cipher.DECRYPT_MODE, key, cipher.getParameters());
//            byte[] plainBytesDecrypted = cipher.doFinal(cipherBytes);
//
//            System.out.println("DECRUPTED DATA : " + new String(plainBytesDecrypted));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new byte[0];
    }
}

