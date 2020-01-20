package com.muzzy.cipher;

//@FunctionalInterface
public interface Cipherable<T> {
    T Encode(T obj);
}
