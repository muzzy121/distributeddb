package com.muzzy.cipher;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.Key;

public class CipherTest implements Cipherable {

    @Override
    public byte[] Encode(byte[] plainBytes) {
        try {
//            byte[] plainBytes = "HELLO JCE".getBytes();

            // Generate the key first
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128);  // Key size
            Key key = keyGen.generateKey();
            System.out.println(key);

            // Create Cipher instance and initialize it to encrytion mode
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");  // Transformation of the algorithm
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] cipherBytes = cipher.doFinal(plainBytes);
            return cipherBytes;

//            // Reinitialize the Cipher to decryption mode
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

