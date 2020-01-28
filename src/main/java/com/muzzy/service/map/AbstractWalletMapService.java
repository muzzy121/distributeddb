package com.muzzy.service.map;

import com.muzzy.cipher.StringUtil;
import com.muzzy.domain.Wallet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class AbstractWalletMapService<T extends Wallet, ID extends String>{
    private Map<String,T> map = new HashMap<>();

    Set<T> findAll() {
        return new HashSet<>(map.values());
    }

    T findById(ID id) {
        return map.get(id);
    }

    T save(T object) {
        map.put(StringUtil.getStringFromKey(object.getPublicKey()), object);
        return map.get(StringUtil.getStringFromKey(object.getPublicKey()));
    }

    void deleteById(ID id) {
        map.remove(id);
    }

    void delete(T object) {
        map.entrySet().removeIf(x -> x.getValue().equals(object));
    }

}
