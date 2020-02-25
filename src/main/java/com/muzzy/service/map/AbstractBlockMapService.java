package com.muzzy.service.map;

import com.muzzy.domain.Block;

import java.util.*;

public abstract class AbstractBlockMapService<T extends Block,ID extends String> {
    private Map<String,T> map = new LinkedHashMap<>();
    LinkedHashSet<T> findAll(){
        return new LinkedHashSet<>(map.values());
    }
    T findById(ID id){
        return map.get(id);
    }
    T save(T object){
        map.put(object.getHash(), object);
        return map.get(object.getHash());
    }
    T saveAll(List<T> object){
        object.forEach(t -> map.put(t.getHash(), t));
        return null;
    }

    void deleteById(ID id){
        map.remove(id);
    }
    void delete(T object){
        map.entrySet().removeIf(x -> x.getValue().equals(object));
    }

    T getLastBlock(){
        String key = "";
        ArrayList<String> hashList = new ArrayList<>(map.keySet());
        if(!hashList.isEmpty()){
            key = hashList.get(hashList.size() - 1);
        } else return null;
        return map.get(key);
    }

//    private Long getNextId(){
//        if(map.isEmpty()) {
//            return 1L;
//        } else {
//            return Collections.max(map.keySet()) + 1;
//        }
//    }

}