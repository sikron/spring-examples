package com.skronawi.spring.examples.caching.hibernate;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity(name = "ItemCollection")
@Table(name = "itemcollection")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ItemCollection {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;

    private String value;
    private String attribute;

    /*
    @Cache must be on this Set and ALSO on the Item itself
     */
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Item> items;
}
