package com.skronawi.spring.examples.jpa.impl;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface JpaDataRepository extends CrudRepository<JpaData, String> {

    Set<JpaData> findAll();
}
