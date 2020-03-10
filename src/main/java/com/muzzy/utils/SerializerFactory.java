package com.muzzy.utils;

public class SerializerFactory {

    public static <clazzName> Serializer getSerializer(Class<?> clazz) {
        String clazzName = clazz.getName();
        Serializer<clazzName> serializer = new Serializer<clazzName>();
        return serializer;
    }
}
