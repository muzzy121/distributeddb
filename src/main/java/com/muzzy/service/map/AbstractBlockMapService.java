package com.muzzy.service.map;

import com.muzzy.domain.Block;

import java.util.*;

public abstract class AbstractBlockMapService<T extends Block,ID extends String> {
    private Map<String,T> map = new LinkedHashMap<>();
    Set<T> findAll(){
        return new HashSet<>(map.values());
    }
    T findById(ID id){
        return map.get(id);
    }
    T save(T object){
        map.put(object.getHash(), object);
        return map.get(object.getHash());
    }

    void deleteById(ID id){
        map.remove(id);
    }
    void delete(T object){
        map.entrySet().removeIf(x -> x.getValue().equals(object));
    }

    T getLastBlock(){
        ArrayList<String> hashList = new ArrayList<>(map.keySet());
        return map.get(hashList.get(hashList.size()-1));
    }

//    private Long getNextId(){
//        if(map.isEmpty()) {
//            return 1L;
//        } else {
//            return Collections.max(map.keySet()) + 1;
//        }
//    }

}