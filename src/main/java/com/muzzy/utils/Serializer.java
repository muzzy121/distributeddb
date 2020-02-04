package com.muzzy.utils;

import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class Serializer {

    private static String FILE_PATH = "/resources/repo/blockchainfile";

    public static void serialize(Object obj) {
        try {
            FileOutputStream fos = new FileOutputStream(FILE_PATH);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object deserialize() {
        Object obj = null;
        try {
            FileInputStream fis = new FileInputStream(FILE_PATH);
            BufferedInputStream bis = new BufferedInputStream(fis);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
