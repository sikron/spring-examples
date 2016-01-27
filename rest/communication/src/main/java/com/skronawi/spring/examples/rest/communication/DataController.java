package com.skronawi.spring.examples.rest.communication;

import com.skronawi.spring.examples.rest.businesslogic.api.DataBusinessLogic;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Set<RestData> getAll() {
        return DataMapper.toRest(dataBusinessLogic.getAll());
    }

    @RequestMapping(value = "/data/{id}",
            consumes = MediaType.ALL_VALUE,
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public RestData get(@PathVariable("id") String id) {
        return DataMapper.toRest(dataBusinessLogic.get(id));
    }

    @RequestMapping(value = "/data",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public RestData create(@RequestBody RestData restData) {
        return DataMapper.toRest(dataBusinessLogic.create(DataMapper.toBL(restData)));
    }

    @RequestMapping(value = "/data/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public RestData update(@RequestBody RestData restData, @PathVariable("id") String id) {
        return DataMapper.toRest(dataBusinessLogic.update(DataMapper.toBL(restData)));
    }

    @RequestMapping(value = "/data/{id}",
            consumes = MediaType.ALL_VALUE,
            method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") String id) {
        dataBusinessLogic.delete(id);
    }
}
