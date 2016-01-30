package com.skronawi.spring.examples.rest.communication;

import com.skronawi.spring.examples.rest.businesslogic.api.DataBusinessLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
public class DataController {

    @Autowired
    private DataBusinessLogic dataBusinessLogic;

    @RequestMapping(value = "/data",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Set<RestData> getAll() {
        Set<RestData> restDatas = DataMapper.toRest(dataBusinessLogic.getAll());
        return restDatas;
    }

    @RequestMapping(value = "/data/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public RestData get(@PathVariable("id") String id) {
        RestData restData = DataMapper.toRest(dataBusinessLogic.get(id));
        return restData;
    }

    @RequestMapping(value = "/data",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public RestData create(@RequestBody RestData restData) {
        RestData createdData = DataMapper.toRest(dataBusinessLogic.create(DataMapper.toBL(restData)));
        return createdData;
    }

    @RequestMapping(value = "/data/{id}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public RestData update(@RequestBody RestData restData, @PathVariable("id") String id) {
        RestData updatedData = DataMapper.toRest(dataBusinessLogic.update(DataMapper.toBL(restData)));
        return updatedData;
    }

    @RequestMapping(value = "/data/{id}",
            method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") String id) {
        dataBusinessLogic.delete(id);
    }
}
