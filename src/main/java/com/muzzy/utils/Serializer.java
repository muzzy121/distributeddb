package com.muzzy.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class Serializer<T> {
    private static final Logger LOG = LoggerFactory.getLogger(Serializer.class);
    private Path path;

    public void serialize(T obj, URL url) {
        try {
            path = Paths.get(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        try (FileOutputStream fos = new FileOutputStream(path.toString())) {
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.close();
            LOG.info("Saving data: " + path.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            LOG.error("Cant save file!");
//            e.printStackTrace();
        }
    }

    public T deserialize(URL url) {
        T obj = null;
        try {
            path = Paths.get(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        try {
            FileInputStream fis = new FileInputStream(path.toString());
            BufferedInputStream bis = new BufferedInputStream(fis);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = (T) ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
//            e.printStackTrace();
            LOG.error("Empty or unreadable file");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
