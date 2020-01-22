package com.muzzy.service;

import java.util.Set;

/**
 * Created by Paweł Mazur
 * 14.01.2020
 */

public interface TransactionOutputCrudService<T,ID> {
    Set<T> getAll();
    T getById(ID id);
    T save(ID id, T t);
    void delete(T t);
    void deleteById(ID id);
}