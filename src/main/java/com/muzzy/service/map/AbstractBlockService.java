package com.muzzy.service.map;

import com.muzzy.domain.Block;

import java.util.*;

public abstract class AbstractBlockService<T extends Block,ID extends Long> {
    private Map<Long,T> map = new HashMap<>();
    Set<T> findAll(){
        return new HashSet<>(map.values());
    }
    T findById(ID id){
        return map.get(id);
    }
    T save(T object){
        if(object != null){
            if(object.getId() == null) {
                object.setId(getNextId());
            }
            map.put(object.getId(), object);
            return object;
        } else {
            throw new RuntimeException("Object cannot be null");
        }
    }

    void deleteById(ID id){
        map.remove(id);
    }
    void delete(T object){
        map.entrySet().removeIf(x -> x.getValue().equals(object));
    }

    private Long getNextId(){
        if(map.isEmpty()) {
            return 1L;
        } else {
            return Collections.max(map.keySet()) + 1;
        }
    }

}