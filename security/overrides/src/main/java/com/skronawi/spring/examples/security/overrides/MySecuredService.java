package com.skronawi.spring.examples.security.overrides;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Secured("ROLE_USR")
@Service
public class MySecuredService implements MyService{

    public String usr(){
        return "usr";
    }

    @Secured("ROLE_MGR") //this overrides the class-level @Secured
    public String alsoMgr(){
        return "usrAndMgr";
    }
}
