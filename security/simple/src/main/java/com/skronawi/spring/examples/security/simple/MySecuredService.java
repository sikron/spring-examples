package com.skronawi.spring.examples.security.simple;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Secured("ROLE_USR")
@Service
public class MySecuredService implements MyService{

    public String usr(){
        return "usr";
    }

    @Secured("ROLE_MGR")
    public String alsoMgr(){
        return "usrAndMgr";
    }
}
