package com.muzzy.service.map;

import com.muzzy.domain.Transaction;
import com.muzzy.domain.TransactionOutput;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Paweł Mazur
 * 14.01.2020
 */

public abstract class AbstractTransactionOutputMapService<T extends TransactionOutput, ID extends String> {

    private Map<String, T> map = new HashMap<>();

    Set<T> findAll() {
        return new HashSet<>(map.values());
    }

    T findById(ID id) {
        return map.get(id);
    }

    T save(T object) {
//        map.put(id, object);
        return object;
    }

    void deleteById(ID id) {
        map.remove(id);
    }

    void delete(T object) {
        map.entrySet().removeIf(x -> x.getValue().equals(object));
    }

}

