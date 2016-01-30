package com.skronawi.spring.examples.rest.communication;

import com.skronawi.spring.examples.rest.businesslogic.api.Data;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DataMapper {

    public static RestData toRest(Data data) {
        if (null == data) {
            return null;
        }
        RestData restData = new RestData();
        restData.setId(data.getId());
        restData.setData(data.getData());
        return restData;
    }

    public static Set<RestData> toRest(Collection<Data> datas) {
        HashSet<RestData> restDatas = new HashSet<RestData>();
        for (Data data : datas) {
            restDatas.add(toRest(data));
        }
        return restDatas;
    }

    public static Data toBL(final RestData data) {
        return new DataAdapter(data);
    }
}
