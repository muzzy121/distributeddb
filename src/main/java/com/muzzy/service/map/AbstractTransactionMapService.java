package com.muzzy.service.map;

import com.muzzy.domain.Transaction;

import java.util.*;

/**
 * Created by Paweł Mazur
 * 14.01.2020
 */

public abstract class AbstractTransactionMapService<T extends Transaction, ID extends String> {

    private Map<String, T> map = new HashMap<>();

    LinkedHashSet<T> findAll() {
        return new LinkedHashSet<>(map.values());
    }

    T findById(ID id) {
        return map.get(id);
    }

    T save(T object) {
        map.put(object.getTransactionId(),object);
        return object;
    }

    void deleteById(ID id) {
        map.remove(id);
    }

    void delete(T object) {
        map.entrySet().removeIf(x -> x.getValue().equals(object));
    }

    void clear() {
        map.clear();
    }
}

