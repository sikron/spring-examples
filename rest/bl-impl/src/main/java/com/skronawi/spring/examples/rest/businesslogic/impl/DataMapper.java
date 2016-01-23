package com.skronawi.spring.examples.rest.businesslogic.impl;

import com.skronawi.spring.examples.jpa.api.Data;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DataMapper {

    public static SimpleData fromPersistence(Data data){
        SimpleData simpleData = new SimpleData();
        simpleData.setData(data.getData());
        simpleData.setId(data.getId());
        return simpleData;
    }

    public static Set<SimpleData> fromPersistence(Collection<Data> datas){
        HashSet<SimpleData> simpleDatas = new HashSet<SimpleData>();
        for (Data data : datas){
            simpleDatas.add(fromPersistence(data));
        }
        return simpleDatas;
    }
}
