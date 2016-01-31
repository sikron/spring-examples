package com.skronawi.spring.examples.rest.communication;

import com.skronawi.spring.examples.rest.businesslogic.api.Data;
import com.skronawi.spring.examples.rest.businesslogic.api.DataBusinessLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Set;

@RestController
@RequestMapping(value = "/data")
public class DataController {

    @Autowired
    private DataBusinessLogic dataBusinessLogic;

    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Set<RestData> getAll() {
        Set<RestData> restDatas = DataMapper.toRest(dataBusinessLogic.getAll());
        return restDatas;
    }

    @RequestMapping(value = "{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public RestData get(@PathVariable("id") String id) {
        Data data = dataBusinessLogic.get(id);
        if (data == null){
            throw new DataNotFoundException(id);
        }
        return DataMapper.toRest(data);
    }

    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@RequestBody RestData restData, UriComponentsBuilder builder) {
        RestData createdData = DataMapper.toRest(dataBusinessLogic.create(DataMapper.toBL(restData)));
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/data/{id}").buildAndExpand(createdData.getId()).toUri());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<RestData>(createdData, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "{id}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public RestData update(@RequestBody RestData restData, @PathVariable("id") String id) {
        RestData updatedData = DataMapper.toRest(dataBusinessLogic.update(DataMapper.toBL(restData)));
        return updatedData;
    }

    @RequestMapping(value = "{id}",
            method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") String id) {
        dataBusinessLogic.delete(id);
    }
}
