package com.skronawi.spring.examples.jpa.impl;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface JpaDataRepository extends CrudRepository<JpaData, String> {

    Set<JpaData> findAll();

    //http://stackoverflow.com/questions/14356148/intellij-idea-specify-datasource-for-jpa-validation
    @Query("select d from JpaData d where d.data = :data")
    Set<JpaData> data(@Param("data") String data);
}
