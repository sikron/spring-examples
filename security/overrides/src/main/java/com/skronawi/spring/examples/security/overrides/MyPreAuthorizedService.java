package com.skronawi.spring.examples.security.overrides;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@PreAuthorize("hasRole('USR')")
@Service
public class MyPreAuthorizedService implements MyService {

    @Override
    public String usr(){
        return "usr";
    }

    @Override
    @PreAuthorize("hasRole('MGR')") //this overrides the class-level @PreAuthorize
    public String alsoMgr(){
        return "usrAndMgr";
    }
}
