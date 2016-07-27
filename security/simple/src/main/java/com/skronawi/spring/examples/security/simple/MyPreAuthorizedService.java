package com.skronawi.spring.examples.security.simple;

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
    @PreAuthorize("hasRole('MGR')")
    public String alsoMgr(){
        return "usrAndMgr";
    }
}
