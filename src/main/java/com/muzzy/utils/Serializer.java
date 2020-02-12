package com.muzzy.utils;

import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class Serializer {

    private final String PATH = getClass().getResource("/blockchain/block.chain").toString();
//    private static String FILE_PATH = "/resources/repo/blockchainfile";

    public void serialize(Object obj) {
        try {
            FileOutputStream fos = new FileOutputStream(PATH);
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

    public Object deserialize() {
        Object obj = null;
        try {
            FileInputStream fis = new FileInputStream(PATH);
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
