package com.skronawi.spring.examples.rest.service.util;

import com.skronawi.spring.examples.rest.communication.RestData;

public class TestUtil {

    public static RestData dummyRestData() {
        RestData restData = new RestData();
        restData.setData("dummy data " + System.currentTimeMillis());
        return restData;
    }
}
