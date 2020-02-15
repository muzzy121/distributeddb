package com.muzzy.service;

import java.util.Set;

/**
 * Created by Paweł Mazur
 * 14.01.2020
 */

public interface CrudService<T, ID> {
    Set<T> getAll();

    T getById(ID id);

    T save(T t);

    void delete(T t);

    void deleteById(ID id);
}